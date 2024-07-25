package com.uce.edu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.uce.edu.util.validaciones.ValidadorCedula;

/**
 * que permite probar la validación de una cédula
 * 
 */
public class ValidadorCedulaTest {
    /**
     * Método que permite probar la validación de una cédula
     */
    @Test
    public void testCedulaValida() {
        // Asumiendo que 0102034567 es una cédula válida para propósitos de prueba
        String cedulaValida = "0102034567";
        Assertions.assertTrue(ValidadorCedula.validarCedula(cedulaValida));
    }

    /**
     * Método que permite probar la validación de una cédula inválida
     */
    @Test
    public void testCedulaInvalida() {
        // Asumiendo que 0102034568 es una cédula inválida para propósitos de prueba
        String cedulaInvalida = "0100034007";
        Assertions.assertFalse(ValidadorCedula.validarCedula(cedulaInvalida));
    }

}
