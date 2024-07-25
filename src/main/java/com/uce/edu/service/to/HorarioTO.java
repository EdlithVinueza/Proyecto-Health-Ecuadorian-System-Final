package com.uce.edu.service.to;

import java.time.LocalTime;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
 * HorarioTO, objeto para traer informacion para crear un horario desde su respectivo formulario
 */
public class HorarioTO {

    
    private Integer id;

    @NotBlank(message = "La cédula del doctor es obligatoria")
    @Size(min = 10, max = 15, message = "La cédula del doctor debe tener entre 10 y 15 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "La cédula solo debe contener números")
    private String cedulaDoctor;

    @NotBlank(message = "El consultorio es obligatorio")
    private String consultorio;

    @NotBlank(message = "El día es obligatorio")
    @Size(min = 3, max = 10, message = "El día debe tener entre 3 y 10 caracteres")
    private String dia;

    @NotNull(message = "La hora de inicio es obligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime horaFin;

    @NotNull(message = "La duración de la cita es obligatoria")
    private Integer duracionCita;

    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HorarioTO horarioTO = (HorarioTO) o;
        return Objects.equals(cedulaDoctor, horarioTO.cedulaDoctor) &&
                Objects.equals(consultorio, horarioTO.consultorio) &&
                Objects.equals(dia, horarioTO.dia) &&
                Objects.equals(horaInicio, horarioTO.horaInicio) &&
                Objects.equals(horaFin, horarioTO.horaFin) &&
                Objects.equals(duracionCita, horarioTO.duracionCita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cedulaDoctor, consultorio, dia, horaInicio, horaFin, duracionCita);
    }
    

}
