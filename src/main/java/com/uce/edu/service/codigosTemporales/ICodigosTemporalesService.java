package com.uce.edu.service.codigosTemporales;

import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.to.ActualizarContraseniaRespuestasNuevasTO;
import com.uce.edu.service.to.ActualizarContraseniaTO;
import com.uce.edu.service.to.RespuestasTO;
/**
 * Interfaz para el servicio de códigos temporales
 */
public interface ICodigosTemporalesService {
    /**
     * Método que permite enviar un código al correo del usuario
     * 
     * @param cedulaUsuario
     * @param correo
     * @return
     */
    public Boolean enviarCodigoUsuario(String cedulaUsuario, String correo);

    /**
     * Método que permite actualizar la contraseña de un usuario
     * @param cedulaUsuario
     * @param respuestas
     * @return
     */
    public Boolean actualizarNuevoUsuario(String cedulaUsuario, ActualizarContraseniaRespuestasNuevasTO respuestas);

    /**
     * Método que permite actualizar la contraseña de un usuario
     * @param cedulaUsuario
     * @param respuestas
     * @return
     */
    public Boolean actualizarContraseniaUsuario(String cedulaUsuario, ActualizarContraseniaTO respuestas);

    /**
     * Método que permite validar si el código le pertenece al usuario
     * @param usuario
     * @param codigo
     * @return
     */
    boolean valdiarSiElCodigoLePerteneceAlUsuario(Usuario usuario, String codigo);

    /**
     * Método que permite validar si el código del usuario ha caducado
     * @param usuario
     * @return
     */
    boolean validarCaducidadCodigoUsuario(Usuario usuario);

    /**
     * Método que permite obtener las respuestas de un usuario
     * @param respuestas
     * @return
     */
    public RespuestasTO obtenerRespuestas(ActualizarContraseniaRespuestasNuevasTO respuestas);
}
