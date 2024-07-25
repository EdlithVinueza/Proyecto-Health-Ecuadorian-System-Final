package com.uce.edu.controller.doctores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uce.edu.repository.modelo.HistoriaClinica;
import com.uce.edu.repository.modelo.dto.CitaDTO;
import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IHistoriaClinicaService;
import com.uce.edu.service.to.DatosHistoriaClinicaTO;

import jakarta.servlet.http.HttpSession;
/**
 * MenuDoctoresControlle
 */
@Controller
@RequestMapping("doctores")
public class MenuDoctoresControlle {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Doctores";
    private final String NOMBRE_NAVBAR = "nombrePasoDoctor";
    private final String CEDULA_DOCTOR_SESSION = "cedulaPasoDoctor";
    private final String HTML_MENU_DOCTORES =  "doctores/menus/doctor_menu_principal";
    private final String HTML_LISTA_CITAS = "doctores/listas/citas_por_atender_lista";
    private final String HTML_LISTA_HORARIO =  "doctores/listas/horario_doctor_lista";
    private final String HTML_LISTA_HISTORIAS = "doctores/listas/historias_lista";

    /**
     * Dependencias
     */
    @Autowired
    private ICitaMedicaService citaMedicaService;

     @Autowired
    private IHistoriaClinicaService historiaClinicaService;
    
    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("")
    public String verMenuDoctores(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas por atender");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR);
        modelo.addAttribute("tituloLista", "Tus citas para atender");
        modelo.addAttribute("returnUrl", "/login-doctores");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        session.setAttribute("cedulaPaciente", null);
        return HTML_MENU_DOCTORES;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/atender-citas")
    public String verMenuSecretariaPacientes(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Lista Citas por atender");
        modelo.addAttribute("tituloNavbar", "Doctores");
        modelo.addAttribute("tituloLista", "Tus citas para atender");
        modelo.addAttribute("returnUrl", "/doctores");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        String tmpCedula = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);
        List<CitaDTO> citasDTO = citaMedicaService.buscarCitasPorCedulaDoctorParaDoctor(tmpCedula);
        session.setAttribute("todasLasCitas", citasDTO);  
        if (citasDTO.isEmpty() || citasDTO == null) {
            modelo.addAttribute("msgError", "No existen citas para atender");
            modelo.addAttribute("citasDTO", new ArrayList<CitaDTO>());
            return HTML_MENU_DOCTORES;
        }
        modelo.addAttribute("citasDTO", citasDTO);
        
        session.setAttribute("todasHistorias", citasDTO);
        return HTML_LISTA_CITAS;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/ver-horario")
    public String verMenuSecretariaCitas(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Doctor - Mi horario");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", "/doctores");
        
        return HTML_LISTA_HORARIO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param session
     * @return
     */

    @GetMapping("/ver-historias")
    public String verMenuSecretariaOrdenCobro(Model modelo, HttpSession session) {
        modelo.addAttribute("tituloNavbar", "Doctor - Ver historias");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("returnUrl", "/doctores");

        String cedulaPaciente = (String) session.getAttribute("cedulaPaciente");
        
        List<HistoriaClinica> historiasClinicas = historiaClinicaService.buscarPorCedulaPaciente(cedulaPaciente);

        if (historiasClinicas.isEmpty() || historiasClinicas == null) {
            modelo.addAttribute("msgError", "Por favor ingresa un numero de cédula para ver historia clinica");
            modelo.addAttribute("citasDTO", new ArrayList<PacienteDTO>());
            return HTML_LISTA_HISTORIAS;
        }

        List<DatosHistoriaClinicaTO> historias = historiaClinicaService
                .convertirHistoriasClinicasADatosHistoriasClinicasTO(historiasClinicas);
        modelo.addAttribute("historias", historias);
        session.setAttribute("todasHistorias", historias);
        return HTML_LISTA_HISTORIAS;
    }


}
