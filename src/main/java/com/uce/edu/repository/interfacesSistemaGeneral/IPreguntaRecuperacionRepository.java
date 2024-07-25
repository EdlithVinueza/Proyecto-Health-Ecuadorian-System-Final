package com.uce.edu.repository.interfacesSistemaGeneral;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uce.edu.repository.modelo.PreguntaRecuperacion;

public interface IPreguntaRecuperacionRepository extends JpaRepository<PreguntaRecuperacion, Integer>{

    PreguntaRecuperacion findByPregunta(String pregunta);
}
