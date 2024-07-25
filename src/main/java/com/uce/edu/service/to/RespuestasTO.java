package com.uce.edu.service.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * RespuestasTO, objeto para traer informacion para las respuestas desde su respectivo formulario
 */
public class RespuestasTO {

    @NotBlank(message = "El código es requerido")
    private String codigo;
    
    @NotBlank(message = "La respuesta 1 es requerida")
    @Size(min=3,max = 15, message = "La respuesta debe tener entre 3 y 15 caracteres")
    private String respuestaUno;
    @NotBlank(message = "La respuesta 2 es requerida")
    @Size(min=3,max = 15, message = "La respuesta debe tener entre 3 y 15 caracteres")
    private String respuestaDos;
    @NotBlank(message = "La respuesta 3 es requerida")
    @Size(min=3,max = 15, message = "La respuesta debe tener entre 3 y 15 caracteres")
    private String respuestaTres;

}
