package com.uce.edu.controller.pacientres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.interfacesSistemaGeneral.IPacienteRepository;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.to.CambiarContrasena;
import com.uce.edu.util.Enum.EstadoSeguridad;

import jakarta.servlet.http.HttpSession;

/**
 * Cambiar Contraseña Pacientes
 * 
 */
@Controller
@RequestMapping("")
public class CambiarContraseñaPacientes {
    /**
     * Formulario de cambio de contraseña
     */
    private final String HTML_FORMULARIO_CONTRASENIA = "cambiar_contrasenia_usuario";
    /**
     * Dependencias
     */
    @Autowired
    private IPacienteRepository pacienteRepository;

    @Autowired
    private IEncryptSercive encryptSercive;

    /**
     * Actualizar contraseña del paciente
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/paciente-actualizar-contraseña")
    public String actualizarContraseia(Model modelo, HttpSession session) {
        // sesión
        modelo.addAttribute("tituloPestaña", "Actualizar Contraseña");
        modelo.addAttribute("tituloFormulario", "Actualizar Preguntas");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/paciente-guardar-cambio-contraseña");
        modelo.addAttribute("c", new CambiarContrasena());

        return HTML_FORMULARIO_CONTRASENIA;
    }

    /**
     * Cambiar contraseña del paciente
     * 
     * @param c
     * @param result
     * @param redirectAttributes
     * @param session
     * @param modelo
     * @return
     */
    @PostMapping("/paciente-guardar-cambio-contraseña")
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
        Paciente paciente = pacienteRepository.findByCedula(c.getCedula());
        if (paciente == null) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "El usuario no existe");
            return HTML_FORMULARIO_CONTRASENIA;
        }
        if (paciente.getContrasena().equals(c.getNuevaContrasenia())) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "La contraseña no puede ser igual a la anterior");
            return HTML_FORMULARIO_CONTRASENIA;
        }
        if (paciente.getCedula().equals(c.getNuevaContrasenia())) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "La contraseña no puede ser igual a la cédula");
            return HTML_FORMULARIO_CONTRASENIA;
        }

        if (c.getNuevaContrasenia().equals(c.getNuevaContraseniaRepetida())) {
            paciente.setContrasena(encryptSercive.encriptar(c.getNuevaContrasenia()));
            paciente.setEstadoSeguridad(EstadoSeguridad.INACTIVO.getEstado());
            pacienteRepository.save(paciente);
            redirectAttributes.addFlashAttribute("msgExito", "Se actualizó la contraseña con éxito");
            return "redirect:/login-usuarios";
        }
        modelo.addAttribute("c", c);
        modelo.addAttribute("msgError", "Las contraseñas no coinciden");

        return HTML_FORMULARIO_CONTRASENIA;
    }

}