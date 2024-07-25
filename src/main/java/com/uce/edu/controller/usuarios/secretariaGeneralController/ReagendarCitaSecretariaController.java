package com.uce.edu.controller.usuarios.secretariaGeneralController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.ActualizarCitaTO;

import jakarta.servlet.http.HttpSession;

/**
 * ReagendarCitaSecretariaController
 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class ReagendarCitaSecretariaController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Secretaria";
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String CEDULA_SECRE_SESSION = "cedulaPasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String URL_MENU_CITA = "/menu-citas";
    private final String HTML_URL_FORMULARIO_REAGENDAR_CITA = "secretariaGeneral/formularios/reagendar_cita_formulario";
    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private ICitaMedicaService citaMedicaService;

    /**
     * Metodo que permite visualizar el formulario de reagendar cita
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("cita-reagendar")
    public String listarReagendarCita(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {

        modelo.addAttribute("tituloPestaña", "Reagendar Cita");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Reagendar Cita");
        modelo.addAttribute("tituloFormulario", "Reagendar Cita");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
        modelo.addAttribute("botonSubmit", "Reagendar Cita");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/cita-reagendar-guardar-cambios");
        if (msgExito != null) {
            modelo.addAttribute("msgExito", msgExito);
        }

        modelo.addAttribute("actualizarCitaTO", new ActualizarCitaTO());
        return HTML_URL_FORMULARIO_REAGENDAR_CITA;
    }

    /**
     * Metodo que permite reagendar una cita
     * @param actualizarCitaTO
     * @param result
     * @param modelo
     * @param session
     * @param redirectAttributes
     * @return
     */
    @PostMapping("cita-reagendar-guardar-cambios")
    public String actualizarCitaMedica(@Validated ActualizarCitaTO actualizarCitaTO, BindingResult result, Model modelo,
            HttpSession session, RedirectAttributes redirectAttributes) {
        modelo.addAttribute("tituloPestaña", "Reagendar Cita");
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Reagendar Cita");
        modelo.addAttribute("tituloFormulario", "Reagendar Cita");
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
        modelo.addAttribute("botonSubmit", "Reagendar Cita");
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/cita-reagendar-guardar-cambios");

        if (result.hasErrors()) {

            modelo.addAttribute("actualizarCitaTO", actualizarCitaTO);
            return HTML_URL_FORMULARIO_REAGENDAR_CITA;
        }
    
        if (!citaMedicaService.existeCitaMedicaPorCodigo(actualizarCitaTO.getCodigoCita())) {
            modelo.addAttribute("msgError", "No existe una cita con el código ingresado");
            modelo.addAttribute("actualizarCitaTO", actualizarCitaTO);
            return HTML_URL_FORMULARIO_REAGENDAR_CITA;
        }
        String cedulaSecretaria = (String) session.getAttribute(CEDULA_SECRE_SESSION);

        Usuario usuario = usuarioService.buscarPorCedula(cedulaSecretaria);
       
        if (usuario == null) {
            modelo.addAttribute("msgError", "No existe un usuario con la cédula ingresada");
            modelo.addAttribute("actualizarCitaTO", actualizarCitaTO);
            return HTML_URL_FORMULARIO_REAGENDAR_CITA;
        } 
        
        actualizarCitaTO.setCedulaAuditor(cedulaSecretaria);

        CitaMedica citaMedica = citaMedicaService.actualizarCitaMedica(actualizarCitaTO);

        if (citaMedica == null) {
            modelo.addAttribute("msgError", "No se pudo reagendar la cita, intente con otra fecha o hora");
            modelo.addAttribute("actualizarCitaTO", actualizarCitaTO);
            return HTML_URL_FORMULARIO_REAGENDAR_CITA;
        }
        redirectAttributes.addFlashAttribute("msgExito",
                "La cita se reagendó la cita: " + citaMedica.getCodigo() + " correctamente");
        return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_CITA;
    }
}
