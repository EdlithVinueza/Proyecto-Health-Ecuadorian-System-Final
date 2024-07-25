package com.uce.edu.repository.modelo;

import java.time.LocalDateTime;

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
@Table(name = "historia_clinica")
public class HistoriaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_historia_clinica")
    @SequenceGenerator(name = "seq_historia_clinica", sequenceName = "seq_historia_clinica", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "cita_medica_id")
    private CitaMedica cita_medica;

    @OneToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @Column(name = "hist_clin_fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "hist_clin_diagnostico", nullable = false)
    private String diagnostico;

    @Column(name = "hist_clin_tratamiento", nullable = false)
    private String tratamiento;

    @Column(name = "hist_clin_examenes", nullable = false)
    private String examenes;

    @Column(name = "hist_clin_resultados", nullable = false)
    private String resultados;

    @Column(name = "hist_clin_receta_medica", nullable = false, length = 255)
    private String recetaMedica;

    @Column(name = "hist_clin_medicamentos", nullable = false, length = 255)
    private String medicamentos;

    @Column(name = "hist_clin_tratamieno", nullable = false, length = 255)
     private String tratamieno;

     @Column(name = "hist_clin_observacion", nullable = false)
     private String observacion;

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

    public CitaMedica getCita_medica() {
        return cita_medica;
    }

    public void setCita_medica(CitaMedica cita_medica) {
        this.cita_medica = cita_medica;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getExamenes() {
        return examenes;
    }

    public void setExamenes(String examenes) {
        this.examenes = examenes;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }

    public String getRecetaMedica() {
        return recetaMedica;
    }

    public void setRecetaMedica(String recetaMedica) {
        this.recetaMedica = recetaMedica;
    }

    public String getMedicamentos() {
        return medicamentos;
    }

    public void setMedicamentos(String medicamentos) {
        this.medicamentos = medicamentos;
    }

    public String getTratamieno() {
        return tratamieno;
    }

    public void setTratamieno(String tratamieno) {
        this.tratamieno = tratamieno;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    

}

//
//
