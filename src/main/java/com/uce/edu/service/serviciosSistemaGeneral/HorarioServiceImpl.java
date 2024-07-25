package com.uce.edu.service.serviciosSistemaGeneral;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IConsultorioRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IHorarioRespository;
import com.uce.edu.repository.modelo.Consultorio;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Horario;
import com.uce.edu.service.interfacesSistemaPrincipal.IHorarioService;
import com.uce.edu.service.to.HorarioTO;

@Service
public class HorarioServiceImpl implements IHorarioService {

    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IHorarioRespository horarioRespository;

    @Autowired
    private IConsultorioRepository consultorioRepository;

    @Override
    public List<String> obtenerDiasSemana() {
        return Arrays.asList("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    public List<Horario> buscarHorarioPorCedulaDoctor(String cedulaDocotor) {
        List<Horario> horarios = horarioRespository.findByDoctorCedula(cedulaDocotor);
        List<String> diasSemana = this.obtenerDiasSemana();
    
        return horarios.stream()
                .sorted(Comparator.comparing((Horario h) -> diasSemana.indexOf(h.getDia()))
                                  .thenComparing(Horario::getHoraInicio)
                                  .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public HorarioTO convertirHorarioAHorarioTO(Horario horario) {
        HorarioTO horarioTO = new HorarioTO();
        horarioTO.setId(horario.getId());
        horarioTO.setCedulaDoctor(horario.getDoctor().getCedula());
        horarioTO.setConsultorio(horario.getConsultorio().getCodigo());
        horarioTO.setDia(this.convertirDayOfWeekAString(horario.getDia()));
        horarioTO.setHoraInicio(horario.getHoraInicio());
        horarioTO.setHoraFin(horario.getHoraFin());
        horarioTO.setDuracionCita(horario.getDuracionCita());
        return horarioTO;
    }

    @Override
    public Horario convertirHorarioTOAHorario(HorarioTO horarioTO) {

        Doctor doctor = doctorRepository.findByCedula(horarioTO.getCedulaDoctor());
        Consultorio consultorio = consultorioRepository.findByCodigo(horarioTO.getConsultorio());
        Horario horario = new Horario();
        horario.setId(horarioTO.getId());
        horario.setDoctor(doctor);
        horario.setConsultorio(consultorio);
        horario.setDia(this.convertirADayOfWeek(horarioTO.getDia()));
        horario.setHoraInicio(horarioTO.getHoraInicio());
        horario.setHoraFin(horarioTO.getHoraFin());
        horario.setDuracionCita(horarioTO.getDuracionCita());

        return horario;
    }

    @Override
    public List<HorarioTO> convertirListaToHorarioTO(List<Horario> horarios) {
        return horarios.stream()
                .map(this::convertirHorarioAHorarioTO)
                .collect(Collectors.toCollection(LinkedHashSet::new)) // Use LinkedHashSet to maintain order and remove
                                                                      // duplicates
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public Horario guardarHorario(HorarioTO horarioTO) {
        Doctor doctor = doctorRepository.findByCedula(horarioTO.getCedulaDoctor());
        Consultorio consultorio = consultorioRepository.findByCodigo(horarioTO.getConsultorio());
        Horario horario = new Horario();
        horario.setId(horarioTO.getId());
        horario.setDoctor(doctor);
        horario.setConsultorio(consultorio);
        horario.setDia(this.convertirADayOfWeek(horarioTO.getDia()));
        horario.setHoraInicio(horarioTO.getHoraInicio());
        horario.setHoraFin(horarioTO.getHoraFin());
        horario.setDuracionCita(horarioTO.getDuracionCita());

        return horarioRespository.save(horario);
    }

    @Override
    public HorarioTO buscarHorarioPorId(int id) {
        return this.convertirHorarioAHorarioTO(horarioRespository.findById(id));
    }

    @Override
    public Horario guardarNuevoHorario(HorarioTO horarioTO) {
        Doctor doctor = doctorRepository.findByCedula(horarioTO.getCedulaDoctor());
        Consultorio consultorio = consultorioRepository.findByCodigo(horarioTO.getConsultorio());
        Horario horario = new Horario();
        horario.setDoctor(doctor);
        horario.setConsultorio(consultorio);
        horario.setDia(this.convertirADayOfWeek(horarioTO.getDia()));
        horario.setHoraInicio(horarioTO.getHoraInicio());
        horario.setHoraFin(horarioTO.getHoraFin());
        horario.setDuracionCita(horarioTO.getDuracionCita());

        return horarioRespository.save(horario);
    }

    @Override
    public List<Integer> duracionesCitas() {
        return Arrays.asList(15, 30, 45, 60);
    }

    @Override
    public String convertirDayOfWeekAString(DayOfWeek dia) {
        switch (dia) {
            case MONDAY:
                return "Lunes";
            case TUESDAY:
                return "Martes";
            case WEDNESDAY:
                return "Miércoles";
            case THURSDAY:
                return "Jueves";
            case FRIDAY:
                return "Viernes";
            case SATURDAY:
                return "Sábado";
            case SUNDAY:
                return "Domingo";
            default:
                throw new IllegalArgumentException("Día no válido: " + dia);

        }
    }

    @Override
    public DayOfWeek convertirADayOfWeek(String dia) {
        switch (dia.toLowerCase(Locale.ROOT)) {
            case "lunes":
                return DayOfWeek.MONDAY;
            case "martes":
                return DayOfWeek.TUESDAY;
            case "miércoles":
                return DayOfWeek.WEDNESDAY;
            case "jueves":
                return DayOfWeek.THURSDAY;
            case "viernes":
                return DayOfWeek.FRIDAY;
            case "sábado":
                return DayOfWeek.SATURDAY;
            case "domingo":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Día no válido: " + dia);
        }
    }

    @Override
    public DayOfWeek getDayOfWeekFromLocalDateTime(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek();
    }

}
