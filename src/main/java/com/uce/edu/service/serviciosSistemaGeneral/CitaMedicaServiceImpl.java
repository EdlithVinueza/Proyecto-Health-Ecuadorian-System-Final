package com.uce.edu.service.serviciosSistemaGeneral;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.ICitaMedicaRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IConsultorioRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IEspecialiadRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IFacturaCitaRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IOrdenCobroRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IPacienteRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IPagoRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.AgendaCitaTemporal;
import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.FacturaCita;
import com.uce.edu.repository.modelo.IAgendaCitaTemporalRepository;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.Pago;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.OrdenCobro;
import com.uce.edu.repository.modelo.dto.CitaDTO;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.emailService.EmailService;
import com.uce.edu.service.interfacesSistemaPrincipal.ICitaMedicaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IDoctorService;
import com.uce.edu.service.interfacesSistemaPrincipal.IFacturaCitaService;
import com.uce.edu.service.interfacesSistemaPrincipal.IOrdenCobroService;
import com.uce.edu.service.to.ActualizarCitaTO;
import com.uce.edu.service.to.AgendarCitaTO;
import com.uce.edu.service.to.BuscarCitaTO;
import com.uce.edu.service.to.DatosCitaAgendar;
import com.uce.edu.util.Enum.EstadoCita;
import com.uce.edu.util.Enum.EstadoOrdenCobro;
import com.uce.edu.util.Enum.EstadoPago;
import com.uce.edu.util.reportes.secretaria.CitaMedicaPDF;
import com.uce.edu.util.reportes.secretaria.FacturaCitaPDF;
import com.uce.edu.util.reportes.secretaria.OrdenCobroPDF;

import jakarta.transaction.Transactional;

@Service
public class CitaMedicaServiceImpl implements ICitaMedicaService {

    @Autowired
    private ICitaMedicaRepository citaMedicaRepository;

    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private IPacienteRepository pacienteRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IEspecialiadRepository especialidadRepository;
    @Autowired
    private IOrdenCobroRepository ordenCobroRepository;

    @Autowired
    private IOrdenCobroService ordenCobroService;

    @Autowired
    private IConsultorioRepository consultorioRepository;

    @Autowired
    private IAgendaCitaTemporalRepository agendaCitaTemporalRepository;

    @Autowired
    private IFacturaCitaService facturaCitaService;

    @Autowired
    private IFacturaCitaRepository facturaCitaRepository;

