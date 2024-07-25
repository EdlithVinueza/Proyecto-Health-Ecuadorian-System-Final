package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IHorarioRespository;
import com.uce.edu.repository.modelo.Horario;
import com.uce.edu.service.interfacesSistemaPrincipal.IHorarioService;
import com.uce.edu.service.to.HorarioTO;

/**
 * HorarioServiceTest
 
 */
@SpringBootTest
@Rollback(true)
public class HorarioServiceTest {

/**
 * Dependencias
 */
    @Autowired
    private IHorarioRespository horarioRespository;

    @Autowired
    private IHorarioService horarioService;

    /**
     * Método que permite probar la conversión de un horario a un HorarioTO
     */
    @Test
    public void testConvertirHorarioAHorarioTO() {
       String cedula = "1751674027";
      
        List<Horario> result = horarioRespository.findByDoctorCedula(cedula);

        Horario horario = result.get(0);
  
        HorarioTO horarioTO = horarioService.convertirHorarioAHorarioTO(horario);
        assertNotNull(horarioTO);
        assertEquals(horario.getId(), horarioTO.getId());
        assertEquals(horario.getDoctor().getCedula(), horarioTO.getCedulaDoctor());
        assertEquals(horario.getConsultorio().getCodigo(), horarioTO.getConsultorio());
        assertEquals(horario.getDia(), horarioTO.getDia());
        assertEquals(horario.getHoraInicio(), horarioTO.getHoraInicio());
        assertEquals(horario.getHoraFin(), horarioTO.getHoraFin());
        assertEquals(horario.getDuracionCita(), horarioTO.getDuracionCita());

        System.out.println(horarioTO.toString());
    }

    /**
     * Método que permite probar la conversión de un horario a un HorarioTO y su
     */
    @Test
    public void testConvertirGuardarHorarioTO() {
       String cedula = "1751674027";
      
        List<Horario> result = horarioRespository.findByDoctorCedula(cedula);

        Horario horario = result.get(0);
  
        HorarioTO horarioTO = horarioService.convertirHorarioAHorarioTO(horario);
        assertNotNull(horarioTO);
        assertEquals(horario.getId(), horarioTO.getId());
        assertEquals(horario.getDoctor().getCedula(), horarioTO.getCedulaDoctor());
        assertEquals(horario.getConsultorio().getCodigo(), horarioTO.getConsultorio());
        assertEquals(horario.getDia(), horarioTO.getDia());
        assertEquals(horario.getHoraInicio(), horarioTO.getHoraInicio());
        assertEquals(horario.getHoraFin(), horarioTO.getHoraFin());
        assertEquals(horario.getDuracionCita(), horarioTO.getDuracionCita());

        System.out.println(horarioTO.toString());
       horarioTO.setConsultorio("codigo3");
        Horario horario2 = horarioService.guardarHorario(horarioTO);
        assertNotNull(horario2);
    }



}
