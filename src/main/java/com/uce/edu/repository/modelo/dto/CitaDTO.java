package com.uce.edu.repository.modelo.dto;

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
public class CitaDTO {

    private Integer id;
    private String codigo;
    private String consultorio;
    private LocalDateTime fechaCita;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;
    private String nombrePaciente;
    private String apellidoPaciente;
    private String cedulaPaciente;



    
 

}
