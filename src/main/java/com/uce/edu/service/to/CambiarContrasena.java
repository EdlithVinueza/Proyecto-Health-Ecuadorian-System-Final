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
 * CambiarContrasena, objeto para traer informacion para cambiar la contraseña de un usuario desde su respectivo formulario
 
 */
public class CambiarContrasena {

   @NotBlank(message = "Tu cedula es obligatoria")
    private String cedula;

    @NotBlank(message = "La contraseña es requerida")
    @Size(min=3,max = 15, message = "La contraseña debe tener entre 3 y 15 caracteres")
    @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "La respuesta debe contener al menos un carácter especial")
    private String nuevaContrasenia;

    @NotBlank(message = "La repeticion de la contrasenia  es requerida")
    @Size(min=3,max = 15, message = "La contraseña repetida debe tener entre 3 y 15 caracteres")
    @Pattern(regexp = ".*[!@#$%^&*(),.?\":{}|<>].*", message = "La respuesta debe contener al menos un carácter especial")
    private String nuevaContraseniaRepetida;
}
