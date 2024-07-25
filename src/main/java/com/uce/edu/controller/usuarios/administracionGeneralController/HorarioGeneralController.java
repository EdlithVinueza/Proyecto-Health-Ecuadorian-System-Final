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

import com.uce.edu.repository.modelo.Horario;
import com.uce.edu.repository.modelo.dto.DoctorDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IConsultorioService;
import com.uce.edu.service.interfacesSistemaPrincipal.IDoctorService;
import com.uce.edu.service.interfacesSistemaPrincipal.IHorarioService;
import com.uce.edu.service.to.HorarioTO;
import com.uce.edu.util.validaciones.ValidarHorasTrabajo;

import jakarta.servlet.http.HttpSession;

/**
 * Horario General Controller
 * 
 */
@Controller
@RequestMapping("administracion-general")
public class HorarioGeneralController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Administración General"; // Título de la barra de navegación
    private final String NOMBRE_NAVBAR = "nombrePasoGeneral";
    private final String URL_MENU_PRINCIPAL = "/administracion-general"; // URL del menú principal
    private final String URL_MENU_DOCTORES = "/menu-doctores"; // URL del menú de doctores
    private final String URL_LISTA_HORARIOS = "/horario-ver-todo"; // URL para ver la lista de horarios
    private final String HTML_URL_LISTA_HORARIOS = "administracionGeneral/listas/horario_lista"; // Plantilla HTML para
                                                                                                 // la lista de horarios
    private final String HTML_URL_FORMULARIO_HORARIO = "administracionGeneral/formularios/horario_formulario"; // Plantilla
                                                                                                               // HTML
                                                                                                               // para
                                                                                                               // el
                                                                                                               // formulario
                                                                                                               // de
                                                                                                               // horario

    /**
     * Dependencias
     */
    @Autowired
    private IHorarioService horarioService; // Servicio para manejar horarios

    @Autowired
    private IConsultorioService consultorioService; // Servicio para manejar consultorios

    @Autowired
    private IDoctorService doctorService; // Servicio para manejar doctores

    /**
     * Método que permite ver la lista de horarios
     * 
     * @param cedula
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/horario-ver-todo")
    public String listarHorarios(@RequestParam(name = "cedula", required = false) String cedula, Model modelo,
            @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Buscar Horario"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Buscar Horario"); // Título de la barra de navegación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Nombre del usuario en la barra de navegación
        modelo.addAttribute("tituloLista", "Buscar Horario"); // Título de la lista de horarios
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_DOCTORES); // URL para regresar al menú de
                                                                                  // doctores
        List<HorarioTO> horariosTO = new ArrayList<>();
        if (cedula != null) {
            modelo.addAttribute("cedula", cedula); // Cédula del doctor
            List<Horario> horarios = horarioService.buscarHorarioPorCedulaDoctor(cedula); // Buscar horarios por cédula
            horariosTO = horarioService.convertirListaToHorarioTO(horarios); // Convertir lista de horarios a TO
            if (horariosTO != null && !horariosTO.isEmpty()) {
                modelo.addAttribute("msgExito", "Se encontró el horario para el doctor: " + cedula); // Mensaje de éxito
            } else {
                modelo.addAttribute("msgError", "No se encontró ningún horario para el doctor: " + cedula); // Mensaje
                                                                                                            // de error
            }
        }
        modelo.addAttribute("horariosTO", horariosTO); // Lista de horarios
        return HTML_URL_LISTA_HORARIOS;
    }

    /**
     * Método que permite buscar un horario por cédula
     * 
     * @param cedula
     * @param session
     * @param modelo
     * @return
     */
    @PostMapping("/horario-buscar-por-cedula")
    public String buscarHorario(@RequestParam("cedula") String cedula, HttpSession session, Model modelo) {
        modelo.addAttribute("tituloPestaña", "Buscar Horario"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Buscar Horario"); // Título de la barra de navegación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Nombre del usuario en la barra de navegación
        modelo.addAttribute("tituloLista", "Buscar Horario"); // Título de la lista de horarios
        modelo.addAttribute("botonSubmit", "Buscar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_DOCTORES); // URL para regresar al menú de
                                                                                  // doctores
        modelo.addAttribute("cedula", cedula); // Cédula del doctor
        DoctorDTO doctor = doctorService.buscarDoctorPorCedula(cedula); // Buscar doctor por cédula

        if (doctor == null) {
            modelo.addAttribute("msgError", "No se encontró ningún doctor con la cédula: " + cedula); // Mensaje de
                                                                                                      // error
            return HTML_URL_LISTA_HORARIOS;
        }

        List<HorarioTO> horariosTO = horarioService
                .convertirListaToHorarioTO(horarioService.buscarHorarioPorCedulaDoctor(cedula)); // Convertir lista de
                                                                                                 // horarios a TO

        if (horariosTO != null && !horariosTO.isEmpty()) {
            modelo.addAttribute("msgExito", "Se encontró el horario para el doctor: " + cedula); // Mensaje de éxito
        } else {
            modelo.addAttribute("msgError", "No se encontró ningún horario para el doctor: " + cedula); // Mensaje de
                                                                                                        // error
        }
        modelo.addAttribute("horariosTO", horariosTO); // Lista de horarios
        return HTML_URL_LISTA_HORARIOS;
    }

    /**
     * Método que permite ver el formulario para agregar un nuevo horario
     * 
     * @param cedula
     * @param session
     * @param modelo
     * @return
     */
    @PostMapping("/horario-nuevo")
    public String montarFormularioParaNuevoHorario(@RequestParam("cedula") String cedula, HttpSession session,
            Model modelo) {
        modelo.addAttribute("tituloPestaña", "Nuevo Horario"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Nuevo Horario"); // Título de la barra de navegación
        modelo.addAttribute("tituloFormulario", "Nuevo Horario para " + cedula); // Título del formulario
        modelo.addAttribute("botonSubmit", "Guardar"); // Texto del botón de submit
        HorarioTO horarioTO = new HorarioTO();
        horarioTO.setCedulaDoctor(cedula); // Cédula del doctor
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "?cedula=" + cedula); // URL para
                                                                                                         // regresar a
                                                                                                         // la lista de
                                                                                                         // horarios
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/horario-guardar"); // Acción del formulario
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario); // Nombre del usuario en la barra de navegación
        modelo.addAttribute("cedula", cedula); // Cédula del doctor
        List<String> listaDias = horarioService.obtenerDiasSemana(); // Obtener días de la semana
        modelo.addAttribute("listaDias", listaDias); // Lista de días
        List<String> listaConsultorio = consultorioService.buscarCodigoConsultorio(); // Obtener códigos de consultorios
        modelo.addAttribute("listaConsultorio", listaConsultorio); // Lista de consultorios
        List<Integer> duracionesCitas = horarioService.duracionesCitas(); // Obtener duraciones de citas
        modelo.addAttribute("duracionesCitas", duracionesCitas); // Lista de duraciones de citas
        modelo.addAttribute("horarioTO", horarioTO); // Objeto HorarioTO
        modelo.addAttribute("esNuevo", true); // Indicador de nuevo horario
        return HTML_URL_FORMULARIO_HORARIO;
    }

    /**
     * Método que permite guardar un nuevo horario
     * 
     * @param horarioTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/horario-guardar")
    public String guardarHorario(@Validated HorarioTO horarioTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        List<String> listaDias = horarioService.obtenerDiasSemana(); // Obtener días de la semana
        modelo.addAttribute("listaDias", listaDias); // Lista de días
        List<String> listaConsultorio = consultorioService.buscarCodigoConsultorio(); // Obtener códigos de consultorios
        modelo.addAttribute("listaConsultorio", listaConsultorio); // Lista de consultorios
        modelo.addAttribute("tituloPestaña", "Nuevo Horario"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Nuevo Horario"); // Título de la barra de navegación
        modelo.addAttribute("tituloFormulario", "Nuevo Horario"); // Título del formulario
        modelo.addAttribute("botonSubmit", "Actualizar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl",
                URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "?cedula=" + horarioTO.getCedulaDoctor()); // URL para
                                                                                                     // regresar a la
                                                                                                     // lista de
                                                                                                     // horarios
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS); // Acción del formulario
        modelo.addAttribute("esNuevo", true); // Indicador de nuevo horario
        if (result.hasErrors()) {
            modelo.addAttribute("horarioTO", horarioTO); // Objeto HorarioTO
            modelo.addAttribute("errors", result.getAllErrors()); // Errores de validación
            return HTML_URL_FORMULARIO_HORARIO;
        }
        if (!ValidarHorasTrabajo.validarHoras(horarioTO.getHoraInicio(), horarioTO.getHoraFin())) { // Validar horas de
                                                                                                    // trabajo
            horarioTO.setHoraInicio(null);
            horarioTO.setHoraFin(null);
            modelo.addAttribute("horarioTO", horarioTO); // Objeto HorarioTO
            modelo.addAttribute("mensajeError", "La hora de fin debe ser mayor que la hora de inicio."); // Mensaje de
                                                                                                         // error
            return HTML_URL_FORMULARIO_HORARIO;
        }
        String cedula = horarioTO.getCedulaDoctor();
        horarioService.guardarNuevoHorario(horarioTO); // Guardar nuevo horario
        redirectAttributes.addFlashAttribute("msgExito", "El horario ha sido guardado correctamente"); // Mensaje de
                                                                                                       // éxito

        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "?cedula=" + cedula;
    }

    /**
     * Método que permite ver el formulario para actualizar un horario
     * 
     * @param id
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/horario-actualizar/{id}")
    public String montarFormularioParaActualizarHorario(@PathVariable Integer id, Model modelo,
            HttpSession session) {
        List<String> listaDias = horarioService.obtenerDiasSemana(); // Obtener días de la semana
        modelo.addAttribute("listaDias", listaDias); // Lista de días
        List<String> listaConsultorio = consultorioService.buscarCodigoConsultorio(); // Obtener códigos de consultorios
        modelo.addAttribute("listaConsultorio", listaConsultorio); // Lista de consultorios
        List<Integer> duracionesCitas = horarioService.duracionesCitas(); // Obtener duraciones de citas
        modelo.addAttribute("duracionesCitas", duracionesCitas); // Lista de duraciones de citas
        modelo.addAttribute("tituloPestaña", "Actualizar Horario " + id); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Horario " + id); // Título de la barra de
                                                                                            // navegación
        modelo.addAttribute("tituloFormulario", "Actualizar Horario " + id); // Título del formulario
        modelo.addAttribute("botonSubmit", "Actualizar"); // Texto del botón de submit
        HorarioTO horarioTO = horarioService.buscarHorarioPorId(id); // Buscar horario por ID
        modelo.addAttribute("returnUrl",
                URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "?cedula=" + horarioTO.getCedulaDoctor()); // URL para
                                                                                                     // regresar a la
                                                                                                     // lista de
                                                                                                     // horarios
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "/" + id); // Acción del formulario
        modelo.addAttribute("horarioTO", horarioTO); // Objeto HorarioTO
        return HTML_URL_FORMULARIO_HORARIO;
    }

    /**
     * Método que permite actualizar un horario
     * 
     * @param id
     * @param horarioTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/horario-ver-todo/{id}")
    public String actualizarHorario(@PathVariable Integer id, @Validated HorarioTO horarioTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        List<String> listaDias = horarioService.obtenerDiasSemana(); // Obtener días de la semana
        modelo.addAttribute("listaDias", listaDias); // Lista de días
        List<String> listaConsultorio = consultorioService.buscarCodigoConsultorio(); // Obtener códigos de consultorios
        modelo.addAttribute("listaConsultorio", listaConsultorio); // Lista de consultorios
        List<Integer> duracionesCitas = horarioService.duracionesCitas(); // Obtener duraciones de citas
        modelo.addAttribute("duracionesCitas", duracionesCitas); // Lista de duraciones de citas

        modelo.addAttribute("tituloPestaña", "Actualizar Horario " + id); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Horario " + id); // Título de la barra de
                                                                                            // navegación
        modelo.addAttribute("tituloFormulario", "Actualizar Horario " + id); // Título del formulario
        modelo.addAttribute("botonSubmit", "Actualizar"); // Texto del botón de submit
        modelo.addAttribute("returnUrl",
                URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "?cedula=" + horarioTO.getCedulaDoctor()); // URL para
                                                                                                     // regresar a la
                                                                                                     // lista de
                                                                                                     // horarios
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "/" + id); // Acción del formulario

        if (result.hasErrors()) {
            modelo.addAttribute("horarioTO", horarioTO); // Objeto HorarioTO
            modelo.addAttribute("errors", result.getAllErrors()); // Errores de validación
            return HTML_URL_FORMULARIO_HORARIO;
        }
        if (!ValidarHorasTrabajo.validarHoras(horarioTO.getHoraInicio(), horarioTO.getHoraFin())) { // Validar horas de
                                                                                                    // trabajo
            horarioTO.setHoraInicio(null);
            horarioTO.setHoraFin(null);
            modelo.addAttribute("horarioTO", horarioTO); // Objeto HorarioTO
            modelo.addAttribute("mensajeErrorHoras", "La hora de fin debe ser mayor que la hora de inicio."); // Mensaje
                                                                                                              // de
                                                                                                              // error
            return HTML_URL_FORMULARIO_HORARIO;
        }
        horarioService.guardarHorario(horarioTO); // Guardar horario
        redirectAttributes.addFlashAttribute("cedula", horarioTO.getCedulaDoctor()); // Cédula del doctor
        redirectAttributes.addFlashAttribute("msgExito", "El Usuario ha sido actualizado correctamente"); // Mensaje de
                                                                                                          // éxito

        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_HORARIOS + "?cedula=" + horarioTO.getCedulaDoctor();
    }
}
