package com.uce.edu.controller.usuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.to.CambiarContrasena;
import com.uce.edu.util.Enum.EstadoSeguridad;

import jakarta.servlet.http.HttpSession;

/**
 * CambiarContraseñaUsuario
 */
@Controller
@RequestMapping("")
public class CambiarContraseñaUsuario {
    /**
     * Información de los session y url de formularios html
     */

    private final String HTML_FORMULARIO_CONTRASENIA = "cambiar_contrasenia_usuario";
    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioRepository usuarioService;

    @Autowired
    private IEncryptSercive encryptSercive;

    /**
     * Metodo que permite visualizar el formulario de cambio de contraseña
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/usuario-actualizar-contraseña")
    public String actualizarContraseia(Model modelo, HttpSession session) {
        // sesión
        modelo.addAttribute("tituloPestaña", "Actualizar Contraseña");
        modelo.addAttribute("tituloFormulario", "Actualizar Preguntas");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/usuarios-guardar-cambio-contraseña");
        modelo.addAttribute("c", new CambiarContrasena());

        return HTML_FORMULARIO_CONTRASENIA;
    }

    /**
     * Metodo que permite guardar el cambio de contraseña
     * 
     * @param c
     * @param result
     * @param redirectAttributes
     * @param session
     * @param modelo
     * @return
     */

    @PostMapping("/usuarios-guardar-cambio-contraseña")
    public String cambiarContrasenia(@Validated CambiarContrasena c, BindingResult result,
            RedirectAttributes redirectAttributes, HttpSession session, Model modelo) {
        modelo.addAttribute("tituloPestaña", "Actualizar Contraseña");
        modelo.addAttribute("tituloFormulario", "Actualizar Preguntas");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/usuarios-guardar-cambio-contraseña");

        if (result.hasErrors()) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("errors", result.getAllErrors());

            return HTML_FORMULARIO_CONTRASENIA;
        }
        Usuario usuario = usuarioService.findByCedula(c.getCedula());
        if (usuario == null) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "El usuario no existe");
            return HTML_FORMULARIO_CONTRASENIA;

        }
        if (usuario.getContrasena().equals(c.getNuevaContrasenia())) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "La contraseña no puede ser igual a la anterior");
            return HTML_FORMULARIO_CONTRASENIA;
        }
        if (usuario.getCedula().equals(c.getNuevaContrasenia())) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "La contraseña no puede ser igual a la cédula");
            return HTML_FORMULARIO_CONTRASENIA;
        }
        if (c.getNuevaContrasenia().equals(c.getNuevaContraseniaRepetida())) {
            usuario.setContrasena(encryptSercive.encriptar(c.getNuevaContrasenia()));
            usuario.setEstadoSeguridad(EstadoSeguridad.INACTIVO.getEstado());
            usuarioService.save(usuario);
            redirectAttributes.addFlashAttribute("msgExito", "Se actualizó la contraseña con éxito");
            return "redirect:/login-usuarios";
        }
        modelo.addAttribute("c", c);
        modelo.addAttribute("msgError", "Las contraseñas no coinciden");

        return HTML_FORMULARIO_CONTRASENIA;
    }

}