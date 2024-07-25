package com.uce.edu.repository.modelo;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "agenda_cita_temporal")
public class AgendaCitaTemporal {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agenda_cita_temporal")
  @SequenceGenerator(name = "seq_agenda_cita_temporal", sequenceName = "seq_agenda_cita_temporal", allocationSize = 1)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "doctor_id", nullable = false)
  private Doctor doctor;

  @ManyToOne
  @JoinColumn(name = "paciente_id", nullable = false)
  private Paciente paciente;

  @ManyToOne
  @JoinColumn(name = "consultorio_id", nullable = false)
  private Consultorio consultorio;

  @ManyToOne
  @JoinColumn(name = "especialidad_id", nullable = false)
  private Especialidad especialidad;

  @OneToOne
  @JoinColumn(name = "orden_cobro_id")
  private OrdenCobro ordenCobro;

  @Column(name = "agen_cita_hora_inicio", nullable = false)
  private LocalTime horaInicio;

  @Column(name = "agen_cita_hora_fin", nullable = false)
  private LocalTime horaFin;

  @Column(name = "agenda_cita_estado", nullable = false)
  private String estado;

  @Column(name = "agenda_cita_nombre_auditor_agendador", nullable = true, length = 60)
  private String nombreAuditorAgendador;

  @Column(name = "agenda_cita_cedula_auditor_agendador", nullable = true, length = 10)
  private String cedulaAuditorAgendador;

  @Column(name = "agenda_cita_fecha_cita", nullable = true)
  private LocalDateTime fechaCita;

  @Column(name = "agenda_cita_observaciones", nullable = true)
  private String observacion;

  public AgendaCitaTemporal() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public Paciente getPaciente() {
    return paciente;
  }

  public void setPaciente(Paciente paciente) {
    this.paciente = paciente;
  }

  public Consultorio getConsultorio() {
    return consultorio;
  }

  public void setConsultorio(Consultorio consultorio) {
    this.consultorio = consultorio;
  }

  public Especialidad getEspecialidad() {
    return especialidad;
  }

  public void setEspecialidad(Especialidad especialidad) {
    this.especialidad = especialidad;
  }

  public OrdenCobro getOrdenCobro() {
    return ordenCobro;
  }

  public void setOrdenCobro(OrdenCobro ordenCobro) {
    this.ordenCobro = ordenCobro;
  }

  public LocalTime getHoraInicio() {
    return horaInicio;
  }

  public void setHoraInicio(LocalTime horaInicio) {
    this.horaInicio = horaInicio;
  }

  public LocalTime getHoraFin() {
    return horaFin;
  }

  public void setHoraFin(LocalTime horaFin) {
    this.horaFin = horaFin;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  
  public String getNombreAuditorAgendador() {
    return nombreAuditorAgendador;
  }

  public void setNombreAuditorAgendador(String nombreAuditorAgendador) {
    this.nombreAuditorAgendador = nombreAuditorAgendador;
  }

  public String getCedulaAuditorAgendador() {
    return cedulaAuditorAgendador;
  }

  public void setCedulaAuditorAgendador(String cedulaAuditorAgendador) {
    this.cedulaAuditorAgendador = cedulaAuditorAgendador;
  }


  public String getObservacion() {
    return observacion;
  }

  public void setObservacion(String observacion) {
    this.observacion = observacion;
  }

  public LocalDateTime getFechaCita() {
    return fechaCita;
  }

  public void setFechaCita(LocalDateTime fechaCita) {
    this.fechaCita = fechaCita;
  }

}