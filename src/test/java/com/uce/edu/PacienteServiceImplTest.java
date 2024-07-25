package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
/**
 * PacienteServiceImplTest
 
 */
@SpringBootTest
@Rollback(true)
public class PacienteServiceImplTest {

    /**
     *  Dependencias
     */
    @Autowired
    private IPacienteService pacienteService;

    /**
     * Método que permite probar la búsqueda de un paciente por cédula
     */
    @Test
    public void testBuscarPorCedula() {

        String cedula = "1751675001";
        Paciente pacienteEsperado = new Paciente();
        pacienteEsperado.setCedula(cedula);

        Paciente resultado = pacienteService.buscarPorCedula(cedula);
        assertNotNull(resultado, "El resultado no debe ser null");
    }

}
