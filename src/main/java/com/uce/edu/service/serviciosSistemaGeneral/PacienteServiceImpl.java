package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IPacienteRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IPreguntaRecuperacionRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IRespuestaPreguntaRepository;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.repository.modelo.RespuestaPregunta;
import com.uce.edu.repository.modelo.dto.PacienteDTO;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.to.PacienteTO;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.util.Enum.EstadoPaciente;
import com.uce.edu.util.Enum.EstadoSeguridad;
import com.uce.edu.util.Enum.Genero;
import com.uce.edu.util.Enum.TipoDiscapacidad;

@Service
public class PacienteServiceImpl implements IPacienteService {

    @Autowired
    private IPacienteRepository pacienteRepository;

    @Autowired
    private IPreguntaRecuperacionRepository preguntaRecuperacionRepository;

    @Autowired
    private IRespuestaPreguntaRepository respuestaPreguntaRepository;

    @Autowired
    private IEncryptSercive encryptSercive;

    @Override
    public Paciente guardarPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }

    @Override
    public Paciente guardarPacienteNuevo(PacienteTO pacienteTO, PreguntasTO preguntasTO) {
        Paciente paciente = new Paciente();
        paciente.setNombre(pacienteTO.getNombre());
        paciente.setApellido(pacienteTO.getApellido());
        paciente.setCedula(pacienteTO.getCedula());
        paciente.setCorreo(pacienteTO.getCorreo());
        paciente.setTelefono(pacienteTO.getTelefono());
        paciente.setDireccion(pacienteTO.getDireccion());
        paciente.setFechaNacimiento(pacienteTO.getFechaNacimiento());

        paciente.setDiscapacidad(false);

        paciente.setTipoDiscapacidad(pacienteTO.getTipoDiscapacidad());
        paciente.setEstado(EstadoPaciente.NUEVO.getEstado());
        paciente.setEstadoSeguridad(EstadoSeguridad.ACTIVIDAD_4.getEstado());
        paciente.setUsuario(pacienteTO.getCedula());
        paciente.setContrasena(encryptSercive.encriptar(pacienteTO.getCedula()));
        Paciente nuevoPaciente = guardarPaciente(paciente);
        PreguntaRecuperacion preguntaUno = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaUno());
        PreguntaRecuperacion preguntaDos = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaDos());
        PreguntaRecuperacion preguntaTres = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaTres());
        RespuestaPregunta respuestaUno = new RespuestaPregunta(preguntaUno, "vacio");
        respuestaUno.setPaciente(nuevoPaciente);
        respuestaPreguntaRepository.save(respuestaUno);
        RespuestaPregunta respuestaDos = new RespuestaPregunta(preguntaDos, "vacio");
        respuestaDos.setPaciente(nuevoPaciente);
        respuestaPreguntaRepository.save(respuestaDos);
        RespuestaPregunta respuestaTres = new RespuestaPregunta(preguntaTres, "vacio");
        respuestaTres.setPaciente(nuevoPaciente);
        respuestaPreguntaRepository.save(respuestaTres);
        List<RespuestaPregunta> respuestas = new ArrayList<>();
        respuestas.add(respuestaUno);
        respuestas.add(respuestaDos);
        respuestas.add(respuestaTres);
        nuevoPaciente.setRespuestasPregunta(respuestas);
        return guardarPaciente(paciente);
    }

    @Override
    public List<PacienteDTO> listarPacientes() {
        return pacienteRepository.findAllAsDto();
    }

    @Override
    public PacienteDTO buscarPacientePorCedula(String cedula) {
        return pacienteRepository.findDtoByCedula(cedula);
    }

    @Override
    public Paciente actualizarPaciente(PacienteTO pacienteTO) {
        Paciente paciente = pacienteRepository.findByCedula(pacienteTO.getCedula());
        paciente.setNombre(pacienteTO.getNombre());
        paciente.setApellido(pacienteTO.getApellido());
        paciente.setCorreo(pacienteTO.getCorreo());
        paciente.setTelefono(pacienteTO.getTelefono());
        paciente.setDireccion(pacienteTO.getDireccion());
        paciente.setFechaNacimiento(pacienteTO.getFechaNacimiento());

        if (pacienteTO.getDiscapacidad().equals("Si")) {
            paciente.setDiscapacidad(true);
        } else {
            paciente.setDiscapacidad(false);
        }
        paciente.setTipoDiscapacidad(pacienteTO.getTipoDiscapacidad());
        return pacienteRepository.save(paciente);
    }

    @Override
    public PacienteTO convertirPacienteDTOAPacienteTO(PacienteDTO PacienteDTO) {
        PacienteTO pacienteTO = new PacienteTO();
        pacienteTO.setId(PacienteDTO.getId());
        pacienteTO.setNombre(PacienteDTO.getNombre());
        pacienteTO.setApellido(PacienteDTO.getApellido());
        pacienteTO.setCedula(PacienteDTO.getCedula());
        pacienteTO.setCorreo(PacienteDTO.getCorreo());
        pacienteTO.setTelefono(PacienteDTO.getTelefono());
        pacienteTO.setDireccion(PacienteDTO.getDireccion());
        pacienteTO.setFechaNacimiento(PacienteDTO.getFechaNacimiento());

        if (PacienteDTO.getTipoDiscapacidad().equals("Ninguna")) {
            pacienteTO.setDiscapacidad("No");
        } else {
            pacienteTO.setDiscapacidad("Si");
        }
        pacienteTO.setTipoDiscapacidad(PacienteDTO.getTipoDiscapacidad());
        return pacienteTO;
    }

    @Override
    public List<String> tiposDiscapacidad() {
        return Arrays.stream(TipoDiscapacidad.values())
                .map(TipoDiscapacidad::getDisplayName)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> bandera() {
        return Arrays.asList("Si", "No");
    }

    @Override
    public List<String> tipoGenero() {
            return Arrays.stream(Genero.values())
                 .map(Genero::getDisplayName)
                 .collect(Collectors.toList());
}

    @Override
    public Paciente buscarPacientePorCedulaPaciente(String cedulaPaciente) {
        return pacienteRepository.findByCedula(cedulaPaciente);
    }

    @Override
    public Paciente buscarPorCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula);
    }
}
