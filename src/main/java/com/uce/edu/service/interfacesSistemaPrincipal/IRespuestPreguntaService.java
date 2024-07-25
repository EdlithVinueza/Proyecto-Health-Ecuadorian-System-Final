package com.uce.edu.service.interfacesSistemaPrincipal;

import com.uce.edu.repository.modelo.RespuestaPregunta;

/**
 * IRespuestPreguntaService
 */
public interface IRespuestPreguntaService {
    /**
     * Método para actualizar una respuesta
     * 
     * @param respuestaPregunta
     * @return
     */
    RespuestaPregunta actualizarRespuesta(RespuestaPregunta respuestaPregunta);

}
