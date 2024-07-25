package com.uce.edu.controller.usuarios.administracionGeneralController;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.dto.UsuarioDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IPreguntaRecuperacionService;
import com.uce.edu.service.interfacesSistemaPrincipal.IRolService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.service.to.UsuarioTO;
import com.uce.edu.util.Enum.EstadoUsuario;
import com.uce.edu.util.validaciones.ValidadorCedula;

import jakarta.servlet.http.HttpSession;

/**
 * Usuario General Controller
 
 */
@Controller
@RequestMapping("administracion-general")
public class UsuarioGeneralController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Administración General";
    private final String NOMBRE_NAVBAR = "nombrePasoGeneral";
    private final String URL_MENU_PRINCIPAL = "/administracion-general";
    private final String URL_MENU_USUARIOS = "/menu-usuarios";
    private final String URL_LISTA_USUARIOS = "/usuario-ver-todo";
    private final String HTML_URL_LISTA_USUARIOS = "administracionGeneral/listas/usuario_lista";
    private final String HTML_URL_FORMULARIO_USUARIOS = "administracionGeneral/formularios/usuario_formulario";

    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPreguntaRecuperacionService preguntaRecuperacionService;

    /**
     * Datos de los roles
     */
    @SuppressWarnings("unused")
    @Autowired
    private IRolService rolService;

    private List<String> roles;

    public UsuarioGeneralController(IRolService rolService) {
        this.rolService = rolService;
        this.roles = rolService.traerNombredeRoles();
    }

    /**
     * Metodo que permite visualizar el formulario para crear un nuevo usuario
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/usuario-nuevo")
    public String montarFormularioParaNuevoUsuario(Model modelo, HttpSession session) {
        // Atributos del modelo
        // Lista de Roles del usuario del sistema
        modelo.addAttribute("listaRoles", roles);
        // Titulo de la pestaña
        modelo.addAttribute("tituloPestaña", "Crear Usuario");
        // Titulo de la barra de navegacion
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Usuario");
        // Nombre del usuario de la sesion
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Titulo del formulario
        modelo.addAttribute("tituloFormulario", "Crear un nuevo Usuario");
        // Texto del boton de submit para enviar informacion
        modelo.addAttribute("botonSubmit", "Guardar");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_USUARIOS);
        // Url de accion
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/usuario-guardar");
        // Se agrega un nuevo usuario al modelo
        modelo.addAttribute("usuarioTO", new UsuarioTO());
        // Atributo que indica si es un nuevo usuario para la lista de roles
        modelo.addAttribute("esNuevo", true); // o false dependiendo de tu lógica
        return HTML_URL_FORMULARIO_USUARIOS;
    }

    /**
     * Metodo que permite guardar un nuevo usuario
     * 
     * @param usuarioTo
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/usuario-guardar")
    public String guardarUsuario(@Validated UsuarioTO usuarioTo, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        // Atributos del modelo
        // Lista de Roles del usuario del sistema
        modelo.addAttribute("listaRoles", roles);
        // Titulo de la pestaña
        modelo.addAttribute("tituloPestaña", "Crear Usuario");
        // Titulo de la barra de navegacion
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Usuario");
        // Titulo del formulario
        modelo.addAttribute("tituloFormulario", "Crear un nuevo Usuario");
        // Nombre del usuario de la sesion
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Texto del boton de submit para enviar informacion
        modelo.addAttribute("botonSubmit", "Guardar");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_USUARIOS);
        // Url de accion
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/usuario-guardar");
        // Atributo que indica si es un nuevo usuario para la lista de roles
        modelo.addAttribute("esNuevo", true);
        // Validacion de errores
        if (result.hasErrors()) {
            modelo.addAttribute("usuarioTO", usuarioTo);
            modelo.addAttribute("errors", result.getAllErrors());
            return HTML_URL_FORMULARIO_USUARIOS;
        }
        // Validacion de cedula
        if (ValidadorCedula.validarCedula(usuarioTo.getCedula()) == false) {
            usuarioTo.setCedula("");
            modelo.addAttribute("usuarioTo", usuarioTo);
            modelo.addAttribute("mensajeErrorCedula", "Ingrese una cédula valida");
            return HTML_URL_FORMULARIO_USUARIOS;
        }
        // Se obtienen las preguntas de recuperacion
        PreguntasTO preguntasTO = preguntaRecuperacionService.obtenerTresPreguntasAleatorias();
        // Se guarda el usuario
        usuarioService.guardarUsuarioNuevo(usuarioTo, preguntasTO);
        // Se redirige a la lista de usuarios
        redirectAttributes.addFlashAttribute("msgExito", "El Usuario ha sido agregado con exito");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS;
    }

    /**
     * Metodo que permite listar todos los usuarios
     * 
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/usuario-ver-todo")
    public String listarUsuarios(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {

        // Atributos del modelo
        // Titulo de la pestaña
        modelo.addAttribute("tituloPestaña", "Lista Usuarios");
        // Titulo de la barra de navegacion
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Usuarios");
        // Nombre del usuario de la sesion
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Titulo de la lista
        modelo.addAttribute("tituloLista", "Lista de Usuarios");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_USUARIOS);
        // Mensaje de exito (viene de otras vistas)
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }
        // Se agrega la lista de usuarios al modelo
        List<UsuarioDTO> usuarioDTOs = usuarioService.buscarTodoUsuarioDTO();
        modelo.addAttribute("listaUsuariosDTO", usuarioDTOs);
        return HTML_URL_LISTA_USUARIOS;
    }

    /**
     * Metodo que permite buscar un usuario por cedula
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/usuario-buscar-por-cedula")
    public String buscarUsuario(@RequestParam("cedula") String cedula, Model modelo, HttpSession session) {
        // Atributos del modelo
        // Titulo de la pestaña
        modelo.addAttribute("tituloPestaña", "Buscar Usuario");
        // Titulo de la barra de navegacion
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Buscar Usuario");
        // Nombre del usuario de la sesion
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        // Titulo del formulario
        modelo.addAttribute("tituloFormulario", "Buscar Usuario");
        // Url de retorno
        modelo.addAttribute("botonSubmit", "Buscar");
        // Se busca el usuario por cedula
        UsuarioDTO usuarioDTO = usuarioService.buscarUsuarioDTOCedula(cedula);
        // Se agrega el usuario al modelo
        List<UsuarioDTO> usuarioDTOs = new ArrayList<>();
        if (usuarioDTO != null) {
            usuarioDTOs.add(usuarioDTO);
        } else {
            modelo.addAttribute("msgError", "No se encontro ningun rol con ese nombre");
        }
        modelo.addAttribute("listaUsuariosDTO", usuarioDTOs);
        return HTML_URL_LISTA_USUARIOS;
    }

    /**
     * Metodo que permite ver el formulario para actualizar un usuario
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/usuario-actualizar/{cedula}")
    public String montarFormularioParaActualizarUsuario(@PathVariable String cedula, Model modelo,
            HttpSession session) {
        // Atributos del modelo
        // Lista de Roles del usuario del sistema
        modelo.addAttribute("listaRoles", roles);
        // Titulo de la pestaña
        modelo.addAttribute("tituloPestaña", "Actualizar Usuario");
        // Titulo de la barra de navegacion
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Usuario");
        // Nombre del usuario de la sesion
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Titulo del formulario
        modelo.addAttribute("tituloFormulario", "Actualizar Usuario");
        // Texto del boton de submit para enviar informacion
        modelo.addAttribute("botonSubmit", "Actualizar");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS);
        // Url de accion
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS + "/" + cedula);
        // Se agrega el usuario al modelo
        modelo.addAttribute("usuarioTO", usuarioService.crearUsuarioTOPorCedula(cedula));
        return HTML_URL_FORMULARIO_USUARIOS;
    }

    /**
     * Metodo que permite actualizar un usuario
     * 
     * @param cedula
     * @param usuarioTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/usuario-ver-todo/{cedula}")
    public String actualizarUsuario(@PathVariable String cedula, @Validated UsuarioTO usuarioTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        // Atributos del modelo
        // Lista de Roles del usuario del sistema
        modelo.addAttribute("listaRoles", roles);
        // Titulo de la pestaña
        modelo.addAttribute("tituloPestaña", "Actualizar Usuario");
        // Titulo de la barra de navegacion
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Usuario");
        // Nombre del usuario de la sesion
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Titulo del formulario
        modelo.addAttribute("tituloFormulario", "Actualizar Usuario");
        // Texto del boton de submit para enviar informacion
        modelo.addAttribute("botonSubmit", "Actualizar");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS);
        // Url de accion
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS + "/" + cedula);

        // Validacion de errores
        if (result.hasErrors()) {
            modelo.addAttribute("usuarioTO", usuarioTO);
            return HTML_URL_FORMULARIO_USUARIOS;
        }
        // Validacion de cedula
        if (ValidadorCedula.validarCedula(usuarioTO.getCedula()) == false) {
            usuarioTO.setCedula("");
            modelo.addAttribute("usuarioTo", usuarioTO);
            modelo.addAttribute("mensajeErrorCedula", "Ingrese una cédula valida");
            return HTML_URL_FORMULARIO_USUARIOS;
        }
        // Se actualiza el usuario
        usuarioService.actualizarUsuario(usuarioTO);
        // Se redirige a la lista de usuarios
        redirectAttributes.addFlashAttribute("msgExito", "El Usuario ha sido actualizado correctamente");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS;
    }

    /**
     * Metodo que permite cambiar el estado de un usuario a activo
     * 
     * @param cedula
     * @param redirectAttributes
     * @param modelo
     * @return
     */
    @PostMapping("/usuario-cambiar-estado-activar/{cedula}")
    public String cambiarEstadoActivo(@PathVariable String cedula, RedirectAttributes redirectAttributes,
            Model modelo) {
        // Se cambia el estado del usuario a activo
        String estado = EstadoUsuario.ACTIVO.getEstado();
        // Se actualiza el estado del usuario
        usuarioService.actualizarEstadoUsuario(cedula, estado);
        // Se redirige a la lista de usuarios
        redirectAttributes.addFlashAttribute("msgExito", "Se cambio el estado a ACTIVO con exito");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS;
    }

    /**
     * Metodo que permite cambiar el estado de un usuario a inactivo
     * 
     * @param cedula
     * @param redirectAttributes
     * @param modelo
     * @return
     */
    @PostMapping("/usuario-cambiar-estado-desactivar/{cedula}")
    public String cambiarEstadoBloquedo(@PathVariable String cedula, RedirectAttributes redirectAttributes,
            Model modelo) {
        // Se cambia el estado del usuario a inactivo
        String estado = EstadoUsuario.INACTIVO.getEstado();
        // Se actualiza el estado del usuario
        usuarioService.actualizarEstadoUsuario(cedula, estado);
        // Se redirige a la lista de usuarios
        redirectAttributes.addFlashAttribute("msgExito", "Se cambio el estado a INACTIVO con exito");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_USUARIOS;
    }

}
