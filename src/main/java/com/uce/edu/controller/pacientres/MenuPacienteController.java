package com.uce.edu.controller.pacientres;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
/**
 * Menu Paciente Controller
 
 */
@Controller
@RequestMapping("pacientes")
public class MenuPacienteController {

    /**
     * Información de los session y url de formularios html
     */
    private final String NOMBRE_NAVBAR = "nombrePasoPaciente";
    private final String HTLM_MENU_PACIENTES = "pacientes/menus/paciente_menu_principal";
    private final String HTLM_MENU_CITAS = "pacientes/menus/pacientes_menu_cita";
    
    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("")
    public String verMenuSecretaria(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Administración General - Menú Usuarios");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        return HTLM_MENU_PACIENTES;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-citas")
    public String verMenuSecretariaCitas(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Secretaria - Menú Cita");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", "/pacientes");
            return HTLM_MENU_CITAS;
    }

}
