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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cita_medica")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita_medica")
    @SequenceGenerator(name = "seq_cita_medica", sequenceName = "seq_cita_medica", allocationSize = 1)
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

    @OneToOne(mappedBy = "cita_medica")
    private HistoriaClinica historiaClinica;
/* 
    @OneToOne
    @JoinColumn(name = "orden_cobro_id", nullable = false)
    private OrdenCobro ordenCobro;
*/
    @Column(name = "cita__medi_fecha", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "cita__medi_hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "cita__medi_hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "cita__medi_estado", nullable = false)
    private String estado;

    @Column(name = "cita__medi__nombre_auditor_emisor", nullable = false)
    private String nombreAuditorEmisor;
    
    @Column(name = "cita__medi_cedula_auditor_emisor", nullable = false)
    private String cedulaeAuditorEmisor;

    @Column(name = "cita__medi_nombre_auditor_modificador", nullable = true)
    private String nombreAuditorModificador;

    @Column(name = "cita__medi_cedula_auditor_modificador", nullable = true)
    private String cedulaAuditorModificador;

    @Column(name = "cita__medi_fecha_creacion", nullable = true)
    private LocalDateTime fechaModificacion;

    @Column(name = "cita__medi_observaciones", nullable = true, length = 255)
    private String observacion;

    @Column(name = "cita__medi_codigo", nullable = false)
    private String codigo;

}