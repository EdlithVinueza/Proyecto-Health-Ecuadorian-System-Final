package com.uce.edu.controller.usuarios.secretariaGeneralController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.uce.edu.repository.modelo.FacturaCita;
import com.uce.edu.repository.modelo.OrdenCobro;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IOrdenCobroService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.PagoEfectivoTO;
import com.uce.edu.util.reportes.secretaria.CitaMedicaPDF;
import com.uce.edu.util.reportes.secretaria.FacturaCitaPDF;
import jakarta.servlet.http.HttpSession;

/**
 * Pagar Orden Cobro Efectivo Secretaria Controller
 * 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class PagarOrdenCobroEfectivoSecretariaController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Secretaria";
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String URL_MENU_ORDEN_COBRO = "/menu-orden-cobro";
    private final String HTML_URL_FORMULARIO_COBRO_EFECTIVO = "secretariaGeneral/formularios/pagar_orden_efectivo_formulario";

    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IOrdenCobroService ordenCobroService;

    @Autowired
    private ICitaMedicaService citaMedicaService;

    /**
     * Metodo que permite visualizar el formulario de pago en efectivo
     * 
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/orden-pago-efectivo")
    public String mostarFormularioPagoEfectivo(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        // Atributos para el formulario de Pago
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Pagar en Efectivo");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Pago Efectivo");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("tituloFormulario", "Pago en efectivo");
            // Título del formulario
            modelo.addAttribute("botonSubmit", "Verificar Datos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO);
            // Url de acción
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-efectivo-generar");

            if (msgExito != null) {
                modelo.addAttribute("msgExito", msgExito);
            }

        }
        modelo.addAttribute("pagoEfectivoTO", new PagoEfectivoTO());
        return HTML_URL_FORMULARIO_COBRO_EFECTIVO;
    }

    /**
     * Metodo que permite pagar una orden de cobro en efectivo
     * 
     * @param pagoEfectivoTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pago-orden-pago-efectivo-generar")
    public String pagarOrdenCobroEfectivo(@Validated PagoEfectivoTO pagoEfectivoTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {

        // Atributos para el formulario de Pago

        // Títulos de la página
        modelo.addAttribute("tituloPestaña", "Pagar en Efectivo");
        // Título del navbar
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Pago Efectivo");
        // nombre del usuario logueado para el navbar
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloFormulario", "Pago en efectivo");
        // Título del formulario
        modelo.addAttribute("botonSubmit", "Verificar Datos");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO);
        // Url de acción
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-efectivo-generar");

        if (result.hasErrors()) {
            modelo.addAttribute("pagoEfectivoTO", pagoEfectivoTO);
            if (pagoEfectivoTO.getDineroEnCaja() == false || pagoEfectivoTO.getDineroEnCaja() == null) {
                modelo.addAttribute("msgError", "No se guardo en dinero en la caja");
            }
            return HTML_URL_FORMULARIO_COBRO_EFECTIVO;
        }

        OrdenCobro ordenCobro = ordenCobroService.buscarOrdenCobroPorCodigo(pagoEfectivoTO.getOrdenCodigo());
        if (ordenCobro == null || pagoEfectivoTO.getOrdenCodigo().equals("")
                || pagoEfectivoTO.getOrdenCodigo() == null) {
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("pagoEfectivoTO", pagoEfectivoTO);

            modelo.addAttribute("msgError", "No existen una orden de cobro con ese código en el sistema");
            return HTML_URL_FORMULARIO_COBRO_EFECTIVO;
        }

        Paciente paciente = ordenCobro.getPaciente();
        if (paciente == null) {
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("pagoEfectivoTO", pagoEfectivoTO);
            modelo.addAttribute("msgError", "No existen pacientes ese paciente en el sistema");
            return HTML_URL_FORMULARIO_COBRO_EFECTIVO;
        }

        session.setAttribute("cedulaPacientePaso", paciente.getCedula());

        String cedulaAuditor = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuario = usuarioService.buscarPorCedula(cedulaAuditor);

        if (usuario == null) {
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("pagoEfectivoTO", pagoEfectivoTO);
            modelo.addAttribute("msgError", "No existe este usuario en el sistema");
            return HTML_URL_FORMULARIO_COBRO_EFECTIVO;
        }

        if (paciente.getCedula().equals(cedulaAuditor)) {
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("pagoEfectivoTO", pagoEfectivoTO);
            redirectAttributes.addFlashAttribute("msgError",
                    "La cedula del paciente no puede ser la misma que la del auditor");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO;
        }

        if (ordenCobroService.validarCaducidadOrdenCobro(ordenCobro)) {
            redirectAttributes.addFlashAttribute("msgError",
                    "La orden de cobro esta caducada, ya no se puede guardar la cita");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO;
        }

        if (ordenCobroService.esOrdenCobroPagada(ordenCobro)) {
            redirectAttributes.addFlashAttribute("msgError", "La orden de cobro ya esta Pagada");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO;
        }

        modelo.addAttribute("msgExito", "El paciente y la cedula contan en el sistema y la orden es valida");
        session.setAttribute("pacientePaso", paciente);
        session.setAttribute("usuarioPaso", usuario);
        session.setAttribute("ordenPaso", ordenCobro);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-efectivo-confirmar");

        modelo.addAttribute("ordenCobro", ordenCobro);
        modelo.addAttribute("pagoEfectivoTO", pagoEfectivoTO);
        modelo.addAttribute("botonSubmit", "Confirmar");
        return HTML_URL_FORMULARIO_COBRO_EFECTIVO; // Retorna la vista seleccionada al final
    }

    /**
     * Metodo que permite confirmar el pago de una orden de cobro en efectivo
     * 
     * @param pagoEfectivoTO
     * @param result
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pago-orden-pago-efectivo-confirmar")
    public String pagarOrdenCobro(@Validated PagoEfectivoTO pagoEfectivoTO, BindingResult result,
            Model modelo, HttpSession session) {
        // Atributos para el formulario de Pago
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Confirmar Pagar en Efectivo");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Confirmar Pago Efectivo");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("tituloFormulario", "Pago en efectivo");
            // Título del formulario
            modelo.addAttribute("botonSubmit", "Confirmar Pago");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO);
            // Url de acción
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-efectivo-generar");
        }

        // Lógica de negocio
        Paciente paciente = (Paciente) session.getAttribute("pacientePaso");
        Usuario usuario = (Usuario) session.getAttribute("usuarioPaso");
        OrdenCobro ordenCobro = (OrdenCobro) session.getAttribute("ordenPaso");

        List<String> lista = citaMedicaService.generarFacturaYCitaPagoEfectivo(paciente, usuario, ordenCobro);

        String pacientePaso = (String) session.getAttribute("cedulaPacientePaso");

        String facturaCitaNumero = (String) lista.get(0);
        session.setAttribute("facturaCitaNumero", facturaCitaNumero);

        String citaMedicaCodigo = (String) lista.get(1);
        session.setAttribute("citaMedicaCodigo", citaMedicaCodigo);

        if (citaMedicaService.enviarFacturaYCitaAlCorreoPaciente(pacientePaso, facturaCitaNumero, citaMedicaCodigo)) {
            modelo.addAttribute("msgExito", "Se envió la Factura y los datos de la cita al correo del paciente");
        } else {
            modelo.addAttribute("msgError",
                    "No se pudo enviar la Factura y los datos de la cita  al correo del paciente");
        }
        modelo.addAttribute("msgExito",
                "Pago exitoso, Factura emitida" + facturaCitaNumero + "Número Cita:"
                        + citaMedicaCodigo);

        return HTML_URL_FORMULARIO_COBRO_EFECTIVO + "_pdf";
    }

    /**
     * Metodo que permite generar un pdf de la factura de una cita medica
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/efectivo-factura-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfFactura(HttpSession session) throws IOException {
        String facturaCitaNumero = (String) session.getAttribute("facturaCitaNumero");

        FacturaCita facturaCita = citaMedicaService.buscarFacturaCitaPorNumero(facturaCitaNumero);
        FacturaCitaPDF facturaCitaPDF = new FacturaCitaPDF();
        ByteArrayOutputStream byteArrayOutputStream = facturaCitaPDF.generarPdfFacturaCita(facturaCita);
        String filename = "filename=" + "factura_" + facturaCitaNumero + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

    /**
     * Metodo que permite generar un pdf de la cita medica
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/efectivo-cita-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfCita(HttpSession session) throws IOException {
        String citaMedicaCodigo = (String) session.getAttribute("citaMedicaCodigo");
        CitaMedica citaMedica = citaMedicaService.buscarCitaMedicaPorCodigo(citaMedicaCodigo);

        CitaMedicaPDF citaMedicaPDF = new CitaMedicaPDF();
        ByteArrayOutputStream byteArrayOutputStream = citaMedicaPDF.generarPdfCitaMedica(citaMedica);

        String filename = "filename=" + "cita_medica_informacion" + citaMedicaCodigo + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }
}
