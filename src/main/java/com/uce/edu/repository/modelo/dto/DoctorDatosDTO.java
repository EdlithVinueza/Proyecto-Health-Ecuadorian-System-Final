package com.uce.edu.repository.modelo.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorDatosDTO {

    private Integer idDoctor;
    private String nombreDoctor;
    private String apellidoDoctor;
    private String cedulaDoctor;
    private String consultorioCodigo;
    private Integer tiempoConsulta;
    private String especialidad;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDatosDTO that = (DoctorDatosDTO) o;
        return Objects.equals(idDoctor, that.idDoctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDoctor);
    }

}
