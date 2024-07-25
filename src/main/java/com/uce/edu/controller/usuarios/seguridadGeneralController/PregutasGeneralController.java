package com.uce.edu.controller.usuarios.seguridadGeneralController;

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

import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.service.interfacesSistemaPrincipal.IPreguntaRecuperacionService;

import jakarta.servlet.http.HttpSession;

/**
 * PregutasGeneralController
 */
@Controller
@RequestMapping("administracion-seguridad")
public class PregutasGeneralController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Administración General";
    private final String NOMBRE_NAVBAR = "nombreUsuario";
    private final String URL_MENU_PRINCIPAL = "/administracion-seguridad";
    private final String URL_LISTA_SEGURIDAD = "/pregunta-ver-todo";
    private final String HTML_URL_FORMULARIO_PREGUNTA = "seguridadGeneral/formularios/pregunta_formulario";
    private final String HTML_URL_LISTA_PREGUNTA = "seguridadGeneral/listas/pregunta_lista";
    /**
     * Dependencias
     */
    @Autowired
    private IPreguntaRecuperacionService preguntaRecuperacionService;

    /**
     * Metodo que permite visualizar el formulario de crear una nueva pregunta
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pregunta-nueva")
    public String montarFormularioParaNuevaPregunta(Model modelo, HttpSession session) {

        modelo.addAttribute("tituloPestaña", "Crear Pregunta");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + "- Crear Pregunta");
        modelo.addAttribute("tituloFormulario", "Crear nueva Pregunta");
        modelo.addAttribute("botonSubmit", "Guardar");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pregunta-guardar");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("preguntaRecuperacion", new PreguntaRecuperacion());

        return HTML_URL_FORMULARIO_PREGUNTA;
    }

    /**
     * Metodo que permite guardar una nueva pregunta
     * 
     * @param preguntaRecuperacion
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pregunta-guardar")
    public String guardarPregunta(@Validated PreguntaRecuperacion preguntaRecuperacion, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        {
            modelo.addAttribute("tituloPestaña", "Crear Pregunta");
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Pregunta");
            modelo.addAttribute("tituloFormulario", "Crear nueva Pregunta");
            modelo.addAttribute("botonSubmit", "Guardar");
            modelo.addAttribute("returnUrl", "/administracion-seguridad");
        }
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);

        if (result.hasErrors()) {
            modelo.addAttribute("preguntaRecuperacion", preguntaRecuperacion);
            modelo.addAttribute("msgError", "Ya existe esa pregunta");
            return HTML_URL_FORMULARIO_PREGUNTA;
        }

        if (!preguntaRecuperacionService.guardarPregunta(preguntaRecuperacion)) {
            modelo.addAttribute("preguntaRecuperacion", preguntaRecuperacion);
            modelo.addAttribute("msgError", "Ya existe esa pregunta");
            return HTML_URL_FORMULARIO_PREGUNTA;
        }

        redirectAttributes.addFlashAttribute("msgExito", "La pregunta se agrego con exito");

        return "redirect:/administracion-seguridad/pregunta-ver-todo";
    }

    /**
     * Metodo que permite visualizar todas las preguntas
     * 
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/pregunta-ver-todo")
    public String listarPreguntas(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        List<PreguntaRecuperacion> preguntasRecuperacion = preguntaRecuperacionService.buscarTodasLasPreguntas();

        modelo.addAttribute("tituloPestaña", "Lista Preguntas");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Preguntas");
        modelo.addAttribute("tituloLista", "Lista de Preguntas");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }

        modelo.addAttribute("listaPreguntas", preguntasRecuperacion);

        return HTML_URL_LISTA_PREGUNTA;
    }

    /**
     * Metodo que permite buscar una pregunta por su pregunta
     * 
     * @param pregunta
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/buscar-por-pregunta")
    public String buscarPregunta(@RequestParam("pregunta") String pregunta, Model modelo, HttpSession session) {

        modelo.addAttribute("tituloPestaña", "Buscar Pregunta");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Buscar Pregunta");
        modelo.addAttribute("tituloFormulario", "Buscar Pregunta");
        modelo.addAttribute("botonSubmit", "Buscar");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        PreguntaRecuperacion preguntaRecuperacion = preguntaRecuperacionService.buscarUnPorPregunta(pregunta);
        List<PreguntaRecuperacion> preguntasRecuperacion = new ArrayList<>();

        if (preguntaRecuperacion != null) {
            preguntasRecuperacion.add(preguntaRecuperacion);
        } else {
            modelo.addAttribute("msgError", "No se encontro ninguna pregunta así");
        }
        modelo.addAttribute("listaPreguntas", preguntasRecuperacion);
        return HTML_URL_LISTA_PREGUNTA;
    }

    /**
     * Metodo que permite visualizar el formulario de actualizar una pregunta
     * 
     * @param id
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pregunta-actualizar/{id}")
    public String montarFormularioParaActualizarPregunta(@PathVariable Integer id, Model modelo, HttpSession session) {

        modelo.addAttribute("tituloPestaña", "Actualizar Pregunta");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Pregunta");
        modelo.addAttribute("tituloFormulario", "Actualizar Pregunta");
        modelo.addAttribute("botonSubmit", "Actualizar");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_SEGURIDAD);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_SEGURIDAD + "/" + id);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);

        modelo.addAttribute("preguntaRecuperacion", preguntaRecuperacionService.obtenerPreguntaPorId(id));
        return HTML_URL_FORMULARIO_PREGUNTA;
    }

    /**
     * Metodo que permite actualizar una pregunta
     * 
     * @param id
     * @param preguntaRecuperacion
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pregunta-ver-todo/{id}")
    public String actualizarPregunta(@PathVariable Integer id, @Validated PreguntaRecuperacion preguntaRecuperacion,
            BindingResult result, RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {

        modelo.addAttribute("tituloPestaña", "Actualizar Pregunta");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Pregunta");
        modelo.addAttribute("tituloFormulario", "Actualizar Pregunta");
        modelo.addAttribute("botonSubmit", "Actualizar");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_SEGURIDAD);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_SEGURIDAD + "/" + id);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        if (result.hasErrors()) {
            modelo.addAttribute("preguntaRecuperacion", preguntaRecuperacion);
            modelo.addAttribute("msgError", "Ya existe esa pregunta");
            return HTML_URL_FORMULARIO_PREGUNTA;
        }

        PreguntaRecuperacion preguntaExistente = preguntaRecuperacionService.obtenerPreguntaPorId(id);
        preguntaExistente.setId(preguntaRecuperacion.getId());
        preguntaExistente.setPregunta(preguntaRecuperacion.getPregunta());

        preguntaRecuperacionService.actualizarPregunta(preguntaExistente);

        redirectAttributes.addFlashAttribute("msgExito", "La pregunta ha sido actualizada correctamente");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_SEGURIDAD;
    }

    /**
     * Metodo que permite eliminar una pregunta
     * 
     * @param id
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/pregunta-eliminar/{id}")
    public String eliminarPregunta(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        preguntaRecuperacionService.eliminarPreguntaPorId(id);

        redirectAttributes.addAttribute("msgEliminacionExitosa", "Se elimino con exito la pregunta");

        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_SEGURIDAD;
    }

}
