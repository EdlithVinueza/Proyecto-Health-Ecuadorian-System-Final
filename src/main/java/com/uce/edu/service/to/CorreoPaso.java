package com.uce.edu.service.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * CorreoPaso, objeto para traer informacion para enviar un correo electronico desde su respectivo formulario
 */
public class CorreoPaso {

    @NotEmpty(message = "La Cédula es obligatoria")
    private String cedula;
    @NotEmpty(message = "El correo electrónico es obligatorio")
    @Email(message = "Por favor, ingresa un correo electrónico válido")
    private String correo;

}
