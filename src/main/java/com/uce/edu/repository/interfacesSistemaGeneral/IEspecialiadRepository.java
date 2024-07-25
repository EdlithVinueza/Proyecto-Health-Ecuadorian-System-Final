package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.dto.EspecialidadNombreDTO;

public interface IEspecialiadRepository  extends JpaRepository<Especialidad, Integer>{

    Especialidad findByNombre(String nombre);
    
    @Query("SELECT new com.uce.edu.repository.modelo.dto.EspecialidadNombreDTO(e.nombre) FROM Especialidad e")
    List<EspecialidadNombreDTO> findAllDto();

}
