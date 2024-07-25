package com.uce.edu.service.interfacesSistemaPrincipal;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.dto.DoctorDTO;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.to.DoctorTO;
import com.uce.edu.service.to.PreguntasTO;

/**
 * IDoctorService
 * 
 */
public interface IDoctorService {

    /**
     * Método para guardar un doctor
     * 
     * @param doctor
     * @return
     */
    public Doctor guardarDoctor(Doctor doctor);

    /**
     * Método para guardar un doctor nuevo
     * 
     * @param doctorTO
     * @param preguntasTO
     * @return
     */
    public Doctor guardarDoctorNuevo(DoctorTO doctorTO, PreguntasTO preguntasTO);

    /**
     * Método para listar los doctores
     * 
     * @return
     */
    public List<DoctorDTO> listarDoctores();

    /**
     * Método para buscar un doctor por cédula
     * 
     * @param cedula
     * @return
     */
    public DoctorDTO buscarDoctorPorCedula(String cedula);

    /**
     * Método para actualizar un Doctros trayendo informacion de un TO
     * 
     * @param doctorTO
     * @return
     */
    public Doctor actualizarDoctor(DoctorTO doctorTO);

    /**
     * Método para convertir un doctorDTO a un DoctorTO
     */
    public DoctorTO convertirDoctorDTOADoctorTO(DoctorDTO doctorDTO);

    /**
     * Método para buscar un doctor por especialidad y fecha y hora
     * 
     * @param especialidad
     * @param dia
     * @param hora
     * @return
     */
    public List<DoctorDatosDTO> buscarDoctorPorEspecialidadYFechaYHora(String especialidad, DayOfWeek dia,
            LocalTime hora);

    /**
     * Método para filtrar doctores sin cita
     * 
     * @param especialidad
     * @param fecha
     * @param hora
     * @return
     */
    public List<DoctorDatosDTO> filtrarDoctoresSinCita(String especialidad, LocalDateTime fecha, LocalTime hora);
}
