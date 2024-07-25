package com.uce.edu.repository.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "pregunta_recuperacion")
public class PreguntaRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pregunta_recuperacion")
    @SequenceGenerator(name = "seq_pregunta_recuperacion", sequenceName = "seq_pregunta_recuperacion", allocationSize = 1)
    private Integer id;

    /**
     * Una pregunta de recuperación tiene muchas respuestas
     */
    @OneToMany(mappedBy = "preguntaRecuperacion")
    private List<RespuestaPregunta> respuestasPregunta;

    @NotBlank(message = "La pregunta de recuperación es obligatoria")
    @Size(min = 5, max = 150, message = "La pregunta de recuperación debe tener entre 5 y 150 caracteres")
    @Column(name = "preg_recuperacion_pregunta", length = 150, nullable = false, unique = true)
    private String pregunta;
   
    public PreguntaRecuperacion(Integer id, String pregunta) {
        this.id = id;
        this.pregunta = pregunta;
    }

    public PreguntaRecuperacion(String pregunta) {
        this.pregunta = pregunta;
    }

    public PreguntaRecuperacion() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<RespuestaPregunta> getRespuestasPregunta() {
        return respuestasPregunta;
    }

    public void setRespuestasPregunta(List<RespuestaPregunta> respuestasPregunta) {
        this.respuestasPregunta = respuestasPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    

    
 

    



}
