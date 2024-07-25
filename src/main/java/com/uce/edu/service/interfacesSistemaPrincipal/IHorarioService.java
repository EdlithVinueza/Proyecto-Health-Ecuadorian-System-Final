package com.uce.edu.service.interfacesSistemaPrincipal;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import com.uce.edu.repository.modelo.Horario;
import com.uce.edu.service.to.HorarioTO;
/**
 * IHorarioService
 */
public interface IHorarioService {
/**
 * Método para obtener los días de la semana
 * @return
 */
    public List<String> obtenerDiasSemana();

    /**
     * Método para obtener los horarios de un doctor
     * @param cedulaDocotor
     * @return
     */
    List<Horario> buscarHorarioPorCedulaDoctor(String cedulaDocotor);

    /**
     * Método para convertir un horario a HorarioTO
     * @param horario
     * @return
     */
    HorarioTO convertirHorarioAHorarioTO(Horario horario);

    /**
     * Método para convertir un HorarioTO a Horario
     * @param horarioTO
     * @return
     */
    Horario convertirHorarioTOAHorario(HorarioTO horarioTO);

    /**
     * Método para guardar un horario
     * @param horarioTO
     * @return
     */
    Horario guardarHorario(HorarioTO horarioTO);

    /**
     * Método para buscar un horario por id
     * @param id
     * @return
     */
    HorarioTO buscarHorarioPorId(int id);

    /**
     * Método para guardar un nuevo horario
     * @param horarioTO
     * @return
     */
    Horario guardarNuevoHorario(HorarioTO horarioTO);

    /**
     * Método para convertir una lista de horarios a HorarioTO
     * @param horarios
     * @return
     */
    List<HorarioTO> convertirListaToHorarioTO(List<Horario> horarios);

    /**
     * Método para traer todos las tiempos de duración de las citas
     * @return
     */
    List<Integer> duracionesCitas();

    /**
     * Método para convertir un día de la semana a String
     * @param dia
     * @return
     */
    String convertirDayOfWeekAString(DayOfWeek dia);

    /**
     * Método para convertir un String a DayOfWeek
     * @param dia
     * @return
     */
    DayOfWeek convertirADayOfWeek(String dia);
   
    /**
     * Método para obtener el día de la semana de una fecha
     * @param dateTime
     * @return
     */
    DayOfWeek getDayOfWeekFromLocalDateTime(LocalDateTime dateTime);
}
