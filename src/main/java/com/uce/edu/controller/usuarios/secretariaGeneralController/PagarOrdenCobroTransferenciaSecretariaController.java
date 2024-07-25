package com.uce.edu.controller.usuarios.secretariaGeneralController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.FacturaCita;
import com.uce.edu.repository.modelo.OrdenCobro;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IOrdenCobroService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.PagoTransferenciaTO;
import com.uce.edu.util.reportes.secretaria.CitaMedicaPDF;
import com.uce.edu.util.reportes.secretaria.FacturaCitaPDF;
import jakarta.servlet.http.HttpSession;
/**
 * PagarOrdenCobroTransferenciaSecretariaController
 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class PagarOrdenCobroTransferenciaSecretariaController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Secretaria";
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String URL_MENU_ORDEN_COBRO = "/menu-orden-cobro";
    private final String HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA = "secretariaGeneral/formularios/pagar_orden_transferencia_formulario";

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
     * Metodo que permite visualizar el formulario de pago en transferencia
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param pagoTransferenciaTO
     * @param session
     * @return
     */
    @GetMapping("/orden-pago-transferencia")
    public String mostarFormularioPagoTransferencia(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, @RequestParam(name = "pagoTransferenciaTO", required = false) PagoTransferenciaTO pagoTransferenciaTO, HttpSession session) {
        // Atributos para el formulario de Pago
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Pagar en Transferencia");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Pago Transferencia");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("tituloFormulario", "Pago en Transferencia");
            // Título del formulario
            modelo.addAttribute("botonSubmit", "Verificar Datos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO);
            // Url de acción
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-transferencia-generar");

            if (msgExito != null) {
                modelo.addAttribute("msgExito", msgExito);
            }
            if (msgExito != null) {
                modelo.addAttribute("msgEliminacionExitosa", msgEliminacionExitosa);
            }

            if (pagoTransferenciaTO != null) {
                modelo.addAttribute("pagoTransferenciaTO", pagoTransferenciaTO);
            }
            else{
                modelo.addAttribute("pagoTransferenciaTO", new PagoTransferenciaTO());
            }
        }
        return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA;
    }

    /**
     * Metodo que permite verificar los datos de la orden de cobro y el pago en transferencia
     * @param pagoTransferenciaTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pago-orden-pago-transferencia-generar")
    public String pagarOrdenCobroPdf(@Validated PagoTransferenciaTO pagoTransferenciaTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {

        // Atributos para el formulario de Pago

        // Títulos de la página
        modelo.addAttribute("tituloPestaña", "Pagar en Transferencia");
        // Título del navbar
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Pago Transferencia");
        // nombre del usuario logueado para el navbar
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        modelo.addAttribute("tituloFormulario", "Pago en Transferencia");
        // Título del formulario
        modelo.addAttribute("botonSubmit", "Verificar Datos");
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO);
        // Url de acción
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-transferencia-generar");

        if (result.hasErrors()) {
            modelo.addAttribute("pagoTransferenciaTO", pagoTransferenciaTO);
            return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA;
        }

        OrdenCobro ordenCobro = ordenCobroService.buscarOrdenCobroPorCodigo(pagoTransferenciaTO.getOrdenCodigo());
        if (ordenCobro == null || pagoTransferenciaTO.getOrdenCodigo().equals("")
                || pagoTransferenciaTO.getOrdenCodigo() == null) {
         
            modelo.addAttribute("pagoTransferenciaTO", pagoTransferenciaTO);

            modelo.addAttribute("msgError", "No existen una orden de cobro con ese código en el sistema");
            return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA;
        }

        Paciente paciente = ordenCobro.getPaciente();
        if (paciente == null) {
        
            modelo.addAttribute("pagoTransferenciaTO", pagoTransferenciaTO);
            modelo.addAttribute("msgError", "No existen pacientes ese paciente en el sistema");
            return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA;
        }

        session.setAttribute("cedulaPacientePaso", paciente.getCedula());

        String cedulaAuditor = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuario = usuarioService.buscarPorCedula(cedulaAuditor);

        if (usuario == null) {
           
            modelo.addAttribute("pagoTransferenciaTO", pagoTransferenciaTO);
            modelo.addAttribute("msgError", "No existe este usuario en el sistema");
            return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA;
        }

        if (paciente.getCedula().equals(cedulaAuditor)) {
        
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
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-transferencia-confirmar");

        modelo.addAttribute("ordenCobro", ordenCobro);
        modelo.addAttribute("pagoTransferenciaTO", pagoTransferenciaTO);
        modelo.addAttribute("botonSubmit", "Confirmar");
        return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA; // Retorna la vista seleccionada al final
    }

    /**
     * Metodo que permite confirmar el pago en transferencia
     * @param pagoTransferenciaTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pago-orden-pago-transferencia-confirmar")
    public String pagarOrdenCobro(@Validated PagoTransferenciaTO pagoTransferenciaTO, BindingResult result,
    RedirectAttributes redirectAttributes,Model modelo, HttpSession session) {
        // Atributos para el formulario de Pago
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Confirmar Pagar en Transferencia");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Confirmar Pago Transferencia");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("tituloFormulario", "Pago en Transferencia");
            // Título del formulario
            modelo.addAttribute("botonSubmit", "Confirmar Pago");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_ORDEN_COBRO);
            // Url de acción
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pago-orden-pago-transferencia-generar");
        }

        boolean tarjetaValida = validarTarjeta(pagoTransferenciaTO.getTarjeta());
        if (!tarjetaValida) {
            redirectAttributes.addFlashAttribute("pagoTransferenciaTO", pagoTransferenciaTO);
            redirectAttributes.addFlashAttribute("msgError",
                    "La tarjeta no pudo ser procesada. Por favor, intente con otra tarjeta.");
                    return "redirect:" + URL_MENU_PRINCIPAL + "/orden-pago-transferencia" ;
        }

        // Lógica de negocio
        Paciente paciente = (Paciente) session.getAttribute("pacientePaso");
        Usuario usuario = (Usuario) session.getAttribute("usuarioPaso");
        OrdenCobro ordenCobro = (OrdenCobro) session.getAttribute("ordenPaso");

        List<String> lista = citaMedicaService.generarFacturaYCitaPagoTransferencia(paciente, usuario, ordenCobro);

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

        return HTML_URL_FORMULARIO_COBRO_TRANSFERENCIA + "_pdf";
    }

    /**
     * Metodo que permite generar el pdf de la factura
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/transferencia-factura-generar-pdf")
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
     * Metodo que permite generar el pdf de la cita
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/transferencia-cita-generar-pdf")
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

    /**
     * Metodo que permite validar la tarjeta
     * @param numeroTarjeta
     * @return
     */
     private boolean validarTarjeta(String numeroTarjeta) {
        // Generar un número aleatorio entre 0 y 1
        double aleatorio = new Random().nextDouble();
        // Considerar la tarjeta como válida si el número aleatorio es >= 0.5
        return aleatorio >= 0.5;
    }

}
