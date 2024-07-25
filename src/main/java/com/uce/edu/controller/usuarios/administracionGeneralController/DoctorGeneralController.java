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

import com.uce.edu.repository.modelo.dto.DoctorDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IDoctorService;
import com.uce.edu.service.interfacesSistemaPrincipal.IEspecialidadService;
import com.uce.edu.service.interfacesSistemaPrincipal.IPreguntaRecuperacionService;
import com.uce.edu.service.to.DoctorTO;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.util.validaciones.ValidadorCedula;

import jakarta.servlet.http.HttpSession;

/**
 * Doctor General Controller
 * 
 */
@Controller
@RequestMapping("administracion-general")
public class DoctorGeneralController {
    /**
     * Información de los session y url de formularios html
     */
    private static final String TITULO_NAVBAR = "Administración General";
    private final String NOMBRE_NAVBAR = "nombrePasoGeneral";
    private static final String URL_MENU_PRINCIPAL = "/administracion-general";
    private static final String URL_MENU_DOCTORES = "/menu-doctores";
    private static final String URL_LISTA_DOCTORES = "/doctor-ver-todo";
    private static final String HTML_URL_LISTA_DOCTORES = "administracionGeneral/listas/doctor_lista";
    private static final String HTML_URL_FORMULARIO_DOCTOR = "administracionGeneral/formularios/doctor_formulario";

    /**
     * Dependencias
     */
    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private IPreguntaRecuperacionService preguntaRecuperacionService;

    @SuppressWarnings("unused")
    @Autowired
    private IEspecialidadService especialidadService;

    private final List<String> todasLasEspecialidades;

    public DoctorGeneralController(IEspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
        this.todasLasEspecialidades = especialidadService.buscarNombresEspecialidades();
    }

    /**
     * Método que permite visualizar el formulario para crear un nuevo doctor
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/doctor-nuevo")
    public String montarFormularioParaNuevoDoctor(Model modelo, HttpSession session) {
        // Obtener preguntas de recuperación
        PreguntasTO preguntasTO = preguntaRecuperacionService.obtenerTresPreguntasAleatorias();
        session.setAttribute("preguntasTO", preguntasTO);

        // Configuración de atributos del modelo
        modelo.addAttribute("tituloPestaña", "Crear Doctor"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Doctor"); // Título de la barra de navegación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Nombre del usuario en la barra de navegación
        modelo.addAttribute("tituloFormulario", "Crear un nuevo Doctor"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Guardar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_DOCTORES); // URL para regresar al menú de
                                                                                  // doctores
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/doctor-guardar"); // URL para guardar el doctor
        modelo.addAttribute("listaEspecialidades", todasLasEspecialidades); // Lista de especialidades
        modelo.addAttribute("preguntaUno", preguntasTO.getPreguntaUno()); // Primera pregunta de recuperación
        modelo.addAttribute("preguntaDos", preguntasTO.getPreguntaDos()); // Segunda pregunta de recuperación
        modelo.addAttribute("preguntaTres", preguntasTO.getPreguntaTres()); // Tercera pregunta de recuperación
        modelo.addAttribute("doctorTO", new DoctorTO()); // Objeto DoctorTO para el formulario
        modelo.addAttribute("esNuevo", true); // Indicador de que es un nuevo doctor

        return HTML_URL_FORMULARIO_DOCTOR;
    }

    /**
     * Método que permite guardar un nuevo doctor
     * 
     * @param doctorTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/doctor-guardar")
    public String guardarDoctor(@Validated @ModelAttribute("doctorTO") DoctorTO doctorTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {

        // Obtener preguntas de recuperación
        PreguntasTO preguntasTO = (PreguntasTO) session.getAttribute("preguntasTO");
        if (preguntasTO == null) {
            preguntasTO = preguntaRecuperacionService.obtenerTresPreguntasAleatorias();
            session.setAttribute("preguntasTO", preguntasTO);
        }

        // Configuración de atributos del modelo
        modelo.addAttribute("tituloPestaña", "Crear Doctor"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Doctor"); // Título de la barra de navegación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Nombre del usuario en la barra de navegación
        modelo.addAttribute("tituloFormulario", "Crear un nuevo Doctor"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Guardar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_DOCTORES); // URL para regresar al menú de
                                                                                  // doctores
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/doctor-guardar"); // URL para guardar el doctor
        modelo.addAttribute("listaEspecialidades", todasLasEspecialidades); // Lista de especialidades
        modelo.addAttribute("preguntaUno", preguntasTO.getPreguntaUno()); // Primera pregunta de recuperación
        modelo.addAttribute("preguntaDos", preguntasTO.getPreguntaDos()); // Segunda pregunta de recuperación
        modelo.addAttribute("preguntaTres", preguntasTO.getPreguntaTres()); // Tercera pregunta de recuperación
        modelo.addAttribute("esNuevo", true); // Indicador de que es un nuevo doctor

        // Validación de errores
        if (result.hasErrors()) {
            modelo.addAttribute("doctorTO", doctorTO); // Objeto DoctorTO con errores
            modelo.addAttribute("errors", result.getAllErrors()); // Lista de errores
            return HTML_URL_FORMULARIO_DOCTOR;
        }

        // Validación de cédula
        if (!ValidadorCedula.validarCedula(doctorTO.getCedula())) {
            doctorTO.setCedula("");
            modelo.addAttribute("doctorTO", doctorTO); // Objeto DoctorTO con cédula vacía
            modelo.addAttribute("mensajeErrorCedula", "Ingrese una cédula válida"); // Mensaje de error de cédula
            return HTML_URL_FORMULARIO_DOCTOR;
        }

        // Guardar el nuevo doctor
        doctorService.guardarDoctorNuevo(doctorTO, preguntasTO);
        redirectAttributes.addFlashAttribute("msgExito", "El Doctor ha sido agregado con éxito"); // Mensaje de éxito

        return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_DOCTORES;
    }

    /**
     * Método que permite actualizar la información de un doctor
     * 
     * @param doctorTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */

