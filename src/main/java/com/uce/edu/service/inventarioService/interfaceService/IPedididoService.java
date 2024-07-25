package com.uce.edu.service.inventarioService.interfaceService;

import java.util.List;

import com.uce.edu.repository.modelo.inventarioModelo.PedidoMedicamento;
import com.uce.edu.repository.modelo.inventarioModelo.PedidoInsumoMedico;
import com.uce.edu.service.to.inventarioTO.PedidoTO;

/**
 * IPedididoService
 */
public interface IPedididoService {

    /**
     * Método para obtener todos los pedidos de medicamentos
     * 
     * @return
     */
    public List<PedidoMedicamento> obtenerTodosLosPedidosDeMedicamento();

    /**
     * Método para buscar un pedido de medicamento por código
     * 
     * @param codigo
     * @return
     */
    public PedidoMedicamento buscarPedidoMedicamentoPorCodigo(String codigo);

    /**
     * Método para buscar un pedido de medicamento por proveedor
     * 
     * @param ruc
     * @param nombreEmpresa
     * @return
     */
    public List<PedidoMedicamento> buscarPedidoMedicamentoPorProveedor(String ruc, String nombreEmpresa);

    /**
     * Método para buscar un pedido de medicamento por código o nombre de insumo
     * 
     * @param parametro
     * @return
     */
    public List<PedidoMedicamento> buscarPedidoMedicamentoPorCodigoONombreInsumo(String parametro);

    /**
     * Método para buscar un pedido de medicamento por estado
     * 
     * @param estado
     * @return
     */
    public List<PedidoMedicamento> buscarPedidoMedicamentoPorEstado(String estado);

    /**
     * Método para guardar un nuevo pedido de insumo medico
     * 
     * @param pedidoTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoInsumoMedico antesDeGuardarPedidoInsumoMedico(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAuditor);

    /**
     * Método para guardar un nuevo pedido de medicamento
     * 
     * @param pedidoTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoMedicamento guardarNuevoPedidoMedicamento(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAuditor);

    /**
     * Método para recibir un pedido de medicamento
     * 
     * @param codigo
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoMedicamento recibirPedidoMedicamento(String codigo, String nombreAuditor, String cedulaAuditor);

    /**
     * Método para rechazar un pedido de medicamento
     * 
     * @param codigo
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoMedicamento rechazarPedidoMedicamento(String codigo, String nombreAuditor, String cedulaAuditor);

    /**
     * Método para obtener todos los pedidos de insumos
     * 
     * @return
     */
    public List<PedidoInsumoMedico> obtenerTodosLosPedidosDeInsumos();

    /**
     * Método para buscar un pedido de insumo por código
     * 
     * @param codigo
     * @return
     */
    public PedidoInsumoMedico buscarPedidoInsumoMedicoPorCodigo(String codigo);

    /**
     * Método para buscar un pedido de insumo por proveedor
     * 
     * @param ruc
     * @param nombreEmpresa
     * @return
     */
    public List<PedidoInsumoMedico> buscarPedidoInsumoMedicoPorProveedor(String ruc, String nombreEmpresa);

    /**
     * Método para buscar un pedido de insumo por código o nombre de insumo
     * 
     * @param parametro
     * @return
     */
    public List<PedidoInsumoMedico> buscarPedidoInsumoMedicoPorCodigoONombreInsumo(String parametro);

    /**
     * Método para buscar un pedido de insumo por estado
     */
    public List<PedidoInsumoMedico> buscarPedidoInsumoMedicoPorEstado(String estado);

    /**
     * Método para recibir un pedido de insumo
     * 
     * @param pedidoTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoMedicamento antesDeGuardarPedidoMedicamento(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAuditor);

    /**
     * Método para guardar un nuevo pedido de insumo medico
     * 
     * @param pedidoTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoInsumoMedico guardarNuevoPedidoInsumoMedico(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAuditor);

    /**
     * Método para recibir un pedido de insumo
     * 
     * @param codigo
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoInsumoMedico recibirPedidoInsumoMedico(String codigo, String nombreAuditor,
            String cedulaAuditor);

    /**
     * Método para rechazar un pedido de insumo
     * 
     * @param codigo
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public PedidoInsumoMedico rechazarPedidoInsumoMedico(String codigo, String nombreAuditor,
            String cedulaAuditor);

}
