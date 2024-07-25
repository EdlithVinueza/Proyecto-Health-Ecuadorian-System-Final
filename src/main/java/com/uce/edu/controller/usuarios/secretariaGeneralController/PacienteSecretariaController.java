package com.uce.edu.controller.usuarios.secretariaGeneralController;

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

import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.interfacesSistemaPrincipal.IPreguntaRecuperacionService;
import com.uce.edu.service.to.PacienteTO;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.util.Enum.Genero;
import com.uce.edu.util.Enum.RespuestaCorta;
import com.uce.edu.util.Enum.TipoDiscapacidad;
import com.uce.edu.util.validaciones.ValidadorCedula;

import jakarta.servlet.http.HttpSession;

/**
 * PacienteSecretariaController
 * 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class PacienteSecretariaController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Secretaria";
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String URL_MENU_PACIENTES = "/menu-pacientes";
    private final String URL_LISTA_PACIENTES = "/paciente-ver-todo";
    private final String HTML_URL_LISTA_PACIENTES = "secretariaGeneral/listas/paciente_lista";
    private final String HTML_URL_FORMULARIO_PACIENTE = "secretariaGeneral/formularios/paciente_formulario";

    /**
     * Dependencias
     */
    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private IPreguntaRecuperacionService preguntaRecuperacionService;

    /**
     * Listas estáticas para almacenar los tipos de discapacidad, género y
     * respuestas cortas
     */
    private static final List<String> tiposDiscapacidad;
    static {
        tiposDiscapacidad = TipoDiscapacidad.getDisplayNames();
    }
    private static final List<String> tiposGenero;
    static {
        tiposGenero = Genero.getDisplayNames();
    }
    private static final List<String> bandera;
    static {
        bandera = RespuestaCorta.getDisplayNames();
    }

    /**
     * Método que permite visualizar el formulario para crear un nuevo paciente
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/paciente-nuevo")
    public String montarFormularioParaNuevoPaciente(Model modelo, HttpSession session) {
        PreguntasTO preguntasTO = preguntaRecuperacionService.obtenerTresPreguntasAleatorias(); // Obtener tres
                                                                                                // preguntas aleatorias

        modelo.addAttribute("esNuevo", true); // Indicador de nuevo paciente
        session.setAttribute("preguntasTO", preguntasTO); // Guardar preguntas en la sesión
        modelo.addAttribute("tituloPestaña", "Crear Paciente"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear paciente"); // Título de la barra de navegación
        modelo.addAttribute("tituloFormulario", "Crear un nuevo paciente"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Guardar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PACIENTES); // URL para regresar al menú de
                                                                                   // pacientes
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/paciente-guardar"); // Acción del formulario
        modelo.addAttribute("preguntaUno", preguntasTO.getPreguntaUno()); // Primera pregunta de recuperación
        modelo.addAttribute("preguntaDos", preguntasTO.getPreguntaDos()); // Segunda pregunta de recuperación
        modelo.addAttribute("preguntaTres", preguntasTO.getPreguntaTres()); // Tercera pregunta de recuperación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR); // Obtener el nombre del usuario de la
                                                                             // sesión
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Añadir el nombre del usuario al modelo
        modelo.addAttribute("pacienteTO", new PacienteTO()); // Crear un nuevo objeto PacienteTO
        modelo.addAttribute("listaBandera", bandera); // Lista de respuestas cortas
        modelo.addAttribute("listaGenero", tiposGenero); // Lista de tipos de género
        modelo.addAttribute("listaTiposDiscapacidad", tiposDiscapacidad); // Lista de tipos de discapacidad

        return HTML_URL_FORMULARIO_PACIENTE; // Retornar la vista del formulario
    }

    /**
     * Método que permite guardar un nuevo paciente
     * 
     * @param pacienteTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/paciente-guardar")
    public String guardarPaciente(@Validated @ModelAttribute("pacienteTO") PacienteTO pacienteTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        PreguntasTO preguntasTO = (PreguntasTO) session.getAttribute("preguntasTO");
        if (preguntasTO == null) {
            preguntasTO = preguntaRecuperacionService.obtenerTresPreguntasAleatorias();
            session.setAttribute("preguntasTO", preguntasTO);
        }
        modelo.addAttribute("esNuevo", true); // Indicador de nuevo paciente
        modelo.addAttribute("listaBandera", bandera); // Lista de respuestas cortas
        modelo.addAttribute("listaGenero", tiposGenero); // Lista de tipos de género
        modelo.addAttribute("listaTiposDiscapacidad", tiposDiscapacidad); // Lista de tipos de discapacidad

        modelo.addAttribute("tituloPestaña", "Crear paciente"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear paciente"); // Título de la barra de navegación
        modelo.addAttribute("tituloFormulario", "Crear un nuevo paciente"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Guardar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PACIENTES); // URL para regresar al menú de
                                                                                   // pacientes
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/paciente-guardar"); // Acción del formulario
        modelo.addAttribute("preguntaUno", preguntasTO.getPreguntaUno()); // Primera pregunta de recuperación
        modelo.addAttribute("preguntaDos", preguntasTO.getPreguntaDos()); // Segunda pregunta de recuperación
        modelo.addAttribute("preguntaTres", preguntasTO.getPreguntaTres()); // Tercera pregunta de recuperación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR); // Obtener el nombre del usuario de la
                                                                             // sesión
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Añadir el nombre del usuario al modelo

        if (result.hasErrors()) {
            modelo.addAttribute("pacienteTO", pacienteTO); // Objeto PacienteTO con errores de validación
            modelo.addAttribute("errors", result.getAllErrors()); // Errores de validación
            return HTML_URL_FORMULARIO_PACIENTE; // Retornar la vista del formulario con errores
        }
        if (!ValidadorCedula.validarCedula(pacienteTO.getCedula())) { // Validar la cédula
            pacienteTO.setCedula("");
            modelo.addAttribute("pacienteTO", pacienteTO); // Objeto PacienteTO con cédula inválida
            modelo.addAttribute("mensajeErrorCedula", "Ingrese una cédula válida"); // Mensaje de error de cédula
            return HTML_URL_FORMULARIO_PACIENTE; // Retornar la vista del formulario con errores
        }
        pacienteService.guardarPacienteNuevo(pacienteTO, preguntasTO); // Guardar el nuevo paciente
        redirectAttributes.addFlashAttribute("msgExito", "El paciente ha sido agregado con éxito"); // Mensaje de éxito
        return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PACIENTES; // Redirigir al menú de pacientes
    }

    /**
     * Método que permite ver toda la lista de pacientes
     * 
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/paciente-ver-todo")
    public String listarPacientes(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        List<PacienteDTO> pacientesDTO = pacienteService.listarPacientes(); // Obtener la lista de pacientes

        modelo.addAttribute("tituloPestaña", "Lista pacientes"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver pacientes"); // Título de la barra de navegación
        modelo.addAttribute("tituloLista", "Lista de pacientes"); // Título de la lista
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR); // Obtener el nombre del usuario de la
                                                                             // sesión
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Añadir el nombre del usuario al modelo
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito); // Mensaje de éxito
        }
        if (msgEliminacionExitosa != null) {
            modelo.addAttribute("msgEliminacionExitosa", msgEliminacionExitosa); // Mensaje de eliminación exitosa
        }

        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PACIENTES); // URL para regresar al menú de
                                                                                   // pacientes
        modelo.addAttribute("pacientesDTO", pacientesDTO); // Añadir la lista de pacientes al modelo
        return HTML_URL_LISTA_PACIENTES; // Retornar la vista de la lista de pacientes
    }

    /**
     * Método que permite buscar un paciente por cédula
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/paciente-buscar-por-cedula")
    public String buscarPaciente(@RequestParam("cedula") String cedula, Model modelo, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Buscar paciente"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Buscar paciente"); // Título de la barra de navegación
        modelo.addAttribute("tituloFormulario", "Buscar paciente"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Buscar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PACIENTES); // URL para regresar al menú de
                                                                                   // pacientes
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR); // Obtener el nombre del usuario de la
                                                                             // sesión
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Añadir el nombre del usuario al modelo
        PacienteDTO pacienteDTO = pacienteService.buscarPacientePorCedula(cedula); // Buscar paciente por cédula
        List<PacienteDTO> pacientesDTO = new ArrayList<>();
        if (pacienteDTO != null) {
            pacientesDTO.add(pacienteDTO); // Añadir el paciente encontrado a la lista
        } else {
            modelo.addAttribute("msgError", "No se encontró ningún paciente con esa cédula"); // Mensaje de error si no
                                                                                              // se encuentra el
                                                                                              // paciente
        }
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PACIENTES); // URL para regresar al menú de
                                                                                   // pacientes
        modelo.addAttribute("pacientesDTO", pacientesDTO); // Añadir la lista de pacientes al modelo
        return HTML_URL_LISTA_PACIENTES; // Retornar la vista de la lista de pacientes
    }

    /**
     * Método que permite ver el formulario para actualizar un paciente
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/paciente-actualizar/{cedula}")
    public String montarFormularioParaActualizarPaciente(@PathVariable String cedula, Model modelo,
            HttpSession session) {
        List<String> bandera = pacienteService.bandera(); // Obtener la lista de banderas
        modelo.addAttribute("listaBandera", bandera); // Añadir la lista de banderas al modelo
        modelo.addAttribute("listaTiposDiscapacidad", tiposDiscapacidad); // Añadir la lista de tipos de discapacidad al
                                                                          // modelo
        modelo.addAttribute("tituloPestaña", "Actualizar paciente"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar paciente"); // Título de la barra de
                                                                                       // navegación
        modelo.addAttribute("tituloFormulario", "Actualizar paciente"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Actualizar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_PACIENTES); // URL para regresar a la lista de
                                                                                    // pacientes
        String urlAction = URL_MENU_PRINCIPAL + URL_LISTA_PACIENTES + "/" + cedula; // Acción del formulario
        modelo.addAttribute("urlAction", urlAction); // Añadir la acción del formulario al modelo
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR); // Obtener el nombre del usuario de la
                                                                             // sesión
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Añadir el nombre del usuario al modelo

        PacienteDTO tmp = pacienteService.buscarPacientePorCedula(cedula); // Buscar paciente por cédula
        PacienteTO pacienteTO = pacienteService.convertirPacienteDTOAPacienteTO(tmp); // Convertir PacienteDTO a
                                                                                      // PacienteTO
        modelo.addAttribute("pacienteTO", pacienteTO); // Añadir PacienteTO al modelo
        return HTML_URL_FORMULARIO_PACIENTE; // Retornar la vista del formulario
    }

    /**
     * Método que permite actualizar un paciente
     * 
     * @param cedula
     * @param pacienteTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/paciente-ver-todo/{cedula}")
    public String actualizarPaciente(@PathVariable String cedula, @Validated PacienteTO pacienteTO,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR); // Obtener el nombre del usuario de la
                                                                             // sesión
        List<String> bandera = pacienteService.bandera(); // Obtener la lista de banderas
        modelo.addAttribute("listaBandera", bandera); // Añadir la lista de banderas al modelo
        modelo.addAttribute("listaTiposDiscapacidad", tiposDiscapacidad); // Añadir la lista de tipos de discapacidad al
                                                                          // modelo
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Añadir el nombre del usuario al modelo

        if (result.hasErrors()) {
            modelo.addAttribute("pacienteTO", pacienteTO); // Objeto PacienteTO con errores de validación
            modelo.addAttribute("tituloPestaña", "Actualizar paciente"); // Título de la pestaña
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar paciente"); // Título de la barra de
                                                                                           // navegación
            modelo.addAttribute("tituloFormulario", "Actualizar paciente"); // Título del formulario
            modelo.addAttribute("botonSubmit", "Actualizar"); // Texto del botón de submit
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_PACIENTES); // URL para regresar a la lista
                                                                                        // de pacientes
            String urlAction = URL_MENU_PRINCIPAL + URL_LISTA_PACIENTES + "/" + cedula; // Acción del formulario
            modelo.addAttribute("urlAction", urlAction); // Añadir la acción del formulario al modelo
            return HTML_URL_FORMULARIO_PACIENTE; // Retornar la vista del formulario con errores
        }

        if (!ValidadorCedula.validarCedula(pacienteTO.getCedula())) { // Validar la cédula
            pacienteTO.setCedula("");
            modelo.addAttribute("pacienteTO", pacienteTO); // Objeto PacienteTO con cédula inválida
            modelo.addAttribute("mensajeErrorCedula", "Ingrese una cédula válida"); // Mensaje de error de cédula
            return HTML_URL_FORMULARIO_PACIENTE; // Retornar la vista del formulario con errores
        }

        pacienteTO.setCedula(cedula); // Establecer la cédula del paciente
        pacienteService.actualizarPaciente(pacienteTO); // Actualizar el paciente
        redirectAttributes.addFlashAttribute("msgExito", "El paciente ha sido actualizado correctamente"); // Mensaje de
                                                                                                           // éxito
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_PACIENTES; // Redirigir a la lista de pacientes
    }
}
