package com.uce.edu.service.serviciosSistemaGeneral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IRespuestaPreguntaRepository;
import com.uce.edu.repository.modelo.RespuestaPregunta;
import com.uce.edu.service.interfacesSistemaPrincipal.IRespuestPreguntaService;
@Service
public class RespuestPreguntaServiceImpl implements IRespuestPreguntaService{

    @Autowired
    private IRespuestaPreguntaRepository respuestaPreguntaRepository;

    @Override
    public RespuestaPregunta actualizarRespuesta(RespuestaPregunta respuestaPregunta) {
       return respuestaPreguntaRepository.save(respuestaPregunta);
    }

}
