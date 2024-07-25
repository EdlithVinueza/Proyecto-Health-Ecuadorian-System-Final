package com.uce.edu.repository.interfacesSistemaGeneral;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.dto.DoctorDTO;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;

public interface IDoctorRepository extends JpaRepository<Doctor, Integer> {
        Doctor findByCedula(String cedula);

        List<Doctor> findByEspecialidad(Especialidad especialidad);

        @SuppressWarnings("null")
        List<Doctor> findAll();

        void deleteByCedula(String cedula);

        @Query("SELECT new com.uce.edu.repository.modelo.dto.DoctorDTO(d.id, d.nombre, d.apellido, d.cedula, d.correo, d.telefono, d.direccion, d.fechaNacimiento, d.especialidad.nombre) FROM Doctor d")
        List<DoctorDTO> findAllDoctors();

        @Query("SELECT new com.uce.edu.repository.modelo.dto.DoctorDTO(d.id, d.nombre, d.apellido, d.cedula, d.correo, d.telefono, d.direccion, d.fechaNacimiento, d.especialidad.nombre) FROM Doctor d WHERE d.cedula = :cedula")
        DoctorDTO findDtoByCedula(@Param("cedula") String cedula);

        @Query("SELECT new com.uce.edu.repository.modelo.dto.DoctorDatosDTO(d.id, d.nombre, d.apellido, d.cedula, h.consultorio.codigo, h.duracionCita, d.especialidad.nombre) "
                        +
                        "FROM Doctor d " +
                        "JOIN d.horarios h " +
                        "WHERE d.especialidad.nombre = :especialidad " +
                        "AND h.dia = :dia " +
                        "AND :hora BETWEEN h.horaInicio AND h.horaFin")
        List<DoctorDatosDTO> findDoctorByEspecialidadAndFechaAndHora(@Param("especialidad") String especialidad,
                        @Param("dia") DayOfWeek dia,
                        @Param("hora") LocalTime hora);

}
