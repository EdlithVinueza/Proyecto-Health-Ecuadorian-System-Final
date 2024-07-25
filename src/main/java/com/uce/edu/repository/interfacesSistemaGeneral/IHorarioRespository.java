package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Horario;

public interface IHorarioRespository extends JpaRepository<Horario, Integer> {

    List<Horario> findByDoctor(Doctor doctor);

    @Query("SELECT h FROM Horario h WHERE h.doctor.cedula = :cedula")
    List<Horario> findByDoctorCedula(@Param("cedula") String cedula);

    Horario findById(int id);

    

}
