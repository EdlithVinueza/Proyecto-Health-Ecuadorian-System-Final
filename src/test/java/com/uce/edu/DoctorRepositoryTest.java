package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IEspecialiadRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IPreguntaRecuperacionRepository;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.repository.modelo.RespuestaPregunta;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IDoctorService;

/**
 * DoctorRepositoryTest
 
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class DoctorRepositoryTest {

    /**
     * Dependencias
     */
    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private IEspecialiadRepository especialidadRepository;

    @Autowired
    private IPreguntaRecuperacionRepository preguntaRecuperacionRepository;

/**
 * Test para agregar un doctor
 */
    @Test
    void testAddDoctor() {
        Especialidad especialidad = especialidadRepository.findByNombre("Cardiología");
        Doctor doctor = new Doctor(null, null, especialidad, null, null, null, null, "UwU:3", "Perez", "1751644777", "uwuperez@gmail.com",
                "2544063", "Zaparos uwu", LocalDate.of(2024, 5, 31), "Trabajando pues", "Contraseña", null, null);

        PreguntaRecuperacion pregunta1 = preguntaRecuperacionRepository.findByPregunta("¿Cuál es tu color favorito?");
        PreguntaRecuperacion pregunta2 = preguntaRecuperacionRepository.findByPregunta("¿Cuál es tu comida favorita?");
        PreguntaRecuperacion pregunta3 = preguntaRecuperacionRepository.findByPregunta("¿Como se llama tu madre?");

        assertNotNull(pregunta1);
        assertNotNull(pregunta2);
        assertNotNull(pregunta3);

        RespuestaPregunta respuesta1 = new RespuestaPregunta(pregunta1, "Azul");
        RespuestaPregunta respuesta2 = new RespuestaPregunta(pregunta2, "Pizza");
        RespuestaPregunta respuesta3 = new RespuestaPregunta(pregunta3, "Maria");

        List<RespuestaPregunta> respuestas = new ArrayList<>();

        respuestas.add(respuesta1);
        respuestas.add(respuesta2);
        respuestas.add(respuesta3);

        doctor.setRespuestasPregunta(respuestas);

        Doctor doctorSave = doctorRepository.save(doctor);
        assertNotNull(doctorSave.getId());

        // Aquí puedes hacer afirmaciones para verificar que el doctor se agregó
        // correctamente.
        // Por ejemplo, podrías buscar el doctor en el servicio y verificar que los
        // datos coinciden.
    }

  /**
   * Test para buscar un doctor por especialidad, fecha y hora
   */

    @Test
    public void testbusqueda() {
        String especialidad = "Cardiología";
        LocalDateTime fecha = LocalDateTime.of(2024, 6, 17, 0, 0);
        System.out.println("------------------------");
        DayOfWeek dia = fecha.getDayOfWeek();
        System.out.println("------------------------");
        System.out.println(dia);
        LocalTime hora = LocalTime.of(17, 0);

        // Invocar el método a probar
        List<DoctorDatosDTO> result = doctorRepository.findDoctorByEspecialidadAndFechaAndHora(especialidad, dia, hora);

        // Verificar el resultado
        assertNotEquals(0, result.size()); // Asegurar que la lista no esté vacía// Asegurar que el resultado sea el
                                           // esperado
        System.out.println("------------------------");
        System.out.println(result.size());
        for (DoctorDatosDTO doctor : result) {
            System.out.println(
                    "Doctor escogido: " + doctor.getNombreDoctor() + ", Consultorio: " + doctor.getConsultorioCodigo());
        }

    }
    /**
     * Test para filtrar doctores sin citas
     */

    @Test
    public void testFiltrarDoctoresSinCitas() {
        String especialidad = "Cardiología";
        LocalDateTime fecha = LocalDateTime.of(2024, 6, 17, 0, 0);
        System.out.println("------------------------");
        DayOfWeek dia = fecha.getDayOfWeek();
        System.out.println("------------------------");
        System.out.println(dia);
        LocalTime hora = LocalTime.of(17, 0);

        // Invocar el método a probar
        List<DoctorDatosDTO> result = doctorService.filtrarDoctoresSinCita(especialidad, fecha, hora);

        // Verificar el resultado
        assertNotEquals(0, result.size()); // Asegurar que la lista no esté vacía// Asegurar que el resultado sea el
                                           // esperado
        System.out.println("------------------------");
        System.out.println(result.size());
        for (DoctorDatosDTO doctor : result) {
            System.out.println(
                    "Doctor escogido: " + doctor.getNombreDoctor() + ", Consultorio: " + doctor.getConsultorioCodigo());
        }

    }

}
