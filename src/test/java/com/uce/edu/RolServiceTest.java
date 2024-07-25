package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.modelo.Rol;
import com.uce.edu.service.serviciosSistemaGeneral.RolServiceImpl;

/**
 * RolServiceTest
 */
@SpringBootTest
@Rollback(true)
public class RolServiceTest {

    /**
     * Dependencias
     */
    @Autowired
    private RolServiceImpl rolService;

    /**
     * Test para crear un rol, y var que se guarde una sola vez en la base de datos
     */
    @Test
    public void testGuardarRolSiNoEstaRepetido() {

        Rol rol = new Rol("x", "Descripción");

        boolean isSaved = rolService.guardarRolSiNoEstaRepetido(rol);

        /**
         * Se verifica que el rol se haya guardado correctamente
         */
        assertTrue(isSaved, "El rol debería haberse guardado correctamente");

    }

}