package com.uce.edu.controller.doctores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.to.CambiarContrasena;
import com.uce.edu.util.Enum.EstadoSeguridad;

import jakarta.servlet.http.HttpSession;
/**
 * CambiarContraseñaDoctores
 */
@Controller
@RequestMapping("")
public class CambiarContraseñaDoctores {

    /**
     * HTML_FORMULARIO_CONTRASENIA
     */
    private final String HTML_FORMULARIO_CONTRASENIA = "cambiar_contrasenia_usuario";
    
    /*
     * Dependencias
     */
    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IEncryptSercive encryptSercive;

    /**
     * Actualizar contraseña del doctor
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/doctores-actualizar-contraseña")
    public String actualizarContraseia(Model modelo, HttpSession session) {
        // sesión
        modelo.addAttribute("tituloPestaña", "Actualizar Contraseña");
        modelo.addAttribute("tituloFormulario", "Actualizar Preguntas");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/doctores-guardar-cambio-contraseña");
        modelo.addAttribute("c", new CambiarContrasena());

        return HTML_FORMULARIO_CONTRASENIA;
    }

    /**
     * Cambiar contraseña del doctor
     * @param c
     * @param result
     * @param redirectAttributes
     * @param session
     * @param modelo
     * @return
     */
    @PostMapping("/doctores-guardar-cambio-contraseña")
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
        Doctor doctor =  doctorRepository.findByCedula(c.getCedula());
        if (doctor == null) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "El usuario no existe");
            return HTML_FORMULARIO_CONTRASENIA;
        }
        if (doctor.getContrasena().equals(c.getNuevaContrasenia())) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "La contraseña no puede ser igual a la anterior");
            return HTML_FORMULARIO_CONTRASENIA;
        }
        if (doctor.getCedula().equals(c.getNuevaContrasenia())) {
            modelo.addAttribute("c", c);
            modelo.addAttribute("msgError", "La contraseña no puede ser igual a la cédula");
            return HTML_FORMULARIO_CONTRASENIA;
        }

        if (c.getNuevaContrasenia().equals(c.getNuevaContraseniaRepetida())) {
            doctor.setContrasena(encryptSercive.encriptar(c.getNuevaContrasenia()));
            doctor.setEstadoSeguridad(EstadoSeguridad.INACTIVO.getEstado());
            doctorRepository.save(doctor);
            redirectAttributes.addFlashAttribute("msgExito", "Se actualizó la contraseña con éxito");
            return "redirect:/login-usuarios";
        }
        modelo.addAttribute("c", c);
        modelo.addAttribute("msgError", "Las contraseñas no coinciden");

        return HTML_FORMULARIO_CONTRASENIA;
    }

}