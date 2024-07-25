package com.uce.edu.service.to;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
 * PacienteTO, objeto para traer informacion para crear un paciente desde su respectivo formulario

 */
public class PacienteTO {
    private Integer id;

    @NotBlank(message = "El nombre paciente es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre paciente debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paciente es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido paciente debe tener entre 3 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "La cédula del paciente es obligatoria")
    @Size(max = 10, message = "La cédula del paciente debe tener entre 10")
    @Pattern(regexp = "^[0-9]+$", message = "La cédula Ecuatoriana solo debe contener números")
    private String cedula;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo electrónico no es válido")
    private String correo;

    @NotBlank(message = "Teléfono es requerido")
    @Size(max = 15, message = "Teléfono debe tener máximo 15 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "El telefono solo debe contener números")
    private String telefono;

    @NotBlank(message = "Dirección es requerida")
    @Size(max = 255, message = "Dirección debe tener máximo 255 caracteres")
    private String direccion;

    @NotNull(message = "Fecha de nacimiento es requerida")
    @Past(message = "Fecha de nacimiento debe ser una fecha pasada")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "La discapacidad del paciente es requerida")
    private String discapacidad;

    @NotBlank(message = "El tipo de discapacidad del paciente es requerido")
    private String tipoDiscapacidad;

    @NotBlank(message = "El género del paciente es requerido")
    private String genero;

}
