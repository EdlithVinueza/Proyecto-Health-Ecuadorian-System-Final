package com.uce.edu.controller.doctores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.HistoriaClinica;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IHistoriaClinicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.to.DatosHistoriaClinicaTO;
import com.uce.edu.service.to.HistoriaClinicaTO;

import jakarta.servlet.http.HttpSession;

/**
 * Ver Historia Clinica para los Doctores
 */
@Controller
@RequestMapping("doctores")
public class VerHistoriaClinicaDoctorController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Doctores";
    private final String NOMBRE_NAVBAR_DOCTORES = "nombreDoctor";
    private final String CEDULA_DOCTOR_SESSION = "cedulaDoctor";
    private final String URL_MENU_PRINCIPAL = "/doctores";
    private final String HTML_INFORMACION_HISTORIA_CLINICA = "doctores/informacion/historia_clinica_informacion";
    private final String HTML_LISSTA_HISTORIA_CLINICA = "doctores/listas/historias_lista";

    /**
     * Dependencias
     */
    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private IHistoriaClinicaService historiaClinicaService;

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param cedulaPaciente
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("historia-buscar-por-cedula")
    public String buscarHistoriasPorCedulaPaciente(@RequestParam("cedulaPaciente") String cedulaPaciente, Model modelo,
            HttpSession session) {
        session.setAttribute("cedulaPaciente", cedulaPaciente);
        modelo.addAttribute("tituloPestaña", "Lista Historia Clinica por paciente");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloLista", "Historias Clínicas por paciente");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
            modelo.addAttribute("msgError", "Por favor ingresa un numero de cédula");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_LISSTA_HISTORIA_CLINICA;
        }
        Paciente paciente = pacienteService.buscarPorCedula(cedulaPaciente);
        if (paciente == null) {
            modelo.addAttribute("msgError", "No existe un paciente con la cédula ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_LISSTA_HISTORIA_CLINICA;
        }
        List<HistoriaClinica> historiasClinicas = historiaClinicaService.buscarPorCedulaPaciente(cedulaPaciente);

        if (historiasClinicas.isEmpty() || historiasClinicas == null) {
            modelo.addAttribute("msgError", "El paciente no tiene historia clinica");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_LISSTA_HISTORIA_CLINICA;
        }

        List<DatosHistoriaClinicaTO> historias = historiaClinicaService
                .convertirHistoriasClinicasADatosHistoriasClinicasTO(historiasClinicas);
        modelo.addAttribute("historias", historias);
        session.setAttribute("todasHistorias", historias);
        modelo.addAttribute("msgExito", "Se encontraron resultados para: " + cedulaPaciente);
        return HTML_LISSTA_HISTORIA_CLINICA;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param id
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("ver-hitorial-especifico/{id}")
    public String verHistoriaEspecifica(@PathVariable Integer id, RedirectAttributes redirectAttributes, Model modelo,
            HttpSession session) {

        modelo.addAttribute("tituloPestaña", "Ver Historial Clínico");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloFormulario", "Historia Clínica ");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + "/ver-historias");
        HistoriaClinica historia = historiaClinicaService.buscarPorId(id);
        DatosHistoriaClinicaTO h = historiaClinicaService
                .convertirHistoriaClinicaADatosHistoriaClinicaTO(historia);
        modelo.addAttribute("h", h);

        String nombreDoctor = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreDoctor);
        String cedulaDoctor = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);
        HistoriaClinicaTO historiaClinicaTO = new HistoriaClinicaTO();
        historiaClinicaTO.setCedulaDoctor(cedulaDoctor);

        return HTML_INFORMACION_HISTORIA_CLINICA;
    }

}