    @Autowired
    private IPagoRepository pagoRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public boolean buscarCitaMedica(Especialidad especialidad, Doctor doctor, LocalDateTime Fecha, LocalTime hora) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarCitaMedica'");
    }

    @Override
    public OrdenCobro crearOrdenCobro(AgendarCitaTO agendarCitaTO, DoctorDatosDTO doctorDatosDTO,
            BuscarCitaTO buscarCitaTO) {

        Doctor doctor = doctorRepository.findByCedula(doctorDatosDTO.getCedulaDoctor());
        Paciente paciente = pacienteRepository.findByCedula(agendarCitaTO.getCedulaPaciente());
        String NombreUsuario  = null;
        if (agendarCitaTO.getCedulaAuditor().equals("System")) {
            NombreUsuario = "System";
        } else {
            Usuario usuario = usuarioRepository.findByCedula(agendarCitaTO.getCedulaAuditor());
            NombreUsuario = usuario.getNombre() + " " + usuario.getApellido();
        }
        Especialidad especialidad = especialidadRepository.findByNombre(doctorDatosDTO.getEspecialidad());

        OrdenCobro ordenCobro = new OrdenCobro();
        ordenCobro.setPaciente(paciente);
        ordenCobro.setCodigo(ordenCobroService.generarCodigoUnicoOrdenCobro());
        ordenCobro.setFechaEmision(LocalDateTime.now());
        /**
         * ordenCobro.setFechaLimitePago(LocalDateTime.now().plusMinutes(20));
         */
        /**
         * Esto es solo para pruebas
         */
        ordenCobro.setFechaLimitePago(LocalDateTime.now().plusDays(2));
        ordenCobro.setMonto(especialidad.getPrecio());
        ordenCobro.setNombreAuiditorEmisor(NombreUsuario);
        ordenCobro.setCedulaAuditorEmisor(agendarCitaTO.getCedulaAuditor());
        ordenCobro.setEstado(EstadoOrdenCobro.POR_COBRAR.getEstado());

        ordenCobroRepository.save(ordenCobro);

        AgendaCitaTemporal agendaCitaTemporal = new AgendaCitaTemporal();
        agendaCitaTemporal.setDoctor(doctor);
        agendaCitaTemporal.setPaciente(paciente);
        agendaCitaTemporal.setConsultorio(consultorioRepository.findByCodigo(doctorDatosDTO.getConsultorioCodigo()));
        agendaCitaTemporal.setEspecialidad(especialidad);
        agendaCitaTemporal.setOrdenCobro(ordenCobro);
        agendaCitaTemporal.setFechaCita(buscarCitaTO.getDiaCita().atStartOfDay());
        agendaCitaTemporal.setHoraInicio(buscarCitaTO.getHoraCita());
        agendaCitaTemporal.setHoraFin(buscarCitaTO.getHoraCita().plusMinutes(doctorDatosDTO.getTiempoConsulta()));
        agendaCitaTemporal.setEstado(EstadoCita.SIN_PAGAR.getEstado());
        agendaCitaTemporal.setNombreAuditorAgendador(NombreUsuario);
        agendaCitaTemporal.setCedulaAuditorAgendador(agendarCitaTO.getCedulaAuditor());

        agendaCitaTemporalRepository.save(agendaCitaTemporal);

        ordenCobro.setAgendaCitaTemporal(agendaCitaTemporal);
        ordenCobroRepository.save(ordenCobro);

        return ordenCobro;
    }

    @Override
    public boolean esFechaValida(LocalDate diaCita) {
        LocalDate fechaActual = LocalDate.now();
        return !diaCita.isBefore(fechaActual); // Retorna true si diaCita es hoy o una fecha futura
    }

    @Override
    public boolean esHoraValidaSiFechaEsHoy(LocalDate diaCita, LocalTime horaCita) {
        if (diaCita.equals(LocalDate.now())) {
            LocalTime horaActual = LocalTime.now();
            return !horaCita.isBefore(horaActual); // Retorna true si horaCita es igual o posterior a la hora actual
        }
        return true;
    }

    @Override
    @Transactional
    public List<String> generarFacturaYCitaPagoEfectivo(Paciente paciente, Usuario usuario, OrdenCobro ordenCobro) {
        Doctor doctor = ordenCobro.getAgendaCitaTemporal().getDoctor();
        Especialidad especialidad = ordenCobro.getAgendaCitaTemporal().getEspecialidad();

        AgendaCitaTemporal agendaCitaTemporal = ordenCobro.getAgendaCitaTemporal();

        CitaMedica citaMedica = new CitaMedica();

        citaMedica.setDoctor(doctor);
        citaMedica.setPaciente(paciente);
        citaMedica.setConsultorio(agendaCitaTemporal.getConsultorio());
        citaMedica.setEspecialidad(agendaCitaTemporal.getEspecialidad());
        //citaMedica.setOrdenCobro(ordenCobro);
        citaMedica.setCodigo(ordenCobroService.generarCodigoUnicoOrdenCobro());

        agendaCitaTemporal.setEstado(EstadoCita.AGENDADA.getEstado());
        agendaCitaTemporalRepository.save(agendaCitaTemporal);

        citaMedica.setFechaCita(agendaCitaTemporal.getFechaCita());
        citaMedica.setHoraInicio(agendaCitaTemporal.getHoraInicio());
        citaMedica.setHoraFin(agendaCitaTemporal.getHoraFin());
        citaMedica.setEstado(EstadoCita.AGENDADA.getEstado());
        citaMedica.setNombreAuditorEmisor(agendaCitaTemporal.getNombreAuditorAgendador());

        citaMedica.setCedulaeAuditorEmisor(agendaCitaTemporal.getCedulaAuditorAgendador());
        /**
         * citaMedica.setAgendadaAuditoria(agendaCitaTemporal.getCedulaAgendador());
         */
        /**
         * falta poner cedula del auditor, y poner cedula y nombre del cobrador de la
         * cita medica y cedula del modificador
         */
        citaMedicaRepository.save(citaMedica);

        FacturaCita facturaCita = new FacturaCita();
        facturaCita.setPaciente(paciente);
        facturaCita.setCitaMedica(citaMedica);
       // facturaCita.setOrdenCobro(ordenCobro);
        facturaCita.setFechaEmision(LocalDateTime.now());
        facturaCita.setHoraEmision(LocalTime.now());
        facturaCita.setNumeroAutorizacion(facturaCitaService.generarNumeroAutorizacion());
        facturaCita.setNumeroFactura(facturaCitaService.generarNumeroParaFactura());
        facturaCita.setNombreCliente(paciente.getNombre() + " " + paciente.getApellido());
        facturaCita.setCedulaPaciente(paciente.getCedula());
        facturaCita.setDireccionPaciente(paciente.getDireccion());
        facturaCita.setTelefonoPaciente(paciente.getTelefono());
        facturaCita.setCorreoPaciente(paciente.getCorreo());
        facturaCita.setEspecialidad(especialidad.getNombre());
        facturaCita.setNombreAuditor(usuario.getNombre() + " " + usuario.getApellido());
        facturaCita.setCedulaAuditor(usuario.getCedula());
        facturaCita.setIva(new BigDecimal(0.15));
        facturaCita.setSubTotal(especialidad.getPrecio());
        facturaCita.setDescuentos(new BigDecimal(0.0));
        BigDecimal porcentaje = especialidad.getPrecio().multiply(new BigDecimal(0.15));
        facturaCita.setTotalPagar(porcentaje.add(especialidad.getPrecio()));
        facturaCitaRepository.save(facturaCita);

        Pago pago = new Pago();
        pago.setPaciente(paciente);
        //pago.setOrdenCobro(ordenCobro);
        pago.setFacturaCita(facturaCita);
        //pago.setOrdenCobro(ordenCobro);
        pago.setFechaPago(LocalDateTime.now());
        pago.setHoraPago(LocalTime.now());
        pago.setMontoPago(porcentaje.add(especialidad.getPrecio()));
        pago.setMetodoPago("Efectivo");
        pago.setAuditoriaCobrador(usuario.getNombre() + " " + usuario.getApellido());
        pago.setCedulaCobrador(usuario.getCedula());
        pago.setEstado(EstadoPago.PAGADO.getEstado());

        pagoRepository.save(pago);
        facturaCita.setPago(pago);
        facturaCitaRepository.save(facturaCita);

        ordenCobro.setNombreAuiditorCobrador(usuario.getNombre() + " " + usuario.getApellido());
        ordenCobro.setCedulaAuditorCobrador(usuario.getCedula());
       // ordenCobro.setFacturaCita(facturaCita);
        //ordenCobro.setPago(pago);
        ordenCobro.setEstado(EstadoOrdenCobro.COBRADA.getEstado());
        ordenCobroRepository.save(ordenCobro);

        List<String> resultado = new ArrayList<>();
        resultado.add(facturaCita.getNumeroFactura());
        resultado.add(citaMedica.getCodigo());

        return resultado;
    }

    @Override
    public List<String> generarFacturaYCitaPagoTransferencia(Paciente paciente, Usuario usuario,
            OrdenCobro ordenCobro) {
        String nombreUsuario = null;
        String cedulaUsuario = null;
        if (usuario == null) {
            nombreUsuario = "System";
            cedulaUsuario = "System";
        }
        else {
            nombreUsuario = usuario.getNombre() + " " + usuario.getApellido();
            cedulaUsuario = usuario.getCedula();
        }
        Doctor doctor = ordenCobro.getAgendaCitaTemporal().getDoctor();
        Especialidad especialidad = ordenCobro.getAgendaCitaTemporal().getEspecialidad();

        AgendaCitaTemporal agendaCitaTemporal = ordenCobro.getAgendaCitaTemporal();

        CitaMedica citaMedica = new CitaMedica();

        citaMedica.setDoctor(doctor);
        citaMedica.setPaciente(paciente);
        citaMedica.setConsultorio(agendaCitaTemporal.getConsultorio());
        citaMedica.setEspecialidad(agendaCitaTemporal.getEspecialidad());
       // citaMedica.setOrdenCobro(ordenCobro);
        citaMedica.setCodigo(ordenCobroService.generarCodigoUnicoOrdenCobro());

        agendaCitaTemporal.setEstado(EstadoCita.AGENDADA.getEstado());
        agendaCitaTemporalRepository.save(agendaCitaTemporal);

        citaMedica.setFechaCita(agendaCitaTemporal.getFechaCita());
        citaMedica.setHoraInicio(agendaCitaTemporal.getHoraInicio());
        citaMedica.setHoraFin(agendaCitaTemporal.getHoraFin());
        citaMedica.setEstado(EstadoCita.AGENDADA.getEstado());
        citaMedica.setNombreAuditorEmisor(agendaCitaTemporal.getNombreAuditorAgendador());

        citaMedica.setCedulaeAuditorEmisor(agendaCitaTemporal.getCedulaAuditorAgendador());
        /**
         * citaMedica.setAgendadaAuditoria(agendaCitaTemporal.getCedulaAgendador());
         */
        /**
         * falta poner cedula del auditor, y poner cedula y nombre del cobrador de la
         * cita medica y cedula del modificador
         */
        citaMedicaRepository.save(citaMedica);

        FacturaCita facturaCita = new FacturaCita();
        facturaCita.setPaciente(paciente);
        facturaCita.setCitaMedica(citaMedica);
       // facturaCita.setOrdenCobro(ordenCobro);
        facturaCita.setFechaEmision(LocalDateTime.now());
        facturaCita.setHoraEmision(LocalTime.now());
        facturaCita.setNumeroAutorizacion(facturaCitaService.generarNumeroAutorizacion());
        facturaCita.setNumeroFactura(facturaCitaService.generarNumeroParaFactura());
        facturaCita.setNombreCliente(paciente.getNombre() + " " + paciente.getApellido());
        facturaCita.setCedulaPaciente(paciente.getCedula());
        facturaCita.setDireccionPaciente(paciente.getDireccion());
        facturaCita.setTelefonoPaciente(paciente.getTelefono());
        facturaCita.setCorreoPaciente(paciente.getCorreo());
        facturaCita.setEspecialidad(especialidad.getNombre());
        facturaCita.setNombreAuditor(nombreUsuario);
        facturaCita.setCedulaAuditor(cedulaUsuario);
        facturaCita.setIva(new BigDecimal(0.15));
        facturaCita.setSubTotal(especialidad.getPrecio());
        facturaCita.setDescuentos(new BigDecimal(0.0));
        BigDecimal porcentaje = especialidad.getPrecio().multiply(new BigDecimal(0.15));
        facturaCita.setTotalPagar(porcentaje.add(especialidad.getPrecio()));
        facturaCitaRepository.save(facturaCita);

        Pago pago = new Pago();
        pago.setPaciente(paciente);
       // pago.setOrdenCobro(ordenCobro);
        pago.setFacturaCita(facturaCita);
       // pago.setOrdenCobro(ordenCobro);
        pago.setFechaPago(LocalDateTime.now());
        pago.setHoraPago(LocalTime.now());
        pago.setMontoPago(porcentaje.add(especialidad.getPrecio()));
        pago.setMetodoPago("Efectivo");
        pago.setAuditoriaCobrador(nombreUsuario);
        pago.setCedulaCobrador(cedulaUsuario);
        pago.setEstado(EstadoPago.PAGADO.getEstado());

        pagoRepository.save(pago);
        facturaCita.setPago(pago);
        facturaCitaRepository.save(facturaCita);

        ordenCobro.setNombreAuiditorCobrador(nombreUsuario);
        ordenCobro.setCedulaAuditorCobrador(cedulaUsuario);
      //  ordenCobro.setFacturaCita(facturaCita);
        //ordenCobro.setPago(pago);
        ordenCobro.setEstado(EstadoOrdenCobro.COBRADA.getEstado());
        ordenCobroRepository.save(ordenCobro);

        List<String> resultado = new ArrayList<>();
        resultado.add(facturaCita.getNumeroFactura());
        resultado.add(citaMedica.getCodigo());

        return resultado;
    }

    @Override
    public boolean existeCitaMedicaPorCodigo(String codigo) {
        return citaMedicaRepository.findByCodigo(codigo) != null;
    }

    @Override
    public CitaMedica actualizarCitaMedica(ActualizarCitaTO actualizarCitaTO) {

        CitaMedica citaMedica = citaMedicaRepository.findByCodigo(actualizarCitaTO.getCodigoCita());
        Integer idDoctor = citaMedica.getDoctor().getId();
        String nombreAuditor = null;
        String cedulaAuditor = null;
        if (actualizarCitaTO.getCedulaAuditor().equals("System")) {
            nombreAuditor = "System";
            cedulaAuditor = "System";
        } else {
            Usuario usuario = usuarioRepository.findByCedula(actualizarCitaTO.getCedulaAuditor());
            nombreAuditor = usuario.getNombre() + " " + usuario.getApellido();
            cedulaAuditor = usuario.getCedula();
            
        }

     
        LocalDateTime nuevaFecha = actualizarCitaTO.getDiaCita().atStartOfDay();
        List<DoctorDatosDTO> posiblesCitas = doctorService.filtrarDoctoresSinCita(
                citaMedica.getEspecialidad().getNombre(),
                nuevaFecha, citaMedica.getHoraInicio());

        DoctorDatosDTO doctorEncontrado = null;

        if (posiblesCitas.isEmpty() || posiblesCitas == null) {
            return null;
        } else {
            for (DoctorDatosDTO doctor : posiblesCitas) {
                if (doctor.getIdDoctor().equals(idDoctor)) {
                    doctorEncontrado = doctor;
                    break; // Rompe el ciclo una vez que encuentres el doctor correspondiente
                }
            }
        }

        citaMedica.setEstado(EstadoCita.REAGENDADA.getEstado());
        citaMedica.setCedulaAuditorModificador(nombreAuditor);
        citaMedica.setCedulaAuditorModificador(cedulaAuditor);
        citaMedica.setFechaModificacion(LocalDateTime.now());
        citaMedica.setFechaCita(nuevaFecha);
        @SuppressWarnings("null")
        Integer tiempoConsulta = doctorEncontrado.getTiempoConsulta();
        citaMedica.setHoraInicio(actualizarCitaTO.getHoraCita());
        citaMedica.setHoraFin(actualizarCitaTO.getHoraCita().plusMinutes(tiempoConsulta));
        return citaMedicaRepository.save(citaMedica);
    }

    @Override
    public ActualizarCitaTO convertirCitaMedicaDTOAAgendarCitaTO(CitaDTO citaAgendadaDTO,
            String cedulaAuditor) {
        ActualizarCitaTO actualizarCitaTO = new ActualizarCitaTO();
        actualizarCitaTO.setCodigoCita(citaAgendadaDTO.getCodigo());
        actualizarCitaTO.setCedulaAuditor(cedulaAuditor);

        return actualizarCitaTO;
    }

    @Override
    public List<CitaDTO> buscarCitasPorCedulaDoctorParaDoctor(String cedulaDoctor) {
        return citaMedicaRepository.buscarCitasPorCedulaDoctor(cedulaDoctor);
    }

    @Override
    public List<CitaDTO> buscarCitasParaCoctorPorCedulaPaciente(String cedulaPaciente,
            String cedulaDoctor) {
        return citaMedicaRepository.buscarCitasPorCedulaPacienteYCedulaDoctor(cedulaPaciente, cedulaDoctor);
    }

    @Override
    public List<CitaDTO> buscarCitasPorFechaYCedulaDoctor(LocalDateTime fecha, String cedulaDoctor) {
        return citaMedicaRepository.buscarCitasPorFechaYCedulaDoctor(fecha, cedulaDoctor);
    }

    @Override
    public CitaMedica cancelarClinica(String codigoCita) {
        CitaMedica citaMedica = citaMedicaRepository.findByCodigo(codigoCita);
        citaMedica.setEstado(EstadoCita.CANCELADA.getEstado());
        return citaMedicaRepository.save(citaMedica);
    }

    @Override
    public List<CitaDTO> buscarCitasPorCedulaPaciente(String cedulaPaciente) {
        return citaMedicaRepository.buscarCitasPorCedulaPaciente(cedulaPaciente);
    }

    @Override
    public List<CitaDTO> buscarCitasPorFecha(LocalDate fecha) {
        return citaMedicaRepository.buscarCitasPorFecha(fecha.atStartOfDay());
    }

    @Override
    public List<CitaDTO> todasLasCitasDelSistema() {
        return citaMedicaRepository.buscarTodasLasCitas();
    }

    @Override
    public DatosCitaAgendar crearCitaAgendar(BuscarCitaTO buscarCitaTO, DoctorDatosDTO doctorDatosDTO) {
        DatosCitaAgendar datosCitaAgendar = DatosCitaAgendar.builder()
                .nombreDoctor(doctorDatosDTO.getNombreDoctor() + " " + doctorDatosDTO.getApellidoDoctor())
                .consultorioCodigo(doctorDatosDTO.getConsultorioCodigo())
                .tiempoConsulta(doctorDatosDTO.getTiempoConsulta())
                .especialidad(doctorDatosDTO.getEspecialidad())
                .fechaCita(buscarCitaTO.getDiaCita().atTime(buscarCitaTO.getHoraCita()))
                .horaInicio(buscarCitaTO.getHoraCita())
                .horaFin(buscarCitaTO.getHoraCita().plusMinutes(doctorDatosDTO.getTiempoConsulta()))
                .build();
        return datosCitaAgendar;
    }

    @Override
    public Boolean enviarOrdenDeCobroAlCorreoPaciente(Paciente paciente, OrdenCobro ordenCobro) {
        // String to = "edlithvinueza@gmail.com"; // solo para pruebas
        String to = ordenCobro.getPaciente().getCorreo();
        if (to == null) {
            return false;
        }
        String subject = "Orden de Cobro - H.E.S";
        String text = "Hola: " + ordenCobro.getPaciente().getNombre() + " " + ordenCobro.getPaciente().getApellido()
                + ". Adjunto a este correo encontrará tu orden de cobro. \n Recuerda que solo sera valida hasta: "
                + ordenCobro.getFechaLimitePago();
        String filename = "filename=" + "orden_cobro_" + LocalDateTime.now() + ".pdf";
        OrdenCobroPDF ordenCobroPDF = new OrdenCobroPDF();
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = ordenCobroPDF.generarPdfOrdenCobro(ordenCobro);
            emailService.sendEmailWithPdf(to, subject, text, byteArrayOutputStream, filename);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean enviarFacturaYCitaAlCorreoPaciente(String cedulaPaciente, String facturaCitaNumero,
            String citaMedicaCodigo) {
        Paciente paciente = pacienteRepository.findByCedula(cedulaPaciente);
        FacturaCita factura = facturaCitaRepository.findByNumeroFactura(facturaCitaNumero);
        CitaMedica cita = citaMedicaRepository.findByCodigo(citaMedicaCodigo);

        if (paciente == null || factura == null || cita == null) {
            return false;
        }
        // String to = "edlithvinueza@gmail.com"; // solo para pruebas
        String to = paciente.getCorreo();
        if (to == null) {
            return false;
        }
        String subject = "Factura Electronica y Cita Médica - H.E.S";
        String text = "Hola: " + paciente.getNombre() + " " + paciente.getApellido()
                + ". Adjunto a este correo encontrará tu Factura y los datos de tu cita. \n Recuerda que debes estar media hora antes con tus documentos personales";
        String filename1 = "factura_" + facturaCitaNumero + ".pdf";
        String filename2 = "cita_medica_" + citaMedicaCodigo + ".pdf";

        FacturaCitaPDF facturaCitaPDF = new FacturaCitaPDF();
        ByteArrayOutputStream byteArrayOutputStreamFactura = null;
        CitaMedicaPDF citaMedicaPDF = new CitaMedicaPDF();
        ByteArrayOutputStream byteArrayOutputStreamCita = null;

        try {
            byteArrayOutputStreamFactura = facturaCitaPDF.generarPdfFacturaCita(factura);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            byteArrayOutputStreamCita = citaMedicaPDF.generarPdfCitaMedica(cita);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (byteArrayOutputStreamFactura != null && byteArrayOutputStreamCita != null) {
                emailService.sendEmailWithPdfBoolean(to, subject, text, byteArrayOutputStreamFactura, filename1,
                        byteArrayOutputStreamCita, filename2);
            } else if (byteArrayOutputStreamFactura != null) {
                subject = "Factura Electronica- H.E.S";
                text = "Hola: " + paciente.getNombre() + " " + paciente.getApellido()
                        + ". Adjunto a este correo los datos de tu cita. \n Recuerda que debes estar media hora antes con tus documentos personales";
                emailService.sendEmailWithPdf(to, subject, text, byteArrayOutputStreamFactura, filename1);
            } else if (byteArrayOutputStreamCita != null) {
                subject = "Cita Médica - H.E.S";
                text = "Hola: " + paciente.getNombre() + " " + paciente.getApellido()
                        + ". Adjunto a este correo encontrará tu Factura. \n Recuerda que debes estar media hora antes con tus documentos personales";
                emailService.sendEmailWithPdf(to, subject, text, byteArrayOutputStreamCita, filename2);
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public CitaMedica buscarCitaMedicaPorCodigo(String codigo) {
        return citaMedicaRepository.findByCodigo(codigo);
    }

    @Override
    public FacturaCita buscarFacturaCitaPorNumero(String numero) {
        return facturaCitaRepository.findByNumeroFactura(numero);
    }

    @Override
    public CitaMedica buscarCitaMedicaPorId(Integer id) {
        return citaMedicaRepository.findById(id).orElse(null);
    }

    @Override
    public List<CitaDTO> buscarCitasPorFechaYCedulaPaciente(LocalDate fecha, String cedulaPaciente) {
       return citaMedicaRepository.buscarCitasPorFechaYCedulaPaciente(fecha.atStartOfDay(), cedulaPaciente);
     }

}
