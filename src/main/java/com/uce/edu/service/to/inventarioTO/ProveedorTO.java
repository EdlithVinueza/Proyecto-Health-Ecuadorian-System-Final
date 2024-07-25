package com.uce.edu.service.to.inventarioTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
 * ProveedorTO, objeto para traer informacion de los proveedores desde su respectivo formulario
 */
public class ProveedorTO {

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(min = 5, max = 15, message = "el nombre del proveedor debetener entre 5 y 15 caracteres")
    private String nombre;

  
    @NotBlank(message = "El ruc del proveedor es obligatorio")
    @Size(min = 13, max = 13, message = "El ruc del proveedor debetener 13 caracteres")
    private String ruc;

    
    @NotBlank(message = "La direccion del proveedor es obligatoria")
    @Size(min = 5, max = 250, message = "La direccion del proveedor debetener entre 5 y 250 caracteres")
    private String direccion;

    @NotBlank(message = "El telefono del proveedor es obligatorio")
    @Size(min = 7, max = 15, message = "El telefono del proveedor debetener entre 7 y 15 caracteres")
    private String telefono;

    @Email(message = "Correo no válido")
    @NotBlank(message = "Correo es requerido")
    @Size(max = 50, message = "Correo debe tener máximo 50 caracteres")
    @Column(name = "prov_correo", length = 50, nullable = false, unique = true)
    private String correo;

}
