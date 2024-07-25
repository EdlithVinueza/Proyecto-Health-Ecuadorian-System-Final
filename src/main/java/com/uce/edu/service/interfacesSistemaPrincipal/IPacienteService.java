package com.uce.edu.service.interfacesSistemaPrincipal;

import java.util.List;

import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.to.PacienteTO;
import com.uce.edu.service.to.PreguntasTO;

/**
 * IPacienteService
 */
public interface IPacienteService {
    /**
     * Método para guardar un paciente
     * 
     * @param paciente
     * @return
     */
    public Paciente guardarPaciente(Paciente paciente);

    /**
     * Método para guardar un paciente nuevo desde un TO
     * 
     * @param PacienteTO
     * @param preguntasTO
     * @return
     */
    public Paciente guardarPacienteNuevo(PacienteTO PacienteTO, PreguntasTO preguntasTO);

    /**
     * Método para listar todos los pacientes
     * 
     * @return
     */
    public List<PacienteDTO> listarPacientes();

    /**
     * Método para buscar un paciente por cédula
     * 
     * @param cedula
     * @return
     */
    public PacienteDTO buscarPacientePorCedula(String cedula);

    /**
     * Método para actualizar un paciente trayendo informacion de un TO
     * 
     * @param PacienteTO
     * @return
     */
    public Paciente actualizarPaciente(PacienteTO PacienteTO);

    /**
     * Método para convertir un paciente a PacienteTO desde un DTO
     * 
     * @param PacienteDTO
     * @return
     */
    public PacienteTO convertirPacienteDTOAPacienteTO(PacienteDTO PacienteDTO);

    /**
     * Método para listar los tipos de discapacidad
     * 
     * @return
     */
    public List<String> tiposDiscapacidad();

    /**
     * Método para evitar mas datos a las vitas
     * 
     * @return
     */
    public List<String> bandera();

    /**
     * Método para listar los tipos de genero
     * 
     * @return
     */
    public List<String> tipoGenero();

    /**
     * Método para buscar un paciente por cedula
     * 
     * @param cedulaPaciente
     * @return
     */
    public Paciente buscarPacientePorCedulaPaciente(String cedulaPaciente);

    /**
     * Método para buscar un paciente por cedula
     * 
     * @param cedula
     * @return
     */
    public Paciente buscarPorCedula(String cedula);

}
