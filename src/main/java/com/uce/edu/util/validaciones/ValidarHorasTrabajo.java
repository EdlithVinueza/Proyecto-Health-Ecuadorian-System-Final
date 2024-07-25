package com.uce.edu.util.validaciones;

import java.time.LocalTime;

public class ValidarHorasTrabajo {

    /**
     * Metodo que permite validar si la hora de inicio es menor a la hora de fin
     * @param horaInicio
     * @param horaFin
     * @return
     */
 public static boolean validarHoras(LocalTime horaInicio, LocalTime horaFin) {
        return horaInicio.isBefore(horaFin);
    }
}
