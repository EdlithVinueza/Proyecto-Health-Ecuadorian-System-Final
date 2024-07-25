package com.uce.edu.controller.usuarios.administracionGeneralController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/**
 * Menu Adminstracion Genera lController
 * 
 */
@Controller
@RequestMapping("administracion-general")
public class MenuAdminstracionGeneralController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Administración General";
    private final String NOMBRE_NAVBAR = "nombrePasoGeneral";
    private final String URL_MENU_PRINCIPAL = "/administracion-general";
    private final String HTLM_MENU_GENERAL = "administracionGeneral/menus/admi_general_menu";
    private final String HTLM_MENU_USUARIOS = "administracionGeneral/menus/admi_general_menu_usuario";
    private final String HTLM_MENU_DOCTORES = "administracionGeneral/menus/admi_general_menu_doctor";
    private final String HTLM_MENU_PACIENTES = "administracionGeneral/menus/admi_general_menu_paciente";

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("")
    public String verMenuAdministracionGeneral(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Administración General");
        modelo.addAttribute("tituloPestaña", TITULO_NAVBAR);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", "/login-usuarios");
        return HTLM_MENU_GENERAL;
    }

    /**
     * Metodo que permite visualizar el menu de usuarios
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("menu-usuarios")
    public String verMenuAdministracionGeneralUsuarios(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Administración General - Menú Usuarios");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL); // URL para regresar al menú de doctores
        return HTLM_MENU_USUARIOS;
    }

    /**
     * Metodo que permite visualizar el menu de doctores
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("menu-doctores")
    public String verMenuAdministracionGeneralDoctores(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Administración General - Menú Doctores");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return HTLM_MENU_DOCTORES;
    }

    /**
     * Metodo que permite visualizar el menu de pacientes
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("menu-pacientes")
    public String verMenuAdministracionGeneralPacientes(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Administración General - Menú Pacientes");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return HTLM_MENU_PACIENTES;
    }

}
