package com.uce.edu.service.to;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/**
 * DatosCitaAgendar, objeto para traer informacion para agendar una cita desde su respectivo formulario
 */
public class DatosCitaAgendar {

    private String nombreDoctor;
    private String consultorioCodigo;
    private Integer tiempoConsulta;
    private String especialidad;
    private LocalDateTime fechaCita;
    private LocalTime horaInicio;
    private LocalTime horaFin;


}
