package com.uce.edu.service.to;

import java.time.LocalDate;
import java.time.LocalTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * BuscarCitaTO, objeto para traer informacion para buscar una cita desde su respectivo formulario
 */
public class BuscarCitaTO {

    private String especialidad;
    private LocalDate diaCita;
    private LocalTime horaCita;

}
