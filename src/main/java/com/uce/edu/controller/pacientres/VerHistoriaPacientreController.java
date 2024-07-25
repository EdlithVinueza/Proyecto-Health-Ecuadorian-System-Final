package com.uce.edu.controller.pacientres;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.HistoriaClinica;
import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IHistoriaClinicaService;
import com.uce.edu.service.to.DatosHistoriaClinicaTO;
import jakarta.servlet.http.HttpSession;
/**
 * Ver Historia Paciente Controller
 */
@Controller
@RequestMapping("pacientes")
public class VerHistoriaPacientreController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Pacientes"; // Título de la barra de navegación
    private final String NOMBRE_NAVBAR = "nombrePasoPaciente";
    private final String CEDULA_PACIENTE = "cedulaPasoPaciente";// Nombre del usuario en la barra de navegación
    private final String URL_MENU_PRINCIPAL = "/pacientes"; // URL del menú principal
    private final String HTML_INFORMACION_HISTORIA_CLINICA = "pacientes/informacion/historia_clinica_informacion";
    private final String HTML_LISTA_HISTORIAS = "pacientes/listas/historias_lista";
   
/**
 * Dependencias
 */

    @Autowired
    private IHistoriaClinicaService historiaClinicaService;

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/ver-historias")
    public String verMenuSecretariaOrdenCobro(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR+" - Ver historias");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);

        String cedulaPaciente = (String) session.getAttribute(CEDULA_PACIENTE);
        
        List<HistoriaClinica> historiasClinicas = historiaClinicaService.buscarPorCedulaPaciente(cedulaPaciente);

        if (historiasClinicas.isEmpty() || historiasClinicas == null) {
            modelo.addAttribute("msgError", "No tienes historias clínicas");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_LISTA_HISTORIAS;
        }

        List<DatosHistoriaClinicaTO> historias = historiaClinicaService
                .convertirHistoriasClinicasADatosHistoriasClinicasTO(historiasClinicas);
        modelo.addAttribute("historias", historias);
        session.setAttribute("todasHistorias", historias);
        return HTML_LISTA_HISTORIAS;
    }

  
/**
 * Ver Historia Especifica
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

        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        return HTML_INFORMACION_HISTORIA_CLINICA;
    }

}

