package com.uce.edu.controller.pacientres;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.OrdenCobro;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IDoctorService;
import com.uce.edu.service.interfacesSistemaPrincipal.IEspecialidadService;
import com.uce.edu.service.interfacesSistemaPrincipal.IOrdenCobroService;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.to.AgendarCitaTO;
import com.uce.edu.service.to.BuscarCitaTO;
import com.uce.edu.service.to.DatosCitaAgendar;
import com.uce.edu.util.reportes.secretaria.OrdenCobroPDF;

import jakarta.servlet.http.HttpSession;

/**
 * Agendar Cita Paciente Controller
 * 
 */
@Controller
@RequestMapping("pacientes")
public class AgendarCitaPacienteController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Paciente";
    private final String NOMBRE_NAVBAR = "nombrePasoPaciente";
    private final String URL_MENU_PRINCIPAL = "/pacientes";
    private final String URL_LISTA_CITAS_PARA_AGENDAR = "/cita-agendar-ver-todo";
    private final String URL_MENU_CITA = "/menu-citas";
    private final String HTML_URL_FORMULARIO_AGENDAR_CITA = "pacientes/formularios/agendar_cita_formulario";
    private final String HTML_URL_LISTA_BUSCAR_CITA = "pacientes/listas/buscar_cita_lista";
    /**
     * Dependencias
     */
    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private IEspecialidadService especialidadService;

    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private ICitaMedicaService citaMedicaService;

    @Autowired
    private IOrdenCobroService ordenCobroService;

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param msgExito
     * @param msgEliminacionExitosa
     * @param session
     * @return
     */
    @GetMapping("/cita-agendar-ver-todo")
    public String listarCitasEncontradas(Model modelo, @ModelAttribute("msgExito") String msgExito,
            @ModelAttribute("msgEliminacionExitosa") String msgEliminacionExitosa, HttpSession session) {
        // Atributos para la lista de citas
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Citas Encontradas");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + "Citas Encontradas");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de citas encontradas");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
            // Lista de especialidades
            List<String> especialidades = especialidadService.buscarNombresEspecialidades();
            modelo.addAttribute("listaEspecialidades", especialidades);
            // Mensajes de éxito o error
            if (msgExito != null) {
                modelo.addAttribute("msgExito", msgExito);
            }
            if (msgExito != null) {
                modelo.addAttribute("msgEliminacionExitosa", msgEliminacionExitosa);
            }
        }
        modelo.addAttribute("buscarCitaTO", new BuscarCitaTO());
        return HTML_URL_LISTA_BUSCAR_CITA;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param especialidad
     * @param diaCita
     * @param horaCita
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/cita-buscar-por-formulario")
    public String buscarCitas(@RequestParam("especialidad") String especialidad,
            @RequestParam("diaCita") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate diaCita,
            @RequestParam("horaCita") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaCita, Model modelo,
            HttpSession session) {
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Citas Encontradas");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + "Citas Encontradas");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de citas encontradas");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);
            // Lista de especialidades
            List<String> especialidades = especialidadService.buscarNombresEspecialidades();
            modelo.addAttribute("listaEspecialidades", especialidades);
        }
        if ((especialidad == null || especialidad.isEmpty()) || especialidad.trim().isEmpty() || diaCita == null
                || horaCita == null) {
            modelo.addAttribute("msgError", "Todos los campos son obligatorios y no deben estar vacíos.");
            // Reenviar los valores ingresados por el usuario para que puedan ser
            // reutilizados
            modelo.addAttribute("especialidadIngresada", especialidad);
            modelo.addAttribute("diaCitaIngresado", diaCita);
            modelo.addAttribute("horaCitaIngresada", horaCita);
            return HTML_URL_LISTA_BUSCAR_CITA; // Asegúrate de reemplazar esto con la ruta correcta a tu vista
                                               // de error
        }
        LocalDateTime fecha = diaCita.atStartOfDay();

        List<DoctorDatosDTO> doctorDatosDTOs = doctorService.filtrarDoctoresSinCita(especialidad, fecha, horaCita);

        if (doctorDatosDTOs == null || doctorDatosDTOs.isEmpty()) {
            modelo.addAttribute("msgError", "No existen citas segun los parametros ingresados");
            modelo.addAttribute("listaDoctorDatosDTO", new ArrayList<DoctorDatosDTO>());
            return HTML_URL_LISTA_BUSCAR_CITA;
        }
        if (!doctorDatosDTOs.isEmpty() || doctorDatosDTOs != null) {
            session.setAttribute("doctorDatosDTOs", doctorDatosDTOs); // Guardar en sesión
            modelo.addAttribute("msgExito", "Existen citas");
        }
        BuscarCitaTO buscarCitaTO = new BuscarCitaTO();
        buscarCitaTO.setEspecialidad(especialidad);
        buscarCitaTO.setDiaCita(diaCita);
        buscarCitaTO.setHoraCita(horaCita);
        session.setAttribute("buscarCitaTO", buscarCitaTO);
        modelo.addAttribute("listaDoctorDatosDTO", doctorDatosDTOs);

        return HTML_URL_LISTA_BUSCAR_CITA;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param doctorDatosDTO
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/agendar-cita")
    public String mostrarFormularioAgendarCita(DoctorDatosDTO doctorDatosDTO, Model modelo, HttpSession session) {
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Agendar Cita");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Agendar Cita");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Agendar Cita");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Verificar Datos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_CITAS_PARA_AGENDAR);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/guardar_cita_en_agendada");
        }
        session.setAttribute("doctorDatosDTO", doctorDatosDTO);
        modelo.addAttribute("doctorDatosDTO", doctorDatosDTO);
        BuscarCitaTO buscarCitaTO = (BuscarCitaTO) session.getAttribute("buscarCitaTO");
        DatosCitaAgendar datosCitaAgendar = citaMedicaService.crearCitaAgendar(buscarCitaTO, doctorDatosDTO);
        modelo.addAttribute("datosCitaAgendar", datosCitaAgendar);
        session.setAttribute("datosCitaAgendar", datosCitaAgendar);

        return HTML_URL_FORMULARIO_AGENDAR_CITA; // Asegúrate de reemplazar esto con la ruta correcta a
                                                 // tu vista del formulario
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param cedulaPaciente
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/guardar_cita_en_agendada")
    public String guardarCitaEnAgenda(
            @RequestParam("cedulaPaciente") String cedulaPaciente, RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Agendar Cita");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " Agendar Cita");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Agendar Cita");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Verificar Datos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA_CITAS_PARA_AGENDAR);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/guardar_cita_en_agendada");
        }

        DatosCitaAgendar datosCitaAgendar = (DatosCitaAgendar) session.getAttribute("datosCitaAgendar");
        modelo.addAttribute("datosCitaAgendar", datosCitaAgendar);
        // Guardar la cédula del paciente en la sesión

        String cedulaUsuarioActual = "System";

        Paciente paciente = pacienteService.buscarPacientePorCedulaPaciente(cedulaPaciente);

        if (paciente == null) {
            modelo.addAttribute("msgError", "No existe ese paciente en el sistema");
            modelo.addAttribute("cedulaPaciente", cedulaPaciente);
            /// --> Aque seria poner un botton para agregar un nuevo paciente
            return HTML_URL_FORMULARIO_AGENDAR_CITA;
        }

        if (cedulaPaciente.equals(cedulaUsuarioActual)) {
            modelo.addAttribute("msgError", "No puedes agendar una cita como secretario, ve al apartado de pacientes");
            modelo.addAttribute("cedulaPaciente", cedulaPaciente);
            return HTML_URL_FORMULARIO_AGENDAR_CITA;
        }

        session.setAttribute("cedulaPaciente", cedulaPaciente);
        modelo.addAttribute("cedulaPaciente", cedulaPaciente);
        modelo.addAttribute("paciente", paciente);
        modelo.addAttribute("msgExito", "El Paciente consta en el sistema");

        AgendarCitaTO agendarCitaTO = new AgendarCitaTO();
        agendarCitaTO.setCedulaPaciente(cedulaPaciente);
        agendarCitaTO.setCedulaAuditor(cedulaUsuarioActual);
        session.setAttribute("agendarCitaTO", agendarCitaTO);
        modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/agendar-cita-confirmar");
        modelo.addAttribute("botonSubmit", "Agendar Cita");

        return HTML_URL_FORMULARIO_AGENDAR_CITA; // Reemplaza esto con la vista que deseas mostrar
                                                 // después de procesar el
        // formulario
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param session
     * @return
     * @throws IOException
     */
    @PostMapping("/agendar-cita-confirmar")
    public String confirmarAgendarCita(Model modelo, HttpSession session) throws IOException {

        // Títulos de la página
        modelo.addAttribute("tituloPestaña", "Agendar Cita");
        // Título del navbar
        modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Agendar Cita");
        // Titulo del formulario
        modelo.addAttribute("tituloFormulario", "Orden de Cobro Generada");
        // nombre del usuario logueado para el navbar
        String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
        modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
        // Url de retorno
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_CITA);

        String cedulaPacienteGuardada = (String) session.getAttribute("cedulaPaciente");
        modelo.addAttribute("cedulaPaciente", cedulaPacienteGuardada);

        AgendarCitaTO agendarCitaTO = (AgendarCitaTO) session.getAttribute("agendarCitaTO");
        DoctorDatosDTO doctorDatosDTO = (DoctorDatosDTO) session.getAttribute("doctorDatosDTO");
        BuscarCitaTO buscarCitaTO = (BuscarCitaTO) session.getAttribute("buscarCitaTO");

        OrdenCobro ordenCobro = citaMedicaService.crearOrdenCobro(agendarCitaTO, doctorDatosDTO, buscarCitaTO);
        // Poner boton para pdf
        modelo.addAttribute("generarPdf", true);
        // Mandar orden a correo electronico

        if (citaMedicaService.enviarOrdenDeCobroAlCorreoPaciente(ordenCobro.getPaciente(), ordenCobro)) {
            modelo.addAttribute("msgExito", "Se envió la orden de cobro al correo del paciente");
        } else {
            modelo.addAttribute("msgExito", "Se creo la orden de cobro para: "
                    + ordenCobro.getPaciente().getNombre() + "en el sistema");
            modelo.addAttribute("msgError", "No se pudo enviar la orden de cobro al correo del paciente");

        }

        String cedulaPaciente = (String) session.getAttribute("cedulaPasoPaciente");

        modelo.addAttribute("cedulaPaciente", cedulaPaciente);
        modelo.addAttribute("cedulaAuditor", "System");

        session.setAttribute("ordenCobro", ordenCobro);
        session.setAttribute("codigoOrdenCobro", ordenCobro.getCodigo());
        return HTML_URL_FORMULARIO_AGENDAR_CITA + "_pdf"; // Asegúrate de reemplazar esto con la ruta correcta
                                                          // a
        // tu vista de confirmación
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/orden-cobro-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfOrdenCobro(HttpSession session) throws IOException {
        String codigo = (String) session.getAttribute("codigoOrdenCobro");
        OrdenCobro ordenCobro = ordenCobroService.buscarOrdenCobroPorCodigo(codigo);
        OrdenCobroPDF ordenCobroPDF = new OrdenCobroPDF();
        ByteArrayOutputStream byteArrayOutputStream = ordenCobroPDF.generarPdfOrdenCobro(ordenCobro);
        String filename = "filename=" + "orden_cobro_" + LocalDateTime.now() + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

}
