package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uce.edu.repository.modelo.Consultorio;
import com.uce.edu.repository.modelo.dto.ConsultorioDTO;

public interface IConsultorioRepository extends JpaRepository<Consultorio, Integer> {

        Consultorio findByNombre(String nombre);

        Consultorio findByNumero(String numero);

        Consultorio findByPiso(String piso);

        Consultorio findByEspecialidad(String especialidad);

        Consultorio findByCodigo(String codigo);

        @Query("SELECT new com.uce.edu.repository.modelo.dto.ConsultorioDTO(c.codigo) FROM Consultorio c")
        List<ConsultorioDTO> findAllCodigos();
}
