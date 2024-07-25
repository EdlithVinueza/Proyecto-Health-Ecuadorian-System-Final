package com.uce.edu.repository.interfacesSistemaGeneral;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uce.edu.repository.modelo.RespuestaPregunta;

public interface IRespuestaPreguntaRepository extends JpaRepository<RespuestaPregunta, Integer>{

    void deleteByRespuesta(String respuesta);

    

   
}
