package com.uce.edu.controller.doctores;

import java.time.LocalDate;
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

import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.dto.CitaDTO;
import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IHistoriaClinicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.to.HistoriaClinicaTO;

import jakarta.servlet.http.HttpSession;

/**
 * Clase que permite atender las citas de los doctores
 */
@Controller
@RequestMapping("doctores")
public class AtenderCitasDoctorController {

    /*
     * Atributos de la clase AtenderCitasDoctorController
     */
    private final String TITULO_NAVBAR = "Doctores"; // Título de la barra de navegación
    /*
     * Nombres de atributos de session
     */
    private final String NOMBRE_NAVBAR_DOCTORES = "nombreDoctor";
    private final String CEDULA_DOCTOR_SESSION = "cedulaDoctor";// Nombre del usuario en la barra de navegación
    /**
     * Url Principales
     */
    private final String URL_MENU_PRINCIPAL = "/doctores"; // URL del menú principal
    private final String URL_LISTA_CITAS = "/citas-ver-todo"; // URL para ver la lista de horarios
    private final String URL_MENU_CITAS = "doctores/menus/doctor_menu_principal";
    /**
     * Direcciones de los archivos HTML
     */
    private final String HTML_URL_FORMULARIO_HISTORIA_CLINICA = "doctores/formularios/historia_clinica_formulario";
    private final String HTML_URL_LISTA_CITAS = "doctores/listas/citas_por_atender_lista";

    /*
     * Servicios de la clase AtenderCitasDoctorController
     */
    @Autowired
    private IPacienteService pacienteService;
    @Autowired
    private ICitaMedicaService citaMedicaService;
    @Autowired
    private IHistoriaClinicaService historiaClinicaService;