    /**
     * Método que permite listar todos los doctores
     * 
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/doctor-ver-todo")
    public String listarDoctores(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        // Obtener lista de doctores
        List<DoctorDTO> doctoresDTO = doctorService.listarDoctores();
        // Configuración de atributos del modelo
        modelo.addAttribute("tituloPestaña", "Lista Doctores");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Doctores");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloLista", "Lista de Doctores");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_DOCTORES);
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }
        modelo.addAttribute("doctoresDTO", doctoresDTO);

        return HTML_URL_LISTA_DOCTORES;
    }

    /**
     * Método que permite buscar un doctor por cédula
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/doctor-buscar-por-cedula")
    public String buscarDoctor(@RequestParam("cedula") String cedula, Model modelo, HttpSession session) {

        // Configuración de atributos del modelo
        modelo.addAttribute("tituloPestaña", "Lista Doctores");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Doctores");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloLista", "Lista de Doctores");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_DOCTORES);

        // Buscar doctor por cédula
        List<DoctorDTO> doctoresDTO = new ArrayList<>();
        DoctorDTO doctorDTO = doctorService.buscarDoctorPorCedula(cedula);
        if (doctorDTO != null) {
            doctoresDTO.add(doctorDTO);
        } else {
            modelo.addAttribute("msgError", "No se encontró ningún doctor con la cédula ingresada");
        }
        modelo.addAttribute("doctoresDTO", doctoresDTO); // Lista de doctores
        return HTML_URL_LISTA_DOCTORES;
    }

    /**
     * Metodo que permite ver el formulario para actualziar un doctor
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/doctor-actualizar/{cedula}")
    public String montarFormularioParaActualizarDoctor(@PathVariable String cedula, Model modelo,
            HttpSession session) {
        // Configuración de atributos del modelo
        List<String> especialidades = especialidadService.buscarNombresEspecialidades();
        modelo.addAttribute("listaEspecialidades", especialidades);
        modelo.addAttribute("tituloPestaña", "Actualizar Doctor");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Doctor");
        modelo.addAttribute("tituloFormulario", "Actualizar  Doctor");
        modelo.addAttribute("botonSubmit", "Actualizar");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_DOCTORES);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_DOCTORES + "/" + cedula);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        DoctorDTO tmp = doctorService.buscarDoctorPorCedula(cedula);
        DoctorTO doctorTO = doctorService.convertirDoctorDTOADoctorTO(tmp);
        modelo.addAttribute("doctorTO", doctorTO);
        return HTML_URL_FORMULARIO_DOCTOR;
    }

    /**
     * Metodo que permite actualizar un doctor
     * 
     * @param cedula
     * @param doctorTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/doctor-ver-todo/{cedula}")
    public String actualizarDoctor(@PathVariable String cedula, @Validated DoctorTO doctorTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {

        modelo.addAttribute("listaEspecialidades", todasLasEspecialidades);
        modelo.addAttribute("tituloPestaña", "Actualizar Doctor");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Doctor");
        modelo.addAttribute("tituloFormulario", "Actualizar  Doctor");
        modelo.addAttribute("botonSubmit", "Actualizar");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_DOCTORES);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_DOCTORES + "/" + cedula);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);

        if (result.hasErrors()) {
            modelo.addAttribute("doctorTO", doctorTO);
            return HTML_URL_FORMULARIO_DOCTOR;

        }
        if (ValidadorCedula.validarCedula(doctorTO.getCedula()) == false) {
            modelo.addAttribute("msgError", "La cédula ingresada no es válida");
            doctorTO.setCedula("");
            modelo.addAttribute("doctorTO", doctorTO);
            return HTML_URL_FORMULARIO_DOCTOR;
        }

        doctorTO.setCedula(cedula);
        doctorService.actualizarDoctor(doctorTO);
        redirectAttributes.addFlashAttribute("msgExito", "El Doctor ha sido actualizado correctamente");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_DOCTORES;
    }

}
