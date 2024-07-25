package com.uce.edu.service.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/** 
 * DatosHistoriaClinicaTO, objeto para traer informacion para la historia clinica desde su respectivo formulario
 
 */
public class DatosHistoriaClinicaTO {

    private Integer id;
    private String nombrePaciente;
    private String nombreDoctor;
    private String especialidad;
    private String fechaRegistro;
    private String diagnostico;
    private String tratamiento;
    private String examenes;
    private String resultados;
    private String recetaMedica;
    private String medicamentos;
    private String observaciones;

}
