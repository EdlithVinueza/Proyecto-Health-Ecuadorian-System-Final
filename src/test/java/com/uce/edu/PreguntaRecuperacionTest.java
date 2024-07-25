package com.uce.edu;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IPreguntaRecuperacionRepository;
import com.uce.edu.repository.modelo.PreguntaRecuperacion;
/**
 * PreguntaRecuperacionTest
 
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class PreguntaRecuperacionTest {
/**
 * Dependencias
 */
    @Autowired
    private IPreguntaRecuperacionRepository preguntaRecuperacionRepository;
    /**
     * Método que permite probar la creación de una pregunta de recuperación
     */
    @Test
    public void testInsertPreguntas() {
        // Create three PreguntaRecuperacion objects
        PreguntaRecuperacion pregunta1 = new PreguntaRecuperacion("¿Cuál es tu color favorito?");
        PreguntaRecuperacion pregunta2 = new PreguntaRecuperacion("¿Cuál es tu comida favorita?");
        PreguntaRecuperacion pregunta3 = new PreguntaRecuperacion("¿Como se llama tu madre?");

        // Save the PreguntaRecuperacion objects to the repository
        preguntaRecuperacionRepository.save(pregunta1);
        preguntaRecuperacionRepository.save(pregunta2);
        preguntaRecuperacionRepository.save(pregunta3);

        // Assert that the PreguntaRecuperacion objects were successfully inserted
        assertNotNull(pregunta1.getId());
        assertNotNull(pregunta2.getId());
        assertNotNull(pregunta3.getId());
    }







}
