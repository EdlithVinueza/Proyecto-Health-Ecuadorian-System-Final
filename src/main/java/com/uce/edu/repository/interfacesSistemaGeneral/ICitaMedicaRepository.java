package com.uce.edu.repository.interfacesSistemaGeneral;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.dto.CitaDTO;

public interface ICitaMedicaRepository extends JpaRepository<CitaMedica, Integer> {
        /**
         * Busca una cita medica por la fecha de la cita y el codigo del consultorio
         * 
         * @param horaInicio
         * @param horaFin
         * @param fecha
         * @param cedulaDoctor
         * @return
         */
        @Query("SELECT cm FROM CitaMedica cm JOIN cm.doctor d WHERE cm.horaInicio >= :horaInicio AND cm.horaFin <= :horaFin AND cm.fechaModificacion = :fecha AND d.cedula = :cedulaDoctor")
        List<CitaMedica> buscarPorHoraInicioFinFechaYDoctor(@Param("horaInicio") LocalTime horaInicio,
                        @Param("horaFin") LocalTime horaFin, @Param("fecha") LocalDateTime fecha,
                        @Param("cedulaDoctor") String cedulaDoctor);

        CitaMedica findByCodigo(String codigo);

        /**
         * Busca todas las citas medicas y almacena en un TO
         * 
         * @return
         */
        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido, p.cedula) "
                        +
                        "FROM CitaMedica cm " +
                        "JOIN cm.consultorio c " +
                        "JOIN cm.paciente p")
        List<CitaDTO> buscarTodasLasCitas();

        /**
         * Busca todas las citas medicas por fecha y almacena en un TO
         * 
         * @param fecha
         * @return
         */
        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido, p.cedula) "
                        +
                        "FROM CitaMedica cm " +
                        "JOIN cm.consultorio c " +
                        "JOIN cm.paciente p " +
                        "WHERE cm.fechaCita = :fecha AND (cm.estado = 'Agendada' OR cm.estado = 'Reagendada')")
        List<CitaDTO> buscarCitasPorFecha(@Param("fecha") LocalDateTime fecha);

        /**
         * Busca todas las citas medicas por cedula de paciente y almacena en un DTO
         * 
         * @param cedulaPaciente
         * @return
         */
        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido,p.cedula) "
                        +
                        "FROM CitaMedica cm JOIN cm.consultorio c JOIN cm.paciente p " +
                        "WHERE p.cedula = :cedulaPaciente AND cm.estado IN ('Agendada', 'Reagendada')")
        List<CitaDTO> buscarCitasPorCedulaPaciente(@Param("cedulaPaciente") String cedulaPaciente);

        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido, p.cedula) "
                        + "FROM CitaMedica cm "
                        + "JOIN cm.consultorio c "
                        + "JOIN cm.paciente p "
                        + "WHERE cm.fechaCita = :fecha AND p.cedula = :cedulaPaciente AND (cm.estado = 'Agendada' OR cm.estado = 'Reagendada')")
        List<CitaDTO> buscarCitasPorFechaYCedulaPaciente(@Param("fecha") LocalDateTime fecha,
                        @Param("cedulaPaciente") String cedulaPaciente);

        /**
         * Busca todas las citas medicas por cedula de doctor y almacena en un DTO
         * 
         * @param cedulaDoctor
         * @return
         */
        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido, p.cedula) "
                        +
                        "FROM CitaMedica cm JOIN cm.consultorio c JOIN cm.paciente p JOIN cm.doctor d " +
                        "WHERE d.cedula = :cedulaDoctor AND cm.estado IN ('Agendada', 'Reagendada')")
        List<CitaDTO> buscarCitasPorCedulaDoctor(@Param("cedulaDoctor") String cedulaDoctor);

        /**
         * Busca todas las citas medicas por fecha y cedula de doctor y almacena en un
         * DTO
         * 
         * @param fecha
         * @param cedulaDoctor
         * @return
         */
        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido,p.cedula) "
                        +
                        "FROM CitaMedica cm JOIN cm.consultorio c JOIN cm.paciente p JOIN cm.doctor d " +
                        "WHERE cm.fechaCita = :fecha AND d.cedula = :cedulaDoctor AND cm.estado IN ('Agendada', 'Reagendada')")
        List<CitaDTO> buscarCitasPorFechaYCedulaDoctor(@Param("fecha") LocalDateTime fecha,
                        @Param("cedulaDoctor") String cedulaDoctor);

        /**
         * Busca todas las citas medicas por cedula de paciente y cedula de doctor y
         * almacena en un DTO
         * 
         * @param cedulaPaciente
         * @param cedulaDoctor
         * @return
         */
        @Query("SELECT new com.uce.edu.repository.modelo.dto.CitaDTO(cm.id, cm.codigo, c.nombre, cm.fechaCita, cm.horaInicio, cm.horaFin, cm.estado, p.nombre, p.apellido,p.cedula) "
                        +
                        "FROM CitaMedica cm JOIN cm.consultorio c JOIN cm.paciente p JOIN cm.doctor d " +
                        "WHERE p.cedula = :cedulaPaciente AND d.cedula = :cedulaDoctor AND cm.estado IN ('Agendada', 'Reagendada')")
        List<CitaDTO> buscarCitasPorCedulaPacienteYCedulaDoctor(@Param("cedulaPaciente") String cedulaPaciente,
                        @Param("cedulaDoctor") String cedulaDoctor);

}
