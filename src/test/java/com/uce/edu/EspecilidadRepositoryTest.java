package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IEspecialiadRepository;
import com.uce.edu.repository.modelo.Especialidad;

/**
 * EspecialidadRepositoryTest
 
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class EspecilidadRepositoryTest {

    /**
     * Dependencias
     */
    @Autowired
    private IEspecialiadRepository especialidadRepository;

    /**
     * Método que permite probar la creación de una especialidad
     */
    @Test
    public void testCrearEspecialidad() {
        // Create a new Especialidad object
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre("Cardiología");
        especialidad.setDescripcion("Especialidad que se encarga de tratar enfermedades del corazón");
        especialidad.setPrecio(new BigDecimal(50.0));

        // Save the especialidad using the repository
        Especialidad savedEspecialidad = especialidadRepository.save(especialidad);

        // Verify that the especialidad was saved successfully
        assertNotNull(savedEspecialidad);
        assertNotNull(savedEspecialidad.getId());
        assertEquals("Cardiología", savedEspecialidad.getNombre());
    }

    /**
     * Método que permite probar la creación de varias especialidades
     */
    @Test
    public void testCrearVariasEspecialidades() {
        // Lista de especialidades a crear
        List<Especialidad> especialidades = Arrays.asList(

                new Especialidad("Dermatología",
                        "Especialidad médica encargada del estudio de la piel y sus enfermedades",
                        new BigDecimal("70.0")),
                new Especialidad("Pediatría", "Especialidad médica que se encarga de los niños y sus enfermedades",
                        new BigDecimal("60.0")),
                new Especialidad("Neurología", "Especialidad médica que trata los trastornos del sistema nervioso",
                        new BigDecimal("80.0")));

        // Iterar sobre la lista de especialidades, guardar cada una y verificar
        for (Especialidad especialidad : especialidades) {
            // Save the especialidad using the repository
            Especialidad savedEspecialidad = especialidadRepository.save(especialidad);

            // Verify that the especialidad was saved successfully
            assertNotNull(savedEspecialidad);
            assertNotNull(savedEspecialidad.getId());
            assertEquals(especialidad.getNombre(), savedEspecialidad.getNombre());
        }

    }

    /**
     * Método que permite probar la búsqueda de una especialidad por nombre
     */
    @Test
    public void testBuscarEspecialidadPorNombre() {

        // Search for the especialidad by name
        Especialidad foundEspecialidad = especialidadRepository.findByNombre("Cardiología");

        // Verify that the especialidad was found successfully
        assertNotNull(foundEspecialidad);
        assertEquals("Cardiología", foundEspecialidad.getNombre());
    }

}
