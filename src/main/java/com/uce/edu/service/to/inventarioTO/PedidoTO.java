package com.uce.edu.service.to.inventarioTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
/**
 * PedidoTO, objeto para traer informacion de los pedidos desde su respectivo formulario
 
 */
public class PedidoTO {

    @NotBlank(message = "El ruc o nombre del proveedor es obligatorio")
    @Size(min = 5, max = 50, message = "El ruc o nombre del proveedor debetener 13 caracteres")
    private String parametro;


    @NotBlank(message = "El código del producto es obligatorio")
    @Size(max = 50, message = "El código del producto debe tener entre 1 y 50 caracteres")
    private String codigoProducto;

    @NotNull(message = "El cantidad del producto es obligatorio")
    @Min(value = 0, message = "El cantidad del producto no puede ser negativo")
    private Integer cantidad;

}
