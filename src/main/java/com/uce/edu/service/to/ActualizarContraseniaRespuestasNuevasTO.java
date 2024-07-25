package com.uce.edu.service.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
 * ActualizarContraseniaRespuestasNuevasTO, objeto para traer informacion para actualizar la contraseña de un usuario
 */
public class ActualizarContraseniaRespuestasNuevasTO {

    @NotBlank(message = "El código es requerido")
    private String codigo;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min=3,max = 15, message = "La contraseña debe tener entre 3 y 15 caracteres")
    @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "La respuesta debe contener al menos un carácter especial")
    private String nuevaContrasenia;

    @NotBlank(message = "La repeticion de la contrasenia  es requerida")
    @Size(min=3,max = 15, message = "La contraseña repetida debe tener entre 3 y 15 caracteres")
    @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "La respuesta debe contener al menos un carácter especial")
    private String nuevaContraseniaRepetida;
    
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
