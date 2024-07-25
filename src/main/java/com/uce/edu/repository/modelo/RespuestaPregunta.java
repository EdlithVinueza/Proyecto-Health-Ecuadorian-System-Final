package com.uce.edu.repository.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "respuesta_pregrunta")
public class RespuestaPregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pregunta_recuperacion")
    @SequenceGenerator(name = "seq_pregunta_recuperacion", sequenceName = "seq_pregunta_recuperacion", allocationSize = 1)
    private Integer id;

    /**
     * Una respuesta de pregunta tiene una pregunta de recuperación
     */
    @ManyToOne
    @JoinColumn(name = "pregunta_recuperacion_id", nullable = false)
    private PreguntaRecuperacion preguntaRecuperacion;

    /**
     * Una respuesta de pregunta tiene un usuario
     */

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    /**
     * Una respuesta de pregunta tiene un paciente
     * 
     */
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = true)
    private Paciente paciente;

    /**
     * Una respuesta de pregunta tiene un doctor
     */

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = true)
    private Doctor doctor;

    /**
     * La respuesta de la pregunta
     */
    @NotBlank(message = "La respuesta es obligatoria")
    @Size(min = 3, max = 50, message = "La respuesta debe tener entre 5 y 50 caracteres")
    @Column(name = "resp_preg_pregunta", length = 50, nullable = false)
    private String respuesta;

    public RespuestaPregunta( Usuario usuario, PreguntaRecuperacion preguntaRecuperacion, String respuesta) {
        this.preguntaRecuperacion = preguntaRecuperacion;
        this.usuario = usuario;
   
        this.respuesta = respuesta;
    }

    public RespuestaPregunta(PreguntaRecuperacion preguntaRecuperacion, String respuesta) {
        this.preguntaRecuperacion = preguntaRecuperacion;
        this.respuesta = respuesta;
    }

    public RespuestaPregunta(Integer id, String respuesta) {
        this.id = id;
        this.respuesta = respuesta;
    }

    public RespuestaPregunta(String respuesta) {
        this.respuesta = respuesta;
    }

    public RespuestaPregunta() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PreguntaRecuperacion getPreguntaRecuperacion() {
        return preguntaRecuperacion;
    }

    public void setPreguntaRecuperacion(PreguntaRecuperacion preguntaRecuperacion) {
        this.preguntaRecuperacion = preguntaRecuperacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    

}
