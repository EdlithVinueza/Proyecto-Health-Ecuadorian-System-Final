package com.uce.edu.service.interfacesSistemaPrincipal;

import java.util.List;


import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.service.to.PreguntasTO;
/**
 * IPreguntaRecuperacionService
 */
public interface IPreguntaRecuperacionService {

    /**
     * Método para buscar una pregunta por pregunta
     * @param pregunta
     * @return
     */
    PreguntaRecuperacion buscarUnPorPregunta(String pregunta);

    /**
     * Método para buscar todas las preguntas
     * @return
     */
    List<PreguntaRecuperacion> buscarTodasLasPreguntas();

    /**
     * Método para guardar una pregunta preguntas de recuperacion 
     * @param preguntaRecuperacion
     * @return
     */
    boolean guardarPregunta(PreguntaRecuperacion preguntaRecuperacion);

    /**
     * Método para obtener una pregunta por id
     * @param id
     * @return
     */
    PreguntaRecuperacion obtenerPreguntaPorId(Integer id);

    /**
     * Método para actualizar una pregunta
     * @param preguntaRecuperacion
     * @return
     */
    PreguntaRecuperacion actualizarPregunta(PreguntaRecuperacion preguntaRecuperacion);

    /**
     * Método para eliminar una pregunta por id
     * @param id
     */
    void eliminarPreguntaPorId(Integer id);

    /**
     * Método para obtener tres preguntas aleatorias
     * @return
     */
    public PreguntasTO obtenerTresPreguntasAleatorias();
}
