package com.uce.edu.service.interfacesSistemaPrincipal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.FacturaCita;
import com.uce.edu.repository.modelo.OrdenCobro;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.dto.CitaDTO;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.to.ActualizarCitaTO;
import com.uce.edu.service.to.AgendarCitaTO;
import com.uce.edu.service.to.BuscarCitaTO;
import com.uce.edu.service.to.DatosCitaAgendar;

/**
 * ICitaMedicaService
 */
public interface ICitaMedicaService {
  /**
   * Método para buscar citas médicas
   * 
   * @param especialidad
   * @param doctor
   * @param Fecha
   * @param hora
   * @return
   */
  public boolean buscarCitaMedica(Especialidad especialidad, Doctor doctor, LocalDateTime Fecha, LocalTime hora);

  /**
   * Método para crear Orden de Cobro de una cita médica pendietne
   * 
   * @param agendarCitaTO
   * @param doctorDatosDTO
   * @param buscarCitaTO
   * @return
   */
  public OrdenCobro crearOrdenCobro(AgendarCitaTO agendarCitaTO, DoctorDatosDTO doctorDatosDTO,
      BuscarCitaTO buscarCitaTO);

  /**
   * Método para una fecha válida
   * 
   * @param diaCita
   * @return
   */

  boolean esFechaValida(LocalDate diaCita);

  /**
   * Método para validar la fecha y hora de una cita
   * 
   * @param diaCita
   * @param horaCita
   * @return
   */
  boolean esHoraValidaSiFechaEsHoy(LocalDate diaCita, LocalTime horaCita);

  /**
   * Método para generar una factura y una cita médica con pago en efectivo
   * 
   * @param paciente
   * @param usuario
   * @param ordenCobro
   * @return
   */

  public List<String> generarFacturaYCitaPagoEfectivo(Paciente paciente, Usuario usuario, OrdenCobro ordenCobro);

  /**
   * Método para generar una factura y una cita médica con pago en tarjeta
   * 
   * @param paciente
   * @param usuario
   * @param ordenCobro
   * @return
   */
  public List<String> generarFacturaYCitaPagoTransferencia(Paciente paciente, Usuario usuario, OrdenCobro ordenCobro);

  /**
   * Método para validar que una cita médica exista por código
   * 
   * @param codigo
   * @return
   */
  public boolean existeCitaMedicaPorCodigo(String codigo);

  /**
   * Método para actualziar (reagendar) una cita médica
   * 
   * @param actualizarCitaTO
   * @return
   */

  public CitaMedica actualizarCitaMedica(ActualizarCitaTO actualizarCitaTO);

  /**
   * Método para convertir una cita médica DTO a AgendarCitaTO
   * 
   * @param citaAgendadaDTO
   * @param cedulaAuditor
   * @return
   */
  public ActualizarCitaTO convertirCitaMedicaDTOAAgendarCitaTO(CitaDTO citaAgendadaDTO, String cedulaAuditor);

  /**
   * Método para buscar citas por cédula de doctor
   * 
   * @param cedulaDoctor
   * @return
   */
  public List<CitaDTO> buscarCitasPorCedulaDoctorParaDoctor(String cedulaDoctor);

  /*
   * Método para buscar citas por cédula de doctor
   */
  public List<CitaDTO> buscarCitasParaCoctorPorCedulaPaciente(String cedulaPaciente, String cedulaDoctor);

  /**
   * Método para buscar citas por fecha y cédula de doctor
   * 
   * @param fecha
   * @param cedulaDoctor
   * @return
   */
  public List<CitaDTO> buscarCitasPorFechaYCedulaDoctor(LocalDateTime fecha, String cedulaDoctor);

  /**
   * Método para cancelar una cita médica
   * 
   * @param codigoCita
   * @return
   */
  public CitaMedica cancelarClinica(String codigoCita);

  /**
   * Método para buscar citas por cédula de paciente
   * 
   * @param cedulaPaciente
   * @return
   */
  public List<CitaDTO> buscarCitasPorCedulaPaciente(String cedulaPaciente);

  /**
   * Método para buscar citas por fecha
   * 
   * @param fecha
   * @return
   */
  public List<CitaDTO> buscarCitasPorFecha(LocalDate fecha);

  /**
   * Método para taer todas las citas del sistema
   * 
   * @return
   */
  public List<CitaDTO> todasLasCitasDelSistema();

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param buscarCitaTO
   * @param doctorDatosDTO
   * @return
   */
  public DatosCitaAgendar crearCitaAgendar(BuscarCitaTO buscarCitaTO, DoctorDatosDTO doctorDatosDTO);

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param paciente
   * @param ordenCobro
   * @return
   */
  public Boolean enviarOrdenDeCobroAlCorreoPaciente(Paciente paciente, OrdenCobro ordenCobro);

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param cedulaPaciente
   * @param facturaCitaNumero
   * @param citaMedicaCodigo
   * @return
   */
  public Boolean enviarFacturaYCitaAlCorreoPaciente(String cedulaPaciente, String facturaCitaNumero,
      String citaMedicaCodigo);

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param codigo
   * @return
   */
  public CitaMedica buscarCitaMedicaPorCodigo(String codigo);

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param numero
   * @return
   */
  public FacturaCita buscarFacturaCitaPorNumero(String numero);

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param id
   * @return
   */
  public CitaMedica buscarCitaMedicaPorId(Integer id);

  /**
   * Método para buscar citas por fecha y cédula de paciente
   * 
   * @param fecha
   * @param cedulaPaciente
   * @return
   */
  public List<CitaDTO> buscarCitasPorFechaYCedulaPaciente(LocalDate fecha, String cedulaPaciente);

}
