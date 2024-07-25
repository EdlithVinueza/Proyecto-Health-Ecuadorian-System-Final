package com.uce.edu.controller.usuarios.secretariaGeneralController;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
/**
 * MenuSecretariaController
 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class MenuSecretariaController {
    /**
     * Información de los session y url de formularios html
     */
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String HTLM_MENU_SECRETARIA = "secretariaGeneral/menus/admi_secretaria_menu";
    private final String HTLM_MENU_PACIENTES = "secretariaGeneral/menus/admi_secretaria_menu_paciente";
    private final String HTLM_MENU_CITAS = "secretariaGeneral/menus/admi_secretaria_menu_cita";
    private final String HTLM_MENU_ORDEN_COBRO = "secretariaGeneral/menus/admi_secretaria_menu_orden_cobro";

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
        return HTLM_MENU_SECRETARIA;
    }
    /**
     * Metodo que permite visualizar el menu de pacientes de modulo de secretaria
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-pacientes")
    public String verMenuSecretariaPacientes(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Secretaria - Menú Pacientes");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return HTLM_MENU_PACIENTES;
    }

    /**
     * Metodo que permite visualizar el menu de citas de modulo de secretaria
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-citas")
    public String verMenuSecretariaCitas(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Secretaria - Menú Cita");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return HTLM_MENU_CITAS;
    }

    /**
     * Metodo que permite visualizar el menu de orden de cobro de modulo de secretaria
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-orden-cobro")
    public String verMenuSecretariaOrdenCobro(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Secretaria - Menú Orden Cobro");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
            
        return HTLM_MENU_ORDEN_COBRO;
    }

}
