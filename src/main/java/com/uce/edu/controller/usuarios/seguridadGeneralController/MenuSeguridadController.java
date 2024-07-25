package com.uce.edu.controller.usuarios.seguridadGeneralController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

/**
 * MenuSeguridadController
 * 
 */
@Controller
@RequestMapping("administracion-seguridad")
public class MenuSeguridadController {
    /**
     * Información de los session y url de formularios html
     */
    private final String NOMBRE_NAVBAR = "nombrePasoSeguridad";
    private final String HTLM_MENU_SEGURIDAD = "seguridadGeneral/menus/admi_seguridad_menu";
    private final String HTLM_MENU_SEGURIDAD_ROLES = "seguridadGeneral/menus/admi_seguridad_roles_menu";
    private final String HTLM_MENU_SEGURIDAD_PREGUNTAS = "seguridadGeneral/menus/admi_seguridad_preguntas_menu";
    private final String URL_MENU_PRINCIPAL = "/administracion-seguridad";

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("")
    public String verMenuAdministracionSeguridad(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Administración Seguridad");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloMenu", "Seguridad");
        return HTLM_MENU_SEGURIDAD;
    }

    /**
     * Metodo que permite visualizar el menu de administracion de roles
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-roles")
    public String verMenuAdministracionSeguridadRoles(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Menú Roles");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad-Menú Roles");
        modelo.addAttribute("tituloMenu", "Administar Roles");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);

        return HTLM_MENU_SEGURIDAD_ROLES;
    }

    /**
     * Metodo que permite visualizar el menu de administracion de preguntas
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-preguntas")
    public String verMenuAdministracionSeguridadPreguntas(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Menú Preguntas");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad-Menú Preguntas");
        modelo.addAttribute("tituloMenu", "Administar Preguntas de Seguridad");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);

        return HTLM_MENU_SEGURIDAD_PREGUNTAS;
    }

}
