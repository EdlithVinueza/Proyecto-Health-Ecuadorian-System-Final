package com.uce.edu.service.interfacesSistemaPrincipal;

import com.uce.edu.repository.modelo.OrdenCobro;
/**
 * IOrdenCobroService
 */
public interface IOrdenCobroService  {

    /**
     * Método para generar caracteres aleatorios
     * @param longitud
     * @return
     */
    String generarCaracteresAleatorios(int longitud);
    /**
     * Método para generar un código único de orden de cobro
     * @return
     */
    String generarCodigoUnicoOrdenCobro();
    /**
     * Método para buscar una orden de cobro por código
     * @param ordenCobro
     * @return
     */
    public OrdenCobro buscarOrdenCobroPorCodigo(String ordenCobro);
    /**
     * Método para vareificar si una orden de cobro ha caducado
     * @param OrdenCobro
     * @return
     */
    public boolean validarCaducidadOrdenCobro(OrdenCobro OrdenCobro);
    /**
     * Método para varificar si una orden de cobro ha sido pagada
     * @param ordenCobro
     * @return
     */
    public boolean esOrdenCobroPagada(OrdenCobro ordenCobro) ;
    /**
     * Método para buscar una orden de cobro por id
     * @param id
     * @return
     */
    public OrdenCobro buscarOrdenCobroPorId(Integer id);
   
}
