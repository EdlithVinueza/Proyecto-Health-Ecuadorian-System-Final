package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IPreguntaRecuperacionRepository;
import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.service.interfacesSistemaPrincipal.IPreguntaRecuperacionService;
import com.uce.edu.service.to.PreguntasTO;

@Service
public class PreguntaRecuperacionServiceImpl implements IPreguntaRecuperacionService {

    @Autowired
    private IPreguntaRecuperacionRepository preguntaRecuperacionRepository;

    @Override
    public PreguntaRecuperacion buscarUnPorPregunta(String pregunta) {

        return preguntaRecuperacionRepository.findByPregunta(pregunta);
    }

    @Override
    public List<PreguntaRecuperacion> buscarTodasLasPreguntas() {
        return preguntaRecuperacionRepository.findAll(); // Implementación del método para obtener todas las preguntas
    }

    @Override
    public boolean guardarPregunta(PreguntaRecuperacion preguntaRecuperacion) {
        try {
            preguntaRecuperacionRepository.save(preguntaRecuperacion);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Retorna null en lugar de lanzar una excepción
            return false;
        }

    }

    @Override
    public PreguntaRecuperacion obtenerPreguntaPorId(Integer id) {
        return preguntaRecuperacionRepository.findById(id).get();
    }

    @Override
    public PreguntaRecuperacion actualizarPregunta(PreguntaRecuperacion preguntaRecuperacion) {
        return preguntaRecuperacionRepository.save(preguntaRecuperacion);
    }

    @Override
    public void eliminarPreguntaPorId(Integer id) {
        preguntaRecuperacionRepository.deleteById(id);
    }

    @Override
    public PreguntasTO obtenerTresPreguntasAleatorias() {
        // Obtener todas las preguntas
        List<PreguntaRecuperacion> todasLasPreguntas = buscarTodasLasPreguntas();
        // Mezclar la lista de preguntas
        Collections.shuffle(todasLasPreguntas);
        // Crear un nuevo objeto PreguntasTO y guardar las primeras tres preguntas
        PreguntasTO preguntasTO = new PreguntasTO();
        if (!todasLasPreguntas.isEmpty()) {
            preguntasTO.setPreguntaUno(todasLasPreguntas.get(0).getPregunta());
            if (todasLasPreguntas.size() > 1) {
                preguntasTO.setPreguntaDos(todasLasPreguntas.get(1).getPregunta());
                if (todasLasPreguntas.size() > 2) {
                    preguntasTO.setPreguntaTres(todasLasPreguntas.get(2).getPregunta());
                }
            }
        }
        return preguntasTO;
    }

}
