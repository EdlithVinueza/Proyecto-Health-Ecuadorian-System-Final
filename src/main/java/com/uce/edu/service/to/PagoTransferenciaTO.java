package com.uce.edu.service.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 *  PagoTransferenciaTO, objeto para traer informacion para realizar un pago por transferencia desde su respectivo formulario
 */
public class PagoTransferenciaTO {

    @NotBlank(message = "El código de la orden no puede ser nulo")
    private String ordenCodigo;

    @NotBlank(message = "Debes ingresar el numero de targeta de credito o debito")
    @Pattern(regexp = "^[0-9]{16}$", message = "El número de tarjeta debe ser numérico y contener 16 dígitos")
    private String tarjeta;

}

