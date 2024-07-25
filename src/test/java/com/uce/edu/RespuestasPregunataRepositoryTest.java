package com.uce.edu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IRespuestaPreguntaRepository;

/**
 * RespuestasPregunataRepositoryTest
 * 
 * 
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RespuestasPregunataRepositoryTest {
    /**
     * Dependencias
     */
    @Autowired
    private IRespuestaPreguntaRepository respuestaPreguntaRepository;

    /**
     * Método para borrar respuestas vacias
     */
    @Test
    @Rollback(true)
    public void borrarVacios() {
        String borrar = "vacio";
        respuestaPreguntaRepository.deleteByRespuesta(borrar);
    }

}
