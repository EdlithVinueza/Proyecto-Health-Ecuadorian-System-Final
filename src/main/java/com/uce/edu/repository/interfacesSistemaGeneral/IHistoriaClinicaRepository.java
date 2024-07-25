package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.HistoriaClinica;

public interface IHistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {

    @Query("SELECT hc FROM HistoriaClinica hc WHERE hc.paciente.cedula = :cedula")
    List<HistoriaClinica> buscarPorCedulaPaciente(@Param("cedula") String cedula);

}
