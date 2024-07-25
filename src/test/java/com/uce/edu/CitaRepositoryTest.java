package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.ICitaMedicaRepository;
import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.serviciosSistemaGeneral.DoctorServiceImpl;

/**
 * CitaRepositoryTest
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CitaRepositoryTest {
    /**
     * Dependencias
     */
    @Autowired
    private ICitaMedicaRepository citaRepository;

    @Autowired
    private DoctorServiceImpl doctorService;

    /**
     * Método que permite probar la búsqueda de citas médicas por hora de inicio,
     * hora de fin, fecha y doctor
     */
    @SuppressWarnings("unused")
    @Test
    public void testBuscarPorHoraInicioFinFechaYDoctor() {
        // Define test parameters
        LocalTime horaInicio = LocalTime.of(9, 0);
        LocalTime horaFin = LocalTime.of(10, 0);
        LocalDateTime fecha = LocalDateTime.of(2023, 4, 20, 0, 0);
        String cedulaDoctor = "1234567890";

        // Execute the method to test
        List<CitaMedica> result = citaRepository.buscarPorHoraInicioFinFechaYDoctor(horaInicio, horaFin, fecha,
                cedulaDoctor);

        // Assertions
        assertNotNull(result, "The result should not be null");
        // Additional assertions can be made based on expected results, such as checking
        // if the list contains expected elements
        for (CitaMedica cita : result) {
            // System.out.println("Cita escogido: " + cita.getDoctor());
        }

    }

    /**
     * Método que permite probar la búsqueda de citas médicas por hora de inicio,
     * hora de fin, fecha y doctor
     */
    @Test
    public void testFiltrarDoctoresSinCita() {
        // Datos de prueba
        String especialidad = "Cardiología";
        LocalDateTime fecha = LocalDateTime.of(2023, 10, 10, 10, 0);
        LocalTime hora = LocalTime.of(10, 0);

        // Ejecutar el método
        List<DoctorDatosDTO> resultado = doctorService.filtrarDoctoresSinCita(especialidad, fecha, hora);

        // Verificar el resultado
        assertEquals(0, resultado.size());
        System.out.println("Numero de Citas --->" + resultado.size());
    }

}
