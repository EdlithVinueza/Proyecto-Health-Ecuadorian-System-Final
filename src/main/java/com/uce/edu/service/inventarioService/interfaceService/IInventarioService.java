package com.uce.edu.service.inventarioService.interfaceService;

import java.util.List;

import com.uce.edu.repository.modelo.inventarioModelo.InsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;
import com.uce.edu.service.to.inventarioTO.InsumoMedicoTO;
import com.uce.edu.service.to.inventarioTO.MedicamentoTO;
/**
 * IInventarioService
 */
public interface IInventarioService {
    
    /**
     * Método para obtener todos los medicamentos
     * @return
     */
    public List<Medicamento> todosLosMedicamentos();

    /**
     * Método para generar caracteres aleatorios
     * @param longitud
     * @return
     */
    public String generarCaracteresAleatorios(int longitud);

    /**
     * Método para generar un código único para un producto
     */
    public String generarCodigoUnicoProducto(String prefijo);

    /**
     * Método para buscar un medicamento por código
     * @param codigo
     * @return
     */
    public Medicamento buscarMedicamentoPorCodigo(String codigo);

    /**
     * Método para buscar un medicamento por nombre común
     * @param nombre
     * @return
     */
    public List<Medicamento> buscarMedicamentoPorNombreComun(String nombre);

    /**
     * Método para buscar un medicamento por nombre de marca
     * @param nombre
     * @return
     */
    public Medicamento buscarMedicamentoPorNombreMarca(String nombre);

    /**
     * Método para buscar un medicamento por proveedor
     * @param ruc
     * @param nombreEmpresa
     * @return
     */
    public List<Medicamento> buscarMedicamentoPorProveedor(String ruc, String nombreEmpresa);

    /**
     * Método para guardar un nuevo medicamento desde un TO
     * @param medicamentoTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public Medicamento guardarNuevoMedicamento(MedicamentoTO medicamentoTO, String nombreAuditor, String cedulaAuditor);

    /**
     * Método para actualizar un medicamento desde un TO
     * @param medicamentoTO
     * @param codigo
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public Medicamento actualizarMedicamento(MedicamentoTO medicamentoTO,String codigo, String nombreAuditor, String cedulaAuditor);

    /**
     * Método para construir un TO de medicamento
     * @param codigoMedicamento
     * @return
     */
    public MedicamentoTO contruMedicamentoTO(String codigoMedicamento);

    /**
     * Método para obtener todos los insumos médicos
     * @return
     */
    public List<InsumoMedico> todosLosInsumosMedicos();

    /**
     * Método para buscar un insumo médico por código
     * @param codigo
     * @return
     */
    public InsumoMedico buscarInsumoMedicoPorCodigo(String codigo);

    /**
     * Método para buscar un insumo médico por nombre
     * @param nombre
     * @return
     */
    public List<InsumoMedico> buscarInsumoMedicoPorNombre(String nombre);

    /**
     * Método para guardar un nuevo insumo médico desde un TO
     * @param insumoMedicoTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public InsumoMedico guardarNuevoInsumoMedico(InsumoMedicoTO insumoMedicoTO, String nombreAuditor, String cedulaAuditor);

    /**
     * Método para actualizar un insumo médico desde un TO
     * @param insumoMedicoTO
     * @param codigo
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public InsumoMedico actualizarInsumoMedico(InsumoMedicoTO insumoMedicoTO,String codigo, String nombreAuditor, String cedulaAuditor);

    /**
     * Método para construir un TO de insumo médico
     * @param codigoInsumoMedico
     * @return
     */
    public InsumoMedicoTO contruirInsumoMedicoTO(String codigoInsumoMedico);

    /**
     * Método para buscar un insumo médico por proveedor
     * @param ruc
     * @param nombreEmpresa
     * @return
     */
    public List<InsumoMedico> buscarInsumoMedicoPorProveedor(String ruc, String nombreEmpresa);
}
