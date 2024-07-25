package com.uce.edu.service.to.inventarioTO;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
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
 * InsumoMedicoTO, objeto para traer informacion de los insumos medicos desde su
 * respectivo formulario
 */
public class InsumoMedicoTO {

    @NotBlank(message = "El parametro es obligatorio")
    @Size(min = 5, max = 50, message = "El parametro ser mayor que 5 y menor que 50 caracteres")
    private String parametro;

    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(min = 5, max = 25, message = "El nombre del medicamento debe tener entre 5 y 25 caracteres")
    private String nombre;

    @NotBlank(message = "La descripcion del insumo es obligatoria")
    @Size(min = 5, max = 255, message = "La descripcion del insumo debe tener entre 5 y 255 caracteres")
    private String descripcion;

    @DecimalMin(value = "0.01", message = "El precio de compra debe ser mayor que cero")
    @NotNull(message = "El precio del insumo es obligatorio")
    private BigDecimal precioCompra;

    @Min(value = 0, message = "El stock del producto no puede ser negativo")
    @NotNull(message = "El stock del insumo es obligatorio")
    private Integer stock;

}
