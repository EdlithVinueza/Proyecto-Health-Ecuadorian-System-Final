package com.uce.edu.controller.usuarios.secretariaGeneralController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.to.BuscarCitaTO;

import jakarta.servlet.http.HttpSession;

/**
 * que permite visualizar la lista de citas agendadas
 * 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class VerCitasSecretariaController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Secretaria";
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String URL_MENU_CITA = "/menu-citas";
    private final String URL_LISTA_CITAS = "/cita-ver-todo";
    private final String HTML_URL_LISTA_CITAS_EXISTENTES = "secretariaGeneral/listas/cita_lista";
    private final String HTML_INFORMACION_CITA = "secretariaGeneral/informacion/cita_informacion";
    /**
     * Dependencias
     */
    @Autowired
    private ICitaMedicaService citaMedicaService;
    @Autowired
    private IPacienteService pacienteService;

    /**
     * Metodo que permite visualizar la lista de citas agendadas
     * 
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/cita-ver-todo")
    public String listarCitas(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas Agendadas");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Citas Agendadas");
        modelo.addAttribute("tituloLista", "Lista de Citas Agendadas");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }

        List<CitaDTO> citasDTO = citaMedicaService.todasLasCitasDelSistema();
        if (citasDTO == null || citasDTO.isEmpty()) {
            modelo.addAttribute("msgError", "No existen citas");
        }
        modelo.addAttribute("citasDTO", citasDTO);

        modelo.addAttribute("buscarCitaTO", new BuscarCitaTO());
        return HTML_URL_LISTA_CITAS_EXISTENTES;
    }

    /**
     * Metodo que permite buscar citas por cedula de paciente
     * 
     * @param cedulaPaciente
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/cita-buscar-por-cedula")
    public String buscarCitasPorCedulaPaciente(@RequestParam("cedulaPaciente") String cedulaPaciente, Model modelo,
            HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas Agendadas");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Citas Agendadas");
        modelo.addAttribute("tituloLista", "Lista de Citas Agendadas");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        if (cedulaPaciente == null || cedulaPaciente.isEmpty()) {
            modelo.addAttribute("msgError", "La cédula ingresada no es válida");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS_EXISTENTES;
        }

        Paciente paciente = pacienteService.buscarPorCedula(cedulaPaciente);

        if (paciente == null) {
            modelo.addAttribute("msgError", "No existe un paciente con la cédula ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS_EXISTENTES;
        }
        List<CitaDTO> citasDTO = citaMedicaService.buscarCitasPorCedulaPaciente(cedulaPaciente);

        if (citasDTO.isEmpty() || citasDTO == null) {
            modelo.addAttribute("msgError", "No existen citas para el paciente con la cédula ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());

        }

        session.setAttribute("todasLasCitas", citasDTO);
        modelo.addAttribute("citasDTO", citasDTO);
        modelo.addAttribute("msgExito", "Se encontraron resultados para: " + cedulaPaciente);

        return HTML_URL_LISTA_CITAS_EXISTENTES;
    }

    /**
     * Metodo que permite buscar citas por fecha
     * 
     * @param fechaCita
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("cita-buscar-por-fecha")
    public String buscarCitasPorFecha(@RequestParam("fechaCita") LocalDate fechaCita, Model modelo,
            HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas Agendadas");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Citas Agendadas");
        modelo.addAttribute("tituloLista", "Lista de Citas Agendadas");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);

        if (fechaCita == null) {
            modelo.addAttribute("msgError", "La fecha ingresada no es válida");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS_EXISTENTES;
        }

        List<CitaDTO> citasDTO = citaMedicaService.buscarCitasPorFecha(fechaCita);
        if (citasDTO.isEmpty() || citasDTO == null) {
            modelo.addAttribute("msgError", "No existen citas para la fecha ingresada");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_URL_LISTA_CITAS_EXISTENTES;
        }

        session.setAttribute("todasLasCitas", citasDTO);
        modelo.addAttribute("citasDTO", citasDTO);
        modelo.addAttribute("msgExito", "Se encontraron resultados para la fecha: " + fechaCita);

        return HTML_URL_LISTA_CITAS_EXISTENTES;
    }

    /**
     * Metodo que permite visualizar la información de una cita
     * 
     * @param id
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("ver-cita-detalles/{id}")
    public String verHistoriaEspecifica(@PathVariable Integer id, RedirectAttributes redirectAttributes, Model modelo,
            HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Ver Información de Cita");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloFormulario", "Información de Cita");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_CITAS);
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        CitaMedica cita = citaMedicaService.buscarCitaMedicaPorId(id);
        modelo.addAttribute("cita", cita);

        return HTML_INFORMACION_CITA;
    }

}