    /**
     * Método que permite buscar las citas de un paciente por su cédula
     * @param cedulaPaciente
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("cita-buscar-por-cedula")
    public String buscarCitasPorCedulaPaciente(@RequestParam("cedulaPaciente") String cedulaPaciente, Model modelo,
            HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas por atender");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloLista", "Tus citas para atender");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        String cedulaDoctor = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);

        if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
            modelo.addAttribute("msgError", "La cédula ingresada no es válida");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS;
        }

        Paciente paciente = pacienteService.buscarPorCedula(cedulaPaciente);

        if (paciente == null) {
            modelo.addAttribute("msgError", "No existe un paciente con la cédula ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS;
        }
        List<CitaDTO> citasDTO = citaMedicaService.buscarCitasParaCoctorPorCedulaPaciente(cedulaPaciente, cedulaDoctor);

        if (citasDTO.isEmpty() || citasDTO == null) {
            modelo.addAttribute("msgError", "No existen citas para el paciente con la cédula ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS;
        }

        session.setAttribute("todasLasCitas", citasDTO);
        modelo.addAttribute("citasDTO", citasDTO);
        modelo.addAttribute("msgExito", "Se encontraron resultados para: " + cedulaPaciente);

        return HTML_URL_LISTA_CITAS;
    }

    /**
     * Método que permite buscar las citas de un paciente por su fecha
     * @param fechaCita
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("cita-buscar-por-fecha")
    public String buscarCitasPorFecha(@RequestParam("fechaCita") LocalDate fechaCita, Model modelo,
            HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas por atender");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloLista", "Tus citas para atender");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        String cedulaDoctor = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);

        if (fechaCita == null) {
            modelo.addAttribute("msgError", "La fecha ingresada no es válida");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS;
        }

        List<CitaDTO> citasDTO = citaMedicaService.buscarCitasPorFechaYCedulaDoctor(fechaCita.atStartOfDay(),
                cedulaDoctor);
        if (citasDTO.isEmpty() || citasDTO == null) {
            modelo.addAttribute("msgError", "No existen citas para la fecha ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS;
        }

        session.setAttribute("todasLasCitas", citasDTO);
        modelo.addAttribute("citasDTO", citasDTO);
        modelo.addAttribute("msgExito", "Se encontraron resultados para la fecha: " + fechaCita);

        return HTML_URL_LISTA_CITAS;
    }

    /**
     * Método que permite ver todas las citas de un doctor
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/citas-ver-todo")
    public String verMenuSecretariaPacientes(Model modelo,@ModelAttribute("msgExito") String msgExito, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas por atender");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloLista", "Tus citas para atender");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        String cedulaDoctor = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);

        if (modelo.containsAttribute("msgError")) {
            String msgError = (String) modelo.getAttribute("msgError");
            modelo.addAttribute("msgError", msgError);
        }
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }
        List<CitaDTO> citasDTO = citaMedicaService.buscarCitasPorCedulaDoctorParaDoctor(cedulaDoctor);
        if (citasDTO.isEmpty() || citasDTO == null) {
            modelo.addAttribute("msgError", "No existen citas para atender");
            modelo.addAttribute("citasDTO", new ArrayList<CitaDTO>());
            return URL_MENU_CITAS;
        }

        session.setAttribute("todasLasCitas", citasDTO);
        modelo.addAttribute("citasDTO", citasDTO);
        modelo.addAttribute("msgExito", "Se encontraron resultados");
        return HTML_URL_LISTA_CITAS;
    }

    /**
     * Método que permite atender una cita
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/atender-cita/{codigo}")
    public String atenderCita(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Atender Cita");
        modelo.addAttribute("tituloNavbar", NOMBRE_NAVBAR_DOCTORES);
        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        String cedulaDoctor = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);
        modelo.addAttribute("tituloFormulario", "Cita: " + codigo);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        modelo.addAttribute("botonSubmit", "Finalizar cita");
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/finalizar-cita" + "/" + codigo);

        HistoriaClinicaTO historiaClinicaTO = new HistoriaClinicaTO();
        session.setAttribute("codigoTmp", codigo);
        historiaClinicaTO.setCodigoCita(codigo);
        historiaClinicaTO.setCedulaDoctor(cedulaDoctor);
        modelo.addAttribute("historiaClinicaTO", historiaClinicaTO);
        return HTML_URL_FORMULARIO_HISTORIA_CLINICA;
    }

    /**
     * Método que permite finalizar una cita
     * @param historiaClinicaTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/finalizar-cita/{codigo}")
    public String finalizarCita(@Validated HistoriaClinicaTO historiaClinicaTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        String codigo = (String) session.getAttribute("codigoTmp");
        modelo.addAttribute("tituloPestaña", "Atender Cita");
        modelo.addAttribute("tituloNavbar", NOMBRE_NAVBAR_DOCTORES);
        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        String cedulaDoctor = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);
        modelo.addAttribute("tituloFormulario", "Cita: " + codigo);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        modelo.addAttribute("botonSubmit", "Finalizar cita");
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/finalizar-cita" + "/" + codigo);

        if (result.hasErrors()) {

            modelo.addAttribute("historiaClinicaTO", historiaClinicaTO);

            return HTML_URL_FORMULARIO_HISTORIA_CLINICA;
        }

        CitaMedica citaMedica = citaMedicaService.buscarCitaMedicaPorCodigo(codigo);

        if (citaMedica==null||citaMedica.getEstado().equals("CANCELADA")) {
            modelo.addAttribute("msgError", "La cita no existe");
            return HTML_URL_FORMULARIO_HISTORIA_CLINICA;
        }

        historiaClinicaTO.setCedulaDoctor(cedulaDoctor);
        historiaClinicaTO.setCodigoCita(codigo);
        historiaClinicaService.guardarHistoriaClinica(historiaClinicaTO);
        redirectAttributes.addFlashAttribute("msgExito", "La cita ha sido finalizada exitosamente");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_CITAS;
    }

    /**
     * Método que permite cancelar una cita
     */
    @GetMapping("/cancelar-cita/{codigo}")
    public String cancelarCita(@PathVariable String codigo, RedirectAttributes redirectAttributes, Model modelo,
            HttpSession session) {
        citaMedicaService.cancelarClinica(codigo);
        redirectAttributes.addFlashAttribute("msgExito", "La cita ha sido cancelada exitosamente");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA_CITAS;

    }

}
