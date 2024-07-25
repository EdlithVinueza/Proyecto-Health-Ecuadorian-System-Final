package com.uce.edu.service.interfacesSistemaPrincipal;

/**
 * IFacturaCitaService
 * 
 */
public interface IFacturaCitaService {
    /**
     * Método para generar un número de autorización
     * 
     * @return
     */
    String generarNumeroAutorizacion();

    /**
     * Método para generar un número de factura
     * 
     * @return
     */
    long generarNumeroFactura();

    /**
     * Método para generar un número de factura
     * 
     * @return
     */
    String generarNumeroParaFactura();

    /**
     * Método para contar las facturas
     * 
     * @return
     */
    long contarFacturas();

}
