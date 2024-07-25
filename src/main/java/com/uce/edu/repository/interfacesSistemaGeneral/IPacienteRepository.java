package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.dto.PacienteDTO;

public interface IPacienteRepository extends JpaRepository<Paciente, Integer>{
 @Query("SELECT new com.uce.edu.repository.modelo.dto.PacienteDTO(p.id, p.nombre, p.apellido, p.cedula, p.correo, p.telefono, p.direccion, p.fechaNacimiento, p.tipoDiscapacidad) FROM Paciente p WHERE p.cedula = :cedula")
    PacienteDTO findDtoByCedula(@Param("cedula") String cedula);

    @Query("SELECT new com.uce.edu.repository.modelo.dto.PacienteDTO(p.id, p.nombre, p.apellido, p.cedula, p.correo, p.telefono, p.direccion, p.fechaNacimiento, p.tipoDiscapacidad) FROM Paciente p")
    List<PacienteDTO> findAllAsDto();

    
    Paciente findByCedula(String cedula);

}
