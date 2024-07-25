package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IConsultorioRepository;
import com.uce.edu.repository.modelo.Consultorio;

/**
 * ConsultorioRepositoryTest
 * 
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ConsultorioRepositoryTest {
    /**
     * Dependencias
     */
    @Autowired
    private IConsultorioRepository consultorioRepository;

    /**
     * Método que permite probar la búsqueda de consultorios por código
     */
    @Test
    public void testBuscarConsultorioPorCodigo() {
        Consultorio consultorio = new Consultorio("nombre", "numero", "piso", "especialidad", "codigo");
        consultorioRepository.save(consultorio);
        Consultorio consultorioGuardado = consultorioRepository.findByCodigo("codigo");
        assertEquals(consultorio, consultorioGuardado);
    }

    /**
     * Método que permite probar el guardado de consultorios por especialidad
     */

    @Test
    public void testGuardarConsultorios() {
        Consultorio consultorio1 = new Consultorio("nombre1", "numero1", "piso1", "especialidad1", "codigo1");
        Consultorio consultorio2 = new Consultorio("nombre2", "numero2", "piso2", "especialidad2", "codigo2");
        Consultorio consultorio3 = new Consultorio("nombre3", "numero3", "piso3", "especialidad3", "codigo3");

        consultorioRepository.save(consultorio1);
        consultorioRepository.save(consultorio2);
        consultorioRepository.save(consultorio3);

        Consultorio consultorioGuardado1 = consultorioRepository.findByCodigo("codigo1");
        Consultorio consultorioGuardado2 = consultorioRepository.findByCodigo("codigo2");
        Consultorio consultorioGuardado3 = consultorioRepository.findByCodigo("codigo3");

        assertEquals(consultorio1, consultorioGuardado1);
        assertEquals(consultorio2, consultorioGuardado2);
        assertEquals(consultorio3, consultorioGuardado3);

    }

}
