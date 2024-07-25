package com.uce.edu.repository.modelo.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {

    private Integer id;
    private String nombre;
    private String apellido;
    private String cedula;
    private String correo;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String tipoDiscapacidad;

    
    
}
