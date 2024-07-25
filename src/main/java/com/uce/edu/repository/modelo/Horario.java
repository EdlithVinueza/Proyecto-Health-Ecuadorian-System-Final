package com.uce.edu.repository.modelo;

import java.time.DayOfWeek;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "horario")
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_horario")
    @SequenceGenerator(name = "seq_horario", sequenceName = "seq_horario", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "consultorio_id")
    private Consultorio consultorio;

    @Column(name = "dia_semana", nullable = false)
    @Enumerated(EnumType.STRING) // Esto indica que el enumerado se debe almacenar como su nombre
    private DayOfWeek dia;

    @NotNull(message = "La hora de inicio es obligatoria")
    @Column(name = "hora_hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @Column(name = "hora_hora_fin", nullable = false)
    private LocalTime horaFin;

    @NotNull(message = "La duración de la cita es obligatoria")
    @Column(name = "hora_duracion_cita", nullable = false)
    private Integer duracionCita;



    public Horario() {
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

    public Consultorio getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(Consultorio consultorio) {
        this.consultorio = consultorio;
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

    public Integer getDuracionCita() {
        return duracionCita;
    }

    public void setDuracionCita(Integer duracionCita) {
        this.duracionCita = duracionCita;
    }

    public DayOfWeek getDia() {
        return dia;
    }

    public void setDia(DayOfWeek dia) {
        this.dia = dia;
    }

}
