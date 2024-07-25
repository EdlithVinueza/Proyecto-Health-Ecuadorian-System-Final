package com.uce.edu.util.validaciones;

public class ValidadorCedula {

    /**
     * Método que permite validar una cédula ecuatoriana
     * @param cedula
     * @return
     */
    public static boolean validarCedula(String cedula) {
        // Verificar que la cédula tenga 10 dígitos
        if (cedula == null || cedula.length() != 10) {
            return false;
        }

        // Convertir la cédula a un array de enteros
        int[] digits = new int[10];
        for (int i = 0; i < 10; i++) {
            digits[i] = Integer.parseInt(String.valueOf(cedula.charAt(i)));
        }

        // Verificar que los dos primeros dígitos correspondan a una provincia válida
        int provincia = digits[0] * 10 + digits[1];
        if (provincia < 1 || provincia > 24) {
            return false;
        }

        // Verificar que el tercer dígito sea menor que 6
        if (digits[2] >= 6) {
            return false;
        }

        // Aplicar el algoritmo de verificación
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int valor = digits[i] * ((i % 2 == 0) ? 2 : 1);
            if (valor > 9) {
                valor -= 9;
            }
            suma += valor;
        }

        int verificador = 10 - (suma % 10);
        if (verificador == 10) {
            verificador = 0;
        }

        return verificador == digits[9];
    }
}
