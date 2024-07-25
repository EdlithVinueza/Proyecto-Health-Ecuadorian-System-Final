package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IConsultorioRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IHorarioRespository;
import com.uce.edu.repository.modelo.Consultorio;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Horario;

/**
 * HorarioRepositoryTest
 
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class HorarioRepositoryTest {

    /**
     * Dependencias
     */
    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IConsultorioRepository consultorioRepository;

    @Autowired
    private IHorarioRespository horarioRespository;

    /**
     * Método que permite probar la creación de un horario
     */
    @Test
    public void testIngresarHorario() {
        Doctor doctor = doctorRepository.findByCedula("1751674027");
        assertNotNull(doctor);

        Consultorio consultorio = consultorioRepository.findByCodigo("codigo1");
        assertNotNull(consultorio);

        Horario horario1 = new Horario();
        horario1.setDoctor(doctor);
        horario1.setConsultorio(consultorio);
        horario1.setDia(DayOfWeek.MONDAY);
        horario1.setHoraInicio(LocalTime.of(9, 0));
        horario1.setHoraFin(LocalTime.of(17, 0));
        horario1.setDuracionCita(30);

        Horario horario2 = new Horario();
        horario2.setDoctor(doctor);
        horario2.setConsultorio(consultorio);
        horario2.setDia(DayOfWeek.TUESDAY);
        horario2.setHoraInicio(LocalTime.of(9, 0));
        horario2.setHoraFin(LocalTime.of(17, 0));
        horario2.setDuracionCita(30);

        horarioRespository.save(horario1);
        horarioRespository.save(horario2);

        doctor.getHorarios().add(horario1);
        doctor.getHorarios().add(horario2);

        doctorRepository.save(doctor);

        assertEquals(2, doctor.getHorarios().size());

        assertNotNull(horarioRespository.findById(horario1.getId()));
        assertNotNull(horarioRespository.findById(horario2.getId()));
    }

    /**
     * Método que permite probar la búsqueda de horarios por cédula de doctor
     */
    @Test
    public void testPrintHorariosAndCount() {
        String cedula = "1751674027";
      
        List<Horario> result = horarioRespository.findByDoctorCedula(cedula);

        result.forEach(horario -> System.out.println(horario.getId()));

        assertEquals(2, result.size(), "The doctor should have 2 schedules");
    }

}
