package com.uce.edu.service.inventarioService.interfaceService;

import java.util.List;

import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.to.inventarioTO.ProveedorTO;
/**
 * IProveedorService
 */
public interface IProveedorService {

    /**
     * Método para listar todos los proveedores
     * @return
     */
    public List<Proveedor> listarProveedores();

    /**
     * Método para buscar un proveedor por ruc
     * @param ruc
     * @return
     */
    public Proveedor buscarProveedorPorRuc(String ruc);
    
    /**
     * Método para buscar un proveedor por nombre de empresa
     * @param nombreEmpresa
     * @return
     */
    public Proveedor buscarPorNombreEmpresa(String nombreEmpresa);

    /**
     * Método para buscar un proveedor por ruc o nombre de empresa
     * @param ruc
     * @param nombreEmpresa
     * @return
     */
    public Proveedor buscarPorRucONombreEmpresa(String ruc, String nombreEmpresa);
    
    /**
     * Método para guardar un nuevo proveedor
     * @param proveedorTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public Proveedor guardaNuevoProveedor(ProveedorTO proveedorTO, String nombreAuditor,String cedulaAuditor);

    /**
     * Método para convertir un proveedor a ProveedorTO
     * @param ruc
     * @return
     */
    public ProveedorTO convertirProveedorTO(String ruc);

    /**
     * Método para actualizar un proveedor
     * @param proveedorTO
     * @param nombreAuditor
     * @param cedulaAuditor
     * @return
     */
    public Proveedor actualizaProveedor(ProveedorTO proveedorTO, String nombreAuditor,String cedulaAuditor);

}
