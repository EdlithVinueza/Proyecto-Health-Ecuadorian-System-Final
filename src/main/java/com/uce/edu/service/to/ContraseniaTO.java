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
 * ContraseniaTO, objeto para traer informacion para cambiar la contraseña de un usuario desde su respectivo formulario
 */
public class ContraseniaTO {
    @NotBlank(message = "Ingresa contraseña actual")
    @Size(min = 3, max = 15, message = "La contraseña actual debe tener entre 3 y 15 caracteres")

    private String contraseñaActual;
    @NotBlank(message = "Ingresa contraseña nueva")
    @Size(min = 3, max = 15, message = "La contraseña nueva debe tener entre 3 y 15 caracteres")

    private String contraseñaNueva;
    @NotBlank(message = "Ingresa contraseña nueva")
    @Size(min = 3, max = 15, message = "La contraseña nueva debe tener entre 3 y 15 caracteres")

    private String contraseñaNuevaRepetida;

   

}
