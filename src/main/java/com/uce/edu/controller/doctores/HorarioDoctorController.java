package com.uce.edu.controller.doctores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.uce.edu.repository.modelo.Horario;
import com.uce.edu.service.interfacesSistemaPrincipal.IHorarioService;
import com.uce.edu.service.to.HorarioTO;
import jakarta.servlet.http.HttpSession;
/**
 * Clase controladora que maneja las peticiones de los horarios de los doctores
 */
@Controller
@RequestMapping("doctores")
public class HorarioDoctorController {

    /*
     * Atributos de la clase AtenderCitasDoctorController
     */
    private final String TITULO_NAVBAR = "Doctores"; // Título de la barra de navegación
    private final String NOMBRE_NAVBAR_DOCTORES = "nombreDoctor";
    /*
     * Nombres de atributos de session
     */
    private final String CEDULA_DOCTOR_SESSION = "cedulaDoctor";// Nombre del usuario en la barra de navegación
    /**
     * URL de la lista de horarios
     */
    private final String URL_MENU_PRINCIPAL = "/doctores";// URL del menú de doctores
    /**
     * URL de la lista de horarios
     */
    private final String HTML_URL_LISTA_HORARIOS = "doctores/listas/horario_lista"; // Plantilla HTML para
                                                                                                 // la lista de horarios
    @Autowired
    private IHorarioService horarioService;

    /**
     * Método que lista los horarios de un doctor
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/horario-ver-todo")
    public String listarHorarios(Model modelo,
            @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        modelo.addAttribute("tituloPestaña", "Tu Horario"); // Título de la pestaña
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Tu Horario"); // Título de la barra de navegación
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR_DOCTORES);
        modelo.addAttribute(NOMBRE_NAVBAR_DOCTORES, nombreUsuario); // Nombre del usuario en la barra de navegación
        modelo.addAttribute("tituloLista", "Tu Horario"); // Título de la lista de horarios
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL); // URL para regresar al menú de
        List<HorarioTO> horariosTO = new ArrayList<>();                                                       // doctores
        String cedula = (String) session.getAttribute(CEDULA_DOCTOR_SESSION);
        if (cedula!=null) {
            List<Horario> horarios = horarioService.buscarHorarioPorCedulaDoctor(cedula); // Buscar horarios por cédula
            horariosTO = horarioService.convertirListaToHorarioTO(horarios);
            modelo.addAttribute("msgExito", "Este es tu horario");
            modelo.addAttribute("esDoctor", false);     
        }
        else{
            modelo.addAttribute("msgError", "No tienes horario");
        }

        modelo.addAttribute("horariosTO", horariosTO);
        return HTML_URL_LISTA_HORARIOS;
    }

}
