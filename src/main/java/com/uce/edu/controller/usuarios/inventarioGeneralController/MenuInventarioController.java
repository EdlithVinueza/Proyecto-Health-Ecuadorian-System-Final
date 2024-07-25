package com.uce.edu.controller.usuarios.inventarioGeneralController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;

import jakarta.servlet.http.HttpSession;

/**
 * MenuInventarioController
 
 */
@Controller
@RequestMapping("administracion-inventario")
public class MenuInventarioController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Inventario";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";

    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioService usuarioService;

    /**
     * Metodo que permite visualizar el menu de administracion inventario
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("")
    public String verMenuInventario(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        String tmpCedula = "1751674027";
        session.setAttribute("cedulaPasoEncargado", tmpCedula);
        String nombreEncargado = "Hola, " + usuarioService.nombreUsuario(tmpCedula);
        session.setAttribute("nombrePasoEncargado", nombreEncargado);
        modelo.addAttribute("nombreEncargado", nombreEncargado);
        modelo.addAttribute("returnUrl", "/login-usuarios");
        return "inventarioGeneral/menus/admi_inventario_menu";
    }

    /**
     * Metodo que permite visualizar el menu de administracion preveedores
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-proveedores")
    public String verMenuInventarioProveedores(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR+ " - Menú Proveedores");
        String nombreUsuario = (String) session.getAttribute("nombrePasoEncargado");
        modelo.addAttribute("nombreEncargado", nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return "inventarioGeneral/menus/admi_inventario_menu_proveedor";
    }

    /**
     * Metodo que permite visualizar el menu de administracion insumos medicos
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-insumo-medico")
    public String verMenuInventarioInsumoMedico(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR+ " - Menú Insumo Médico");
        String nombreUsuario = (String) session.getAttribute("nombrePasoEncargado");
        modelo.addAttribute("nombreEncargado", nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return "inventarioGeneral/menus/admi_inventario_menu_insumo_medico";
    }

    /**
     * Metodo que permite visualizar el menu de administracion medicamentos
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/menu-medicamento")
    public String verMenuInventarioMedicamento(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR+ " - Menú Medicinas");
        String nombreUsuario = (String) session.getAttribute("nombrePasoEncargado");
        modelo.addAttribute("nombreEncargado", nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return "inventarioGeneral/menus/admi_inventario_menu_medicamento";
    }

    /**
     * Metodo que permite visualizar el menu de administracion pedidos
     * @param modelo
     * @param session
     * @return
     */
    
    @GetMapping("/menu-pedido")
    public String verMenuInventarioPedido(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR+ " - Menú Pedidos");
        String nombreUsuario = (String) session.getAttribute("nombrePasoEncargado");
        modelo.addAttribute("nombreEncargado", nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        return "inventarioGeneral/menus/admi_inventario_menu_pedido";
    }

}
