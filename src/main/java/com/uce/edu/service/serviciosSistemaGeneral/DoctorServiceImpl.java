package com.uce.edu.service.serviciosSistemaGeneral;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.ICitaMedicaRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IEspecialiadRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IPreguntaRecuperacionRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IRespuestaPreguntaRepository;
import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.repository.modelo.RespuestaPregunta;
import com.uce.edu.repository.modelo.dto.DoctorDTO;
import com.uce.edu.repository.modelo.dto.DoctorDatosDTO;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.interfacesSistemaPrincipal.IDoctorService;
import com.uce.edu.service.to.DoctorTO;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.util.Enum.EstadoDoctor;
import com.uce.edu.util.Enum.EstadoSeguridad;

@Service
public class DoctorServiceImpl implements IDoctorService {

    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IEspecialiadRepository especialidadRepository;

    @Autowired
    private IPreguntaRecuperacionRepository preguntaRecuperacionRepository;

    @Autowired
    private IRespuestaPreguntaRepository respuestaPreguntaRepository;

    @Autowired
    private IEncryptSercive encryptSercive;

    @Autowired
    private ICitaMedicaRepository citaMedicaRepository;

    @Override
    public Doctor guardarDoctor(Doctor Doctor) {

        return doctorRepository.save(Doctor);
    }

    @Override
    public Doctor guardarDoctorNuevo(DoctorTO doctorTO, PreguntasTO preguntasTO) {

        Especialidad especialidad = especialidadRepository.findByNombre(doctorTO.getEspecialidad());

        Doctor nuevoDoctor = new Doctor();

        nuevoDoctor.setNombre(doctorTO.getNombre());
        nuevoDoctor.setApellido(doctorTO.getApellido());
        nuevoDoctor.setCedula(doctorTO.getCedula());
        nuevoDoctor.setCorreo(doctorTO.getCorreo());
        nuevoDoctor.setTelefono(doctorTO.getTelefono());
        nuevoDoctor.setDireccion(doctorTO.getDireccion());
        nuevoDoctor.setFechaNacimiento(doctorTO.getFechaNacimiento());
        nuevoDoctor.setEspecialidad(especialidad);
        nuevoDoctor.setEstado(EstadoDoctor.NUEVO.getEstado());
        nuevoDoctor.setEstadoSeguridad(EstadoSeguridad.ACTIVIDAD_4.getEstado());
        nuevoDoctor.setUsuario(doctorTO.getCedula());
        nuevoDoctor.setContrasena(encryptSercive.encriptar(doctorTO.getCedula()));

        nuevoDoctor = doctorRepository.save(nuevoDoctor);

        PreguntaRecuperacion preguntaUno = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaUno());
        PreguntaRecuperacion preguntaDos = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaDos());
        PreguntaRecuperacion preguntaTres = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaTres());

        RespuestaPregunta respuestaUno = new RespuestaPregunta(preguntaUno, "vacio");
        respuestaUno.setDoctor(nuevoDoctor);
        respuestaPreguntaRepository.save(respuestaUno);
        RespuestaPregunta respuestaDos = new RespuestaPregunta(preguntaDos, "vacio");
        respuestaDos.setDoctor(nuevoDoctor);
        respuestaPreguntaRepository.save(respuestaDos);
        RespuestaPregunta respuestaTres = new RespuestaPregunta(preguntaTres, "vacio");
        respuestaTres.setDoctor(nuevoDoctor);
        respuestaPreguntaRepository.save(respuestaTres);

        List<RespuestaPregunta> respuestas = new ArrayList<>();
        respuestas.add(respuestaUno);
        respuestas.add(respuestaDos);
        respuestas.add(respuestaTres);

        nuevoDoctor.setRespuestasPregunta(respuestas);

        return doctorRepository.save(nuevoDoctor);

    }

    @Override
    public List<DoctorDTO> listarDoctores() {
        return doctorRepository.findAllDoctors();
    }

    @Override
    public DoctorDTO buscarDoctorPorCedula(String cedula) {
        return doctorRepository.findDtoByCedula(cedula);
    }

    @Override
    public Doctor actualizarDoctor(DoctorTO doctorTO) {
        Doctor doctor = doctorRepository.findByCedula(doctorTO.getCedula());
        Especialidad especialidad = especialidadRepository.findByNombre(doctorTO.getEspecialidad());
        doctor.setNombre(doctorTO.getNombre());
        doctor.setApellido(doctorTO.getApellido());
        doctor.setCedula(doctorTO.getCedula());
        doctor.setCorreo(doctorTO.getCorreo());
        doctor.setTelefono(doctorTO.getTelefono());
        doctor.setDireccion(doctorTO.getDireccion());
        doctor.setFechaNacimiento(doctorTO.getFechaNacimiento());
        doctor.setEspecialidad(especialidad);
        return doctorRepository.save(doctor);
    }

    @Override
    public DoctorTO convertirDoctorDTOADoctorTO(DoctorDTO doctorDTO) {

        DoctorTO doctorTO = new DoctorTO();
        doctorTO.setNombre(doctorDTO.getNombre());
        doctorTO.setApellido(doctorDTO.getApellido());
        doctorTO.setCedula(doctorDTO.getCedula());
        doctorTO.setCorreo(doctorDTO.getCorreo());
        doctorTO.setTelefono(doctorDTO.getTelefono());
        doctorTO.setDireccion(doctorDTO.getDireccion());
        doctorTO.setFechaNacimiento(doctorDTO.getFechaNacimiento());
        return doctorTO;
    }

    @Override
    public List<DoctorDatosDTO> buscarDoctorPorEspecialidadYFechaYHora(String especialidad, DayOfWeek dia,
            LocalTime hora) {
        List<DoctorDatosDTO> datosDoctorDatosDTOs = doctorRepository
                .findDoctorByEspecialidadAndFechaAndHora(especialidad, dia, hora);

        return datosDoctorDatosDTOs;
    }

    @Override
    public List<DoctorDatosDTO> filtrarDoctoresSinCita(String especialidad, LocalDateTime fecha, LocalTime hora) {
        List<DoctorDatosDTO> doctoresSinCita = this.buscarDoctorPorEspecialidadYFechaYHora(especialidad,
                fecha.getDayOfWeek(), hora);
        Set<DoctorDatosDTO> doctoresSinCitaFinal = new HashSet<>();

        if (!doctoresSinCita.isEmpty()) {
            for (DoctorDatosDTO doctor : doctoresSinCita) {
                List<CitaMedica> citas = citaMedicaRepository.buscarPorHoraInicioFinFechaYDoctor(hora,
                        hora.plusHours(doctor.getTiempoConsulta()),
                        fecha, doctor.getCedulaDoctor());
                if (citas.isEmpty()) {
                    // Si el doctor no tiene citas en el rango dado, se añade al conjunto temporal
                    // de doctores sin cita
                    doctoresSinCitaFinal.add(doctor);
                }
            }
        } else {
            return null;
        }
        return new ArrayList<>(doctoresSinCitaFinal);
    }

}
