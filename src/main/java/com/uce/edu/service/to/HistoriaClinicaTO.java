package com.uce.edu.service.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
/**
 * HistoriaClinicaTO, objeto para traer informacion para crear una historia clinica desde su respectivo formulario
 */
public class HistoriaClinicaTO {

   private String codigoCita;

   private String cedulaDoctor;

    @NotBlank(message = "Los exámenes no pueden estar vacíos")
    @Size(max = 255, message = "Los exámenes no pueden superar los 255 caracteres")
    private String examenes;

    @NotBlank(message = "Los resultados no pueden estar vacíos")
    @Size(max = 255, message = "Los resultados no pueden superar los 255 caracteres")
    private String resultados;

    @NotBlank(message = "Los diagnóstico no pueden estar vacíos")
    @Size(max = 255, message = "El diagnóstico no puede superar los 255 caracteres")
    private String diagnostico;

    @NotBlank(message = "El tratamiento no puede estar vacío")
    @Size(max = 255, message = "El tratamiento no puede superar los 255 caracteres")
    private String tratamiento;

    @NotBlank(message = "La receta médica no puede estar vacía")
    @Size(max = 255, message = "La receta médica no puede superar los 255 caracteres")
    private String recetaMedica;

    @NotBlank(message = "Los medicamentos no pueden estar vacíos")
    @Size(max = 255, message = "Los medicamentos no pueden superar los 255 caracteres")
    private String medicamentos;

    @NotBlank(message = "La observación no puede estar vacía")
    @Size(max = 255, message = "La observación no puede superar los 255 caracteres")
    private String observacion;

}
