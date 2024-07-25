package com.uce.edu.controller.usuarios.seguridadGeneralController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.uce.edu.repository.modelo.Rol;
import com.uce.edu.service.interfacesSistemaPrincipal.IRolService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/**
 * RolController
 
 */
@Controller
@RequestMapping("administracion-seguridad")
public class RolController {

    /**
     * Información de los session y url de formularios html
     */
    private final String NOMBRE_NAVBAR = "nombrePasoSeguridad";
    private final String HTML_URL_FORMULARIO_ROL = "seguridadGeneral/formularios/rol_formulario";
    private final String HTML_URL_LISTA_ROL = "seguridadGeneral/listas/rol_lista";

    /**
     * Dependencias
     */
    @Autowired
    private IRolService rolService;

    /**
     * Metodo que permite visualizar el formulario de creacion de un nuevo rol
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/rol-nuevo")
    public String montarFormularioParaNuevoRol(Model modelo, HttpSession session) {

        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloPestaña", "Crear Rol");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad - Crear Roles");
        modelo.addAttribute("tituloFormulario", "Crear un nuevo Rol");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", "/administracion-seguridad");
        modelo.addAttribute("urlAction", "/administracion-seguridad/rol-guardar");

        modelo.addAttribute("rol", new Rol());

        return HTML_URL_FORMULARIO_ROL;
    }

    /**
     * Metodo que permite guardar un nuevo rol
     * @param rol
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/rol-guardar")
    public String guardarRol(@Validated Rol rol, BindingResult result, RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloPestaña", "Crear Rol");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad - Crear Roles");
        modelo.addAttribute("tituloFormulario", "Crear un nuevo Rol");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", "/administracion-seguridad");

        if (result.hasErrors()) {
            modelo.addAttribute("rol", rol);
            return HTML_URL_FORMULARIO_ROL;
        }

        if (!rolService.guardarRolSiNoEstaRepetido(rol)) {
            modelo.addAttribute("rol", rol);
            modelo.addAttribute("errorUnique", "Ya existe un rol con ese nombre");
            return HTML_URL_FORMULARIO_ROL;
        }

        redirectAttributes.addFlashAttribute("msgExito", "El rol ha sido agregado con exito");

        return "redirect:/administracion-seguridad/rol-ver-todo";
    }

    /**
     * Metodo que permite visualizar todos los roles
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/rol-ver-todo")
    public String listarRoles(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        List<Rol> roles = rolService.buscarTodosLosRoles();

        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloPestaña", "Lista Roles");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad - Ver Roles");
        modelo.addAttribute("tituloLista", "Lista de Roles");
        modelo.addAttribute("returnUrl", "/administracion-seguridad");

        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }
        if (msgExito != null) {
            modelo.addAttribute("msgEliminacionExitosa", msgEliminacionExitosa);
        }

        modelo.addAttribute("listaRoles", roles);

        return HTML_URL_LISTA_ROL;
    }

    /**
     * Metodo que permite buscar un rol por nombre
     * @param nombre
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/rol-buscar-por-nombre")
    public String buscarRol(@RequestParam("nombre") String nombre, Model modelo, HttpSession session) {
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloPestaña", "Buscar Rol");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad - Buscar Rol");
        modelo.addAttribute("tituloFormulario", "Buscar Rol");
        modelo.addAttribute("botonSubmit", "Buscar");
        modelo.addAttribute("returnUrl", "/administracion-seguridad");

        Rol rol = rolService.buscarUnRolPorNombre(nombre);
        List<Rol> roles = new ArrayList<>();
        if (rol != null) {
            roles.add(rol);
        } else {
            modelo.addAttribute("msgError", "No se encontro ningun rol con ese nombre");
        }
        modelo.addAttribute("listaRoles", roles);
        return HTML_URL_LISTA_ROL;
    }

    /**
     * Metodo que permite visualizar el formulario de actualizacion de un rol
     * @param id
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/rol-actualizar/{id}")
    public String montarFormularioParaActualizar(@PathVariable Integer id, Model modelo, HttpSession session) {
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloPestaña", "Actualizar Rol");
        modelo.addAttribute("tituloNavbar", "Administración General - Actualizar Roles");
        modelo.addAttribute("tituloFormulario", "Actualizar Rol");
        modelo.addAttribute("botonSubmit", "Actualizar");
        modelo.addAttribute("returnUrl", "/administracion-seguridad/rol-ver-todo");
        String urlAction = "/administracion-seguridad/rol-ver-todo/" + id;
        /**
         * Si llamaramos directamente en el formuario de rol la accion seria:
         * th:action="@{/administracion-seguridad/rol-ver-todo/{id}(id=${rol.id})}"
         */
        modelo.addAttribute("urlAction", urlAction);

        modelo.addAttribute("rol", rolService.obtenerRolPorId(id));
        return HTML_URL_FORMULARIO_ROL;
    }
    /*
     * Metodo que permite actualizar un rol
     */
    @PostMapping("/rol-ver-todo/{id}")
    public String actualizarRol(@PathVariable Integer id, @Validated Rol rol, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloPestaña", "Actualizar Rol");
        modelo.addAttribute("tituloNavbar", "Administración Seguridad - Actualizar Roles");
        modelo.addAttribute("tituloFormulario", "Actualizar Rol");
        modelo.addAttribute("botonSubmit", "Actualizar");
        modelo.addAttribute("returnUrl", "/administracion-seguridad/rol-ver-todo");
        String urlAction = "/administracion-seguridad/rol-ver-todo/" + id;
        modelo.addAttribute("urlAction", urlAction);
        if (result.hasErrors()) {
            modelo.addAttribute("rol", rol);

            return HTML_URL_FORMULARIO_ROL;
        }

        Rol rolExistente = rolService.obtenerRolPorId(id);
        rolExistente.setId(rol.getId());
        rolExistente.setNombre(rol.getNombre());
        rolExistente.setDescripcion(rol.getDescripcion());
        rolService.actualizarRol(rolExistente);

        redirectAttributes.addFlashAttribute("msgExito", "El rol ha sido actualizado correctamente");
        return "redirect:/administracion-seguridad/rol-ver-todo";
    }

    /**
     * Metodo que permite eliminar un rol
     * @param id
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/administracion-seguridad/rol-eliminar/{id}")
    public String eliminarRol(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        rolService.eliminarRolPorId(id);

        redirectAttributes.addAttribute("msgEliminacionExitosa", "Se elimino con exito el rol");

        return "redirect:/administracion-seguridad/rol-ver-todo";
    }

}
