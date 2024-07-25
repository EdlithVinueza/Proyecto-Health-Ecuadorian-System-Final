package com.uce.edu.service.to;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * ActualizarCitaTO, objeto para traer informacion para actualziar una citas desde su respectivo formulario
 */
public class ActualizarCitaTO {

    @NotBlank(message = "El código de la cita no puede ser nulo")
    private String codigoCita;
    @NotNull(message = "El día de la cita no puede ser nulo")
    @FutureOrPresent(message = "El día de la cita debe ser en el presente o futuro")
    private LocalDate diaCita;
    @NotNull(message = "La hora de la cita no puede ser nula")
    private LocalTime horaCita;
    private String cedulaAuditor;

   
   

}
