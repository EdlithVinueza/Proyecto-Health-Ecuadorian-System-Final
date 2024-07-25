package com.uce.edu.service.interfacesSistemaPrincipal;

import java.util.List;
import com.uce.edu.repository.modelo.HistoriaClinica;
import com.uce.edu.service.to.DatosHistoriaClinicaTO;
import com.uce.edu.service.to.HistoriaClinicaTO;

/**
 * IHistoriaClinicaService
 */
public interface IHistoriaClinicaService {

    /**
     * Método para guardar una historia clínica
     * 
     * @param historiaClinicaTO
     * @return
     */
    public HistoriaClinica guardarHistoriaClinica(HistoriaClinicaTO historiaClinicaTO);

    /**
     * Método para buscar historias clínicas por id
     * 
     * @param id
     * @return
     */
    public HistoriaClinica buscarPorId(Integer id);

    /**
     * Método para convertir una historia clínica a DatosHistoriaClinicaTO
     * 
     * @param historiaClinica
     * @return
     */

    public DatosHistoriaClinicaTO convertirHistoriaClinicaADatosHistoriaClinicaTO(HistoriaClinica historiaClinica);

    /**
     * Método para convertir historias clínicas a DatosHistoriaClinicaTO
     * 
     * @param historiasClinicas
     * @return
     */
    public List<DatosHistoriaClinicaTO> convertirHistoriasClinicasADatosHistoriasClinicasTO(
            List<HistoriaClinica> historiasClinicas);

    /**
     * Método para buscar historias clínicas por cédula de paciente
     * 
     * @param cedulaPaciente
     * @return
     */
    public List<HistoriaClinica> buscarPorCedulaPaciente(String cedulaPaciente);
}
