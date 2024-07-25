package com.uce.edu.repository.modelo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriaClinicaDTO {

    private Integer id;
    private String cedulaPaciente;
    private String nombrePaciente;
    private String apellidoPaciente;
    private String codigoCitaMedica;
    private String especialidad;

    


}
