package com.uce.edu.service.serviciosSistemaGeneral;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.ICitaMedicaRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IHistoriaClinicaRepository;
import com.uce.edu.repository.modelo.CitaMedica;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.HistoriaClinica;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.service.interfacesSistemaPrincipal.IHistoriaClinicaService;
import com.uce.edu.service.to.DatosHistoriaClinicaTO;
import com.uce.edu.service.to.HistoriaClinicaTO;
import com.uce.edu.util.Enum.EstadoCita;

@Service
public class HistoriaClinicaServiceImpl implements IHistoriaClinicaService {

    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private ICitaMedicaRepository citaMedicaRepository;

    @Autowired
    private IHistoriaClinicaRepository historiaClinicaRepository;

    @Override
    public HistoriaClinica guardarHistoriaClinica(HistoriaClinicaTO historiaClinicaTO) {
        HistoriaClinica historiaClinica = new HistoriaClinica();
        
        CitaMedica citaMedica = citaMedicaRepository.findByCodigo(historiaClinicaTO.getCodigoCita());
        Doctor doctor = doctorRepository.findByCedula(historiaClinicaTO.getCedulaDoctor());
        if (doctor == null) {
            doctor = citaMedica.getDoctor();
        }
        Paciente paciente = citaMedica.getPaciente();
        Especialidad especialidad = citaMedica.getEspecialidad();

        citaMedica.setEstado(EstadoCita.ATENDIDA.getEstado());
        citaMedicaRepository.save(citaMedica);

        historiaClinica.setDoctor(doctor);
        historiaClinica.setPaciente(paciente);
        historiaClinica.setCita_medica(citaMedica);
        historiaClinica.setEspecialidad(especialidad);
        historiaClinica.setFechaRegistro(LocalDateTime.now());
        historiaClinica.setDiagnostico(historiaClinicaTO.getDiagnostico());
        historiaClinica.setTratamiento(historiaClinicaTO.getTratamiento());
        historiaClinica.setExamenes(historiaClinicaTO.getExamenes());
        historiaClinica.setResultados(historiaClinicaTO.getResultados());
        historiaClinica.setRecetaMedica(historiaClinicaTO.getRecetaMedica());
        historiaClinica.setMedicamentos(historiaClinicaTO.getMedicamentos());
        historiaClinica.setTratamieno(historiaClinicaTO.getTratamiento());
        historiaClinica.setObservacion(historiaClinicaTO.getObservacion());

        return historiaClinicaRepository.save(historiaClinica);
    }

    @Override
    public HistoriaClinica buscarPorId(Integer id) {
        return historiaClinicaRepository.findById(id).orElse(null);
    }

    @Override
    public DatosHistoriaClinicaTO convertirHistoriaClinicaADatosHistoriaClinicaTO(HistoriaClinica historiaClinica) {
        DatosHistoriaClinicaTO datosHistoriaClinicaTO = new DatosHistoriaClinicaTO();
        datosHistoriaClinicaTO.setId(historiaClinica.getId());
        datosHistoriaClinicaTO.setNombrePaciente(
                historiaClinica.getPaciente().getNombre() + " " + historiaClinica.getPaciente().getApellido());
        datosHistoriaClinicaTO.setNombreDoctor(
                historiaClinica.getDoctor().getNombre() + " " + historiaClinica.getDoctor().getApellido());
        datosHistoriaClinicaTO.setEspecialidad(historiaClinica.getEspecialidad().getNombre());
        datosHistoriaClinicaTO.setFechaRegistro(historiaClinica.getFechaRegistro().toString());
        datosHistoriaClinicaTO.setDiagnostico(historiaClinica.getDiagnostico());
        datosHistoriaClinicaTO.setTratamiento(historiaClinica.getTratamiento());
        datosHistoriaClinicaTO.setExamenes(historiaClinica.getExamenes());
        datosHistoriaClinicaTO.setResultados(historiaClinica.getResultados());
        datosHistoriaClinicaTO.setRecetaMedica(historiaClinica.getRecetaMedica());
        datosHistoriaClinicaTO.setMedicamentos(historiaClinica.getMedicamentos());
        datosHistoriaClinicaTO.setObservaciones(historiaClinica.getObservacion());

        return datosHistoriaClinicaTO;
    }

    @Override
    public List<DatosHistoriaClinicaTO> convertirHistoriasClinicasADatosHistoriasClinicasTO(
            List<HistoriaClinica> historiasClinicas) {
        List<DatosHistoriaClinicaTO> datosHistoriasClinicasTO = new ArrayList<>();
        for (HistoriaClinica historiaClinica : historiasClinicas) {
            DatosHistoriaClinicaTO datosHistoriaClinicaTO = convertirHistoriaClinicaADatosHistoriaClinicaTO(
                    historiaClinica);
            datosHistoriasClinicasTO.add(datosHistoriaClinicaTO);
        }
        return datosHistoriasClinicasTO;
    }

    @Override
    public List<HistoriaClinica> buscarPorCedulaPaciente(String cedulaPaciente) {
        return historiaClinicaRepository.buscarPorCedulaPaciente(cedulaPaciente);
        }

}
