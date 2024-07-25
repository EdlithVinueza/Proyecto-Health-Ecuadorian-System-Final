package com.uce.edu.service.to;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * PagoEfectivoTO, objeto para traer informacion para realizar un pago en efectivo desde su respectivo formulario
 */
public class PagoEfectivoTO {

    @NotBlank(message = "El código de la orden no puede ser nulo")
    private String ordenCodigo;

    @AssertTrue(message = "El dinero debe estar en caja para continuar.")
    private Boolean dineroEnCaja;

   
    

}
