package com.uce.edu.service.interfacesSistemaPrincipal;

import java.util.List;

import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.dto.UsuarioDTO;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.service.to.RespuestasTO;
import com.uce.edu.service.to.UsuarioTO;
/**
 * IUsuarioService
 */
public interface IUsuarioService {

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @return
     */
    Usuario buscarPorCedula(String cedula);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @return
     */
    Usuario buscarPorCedulaConTodosLosDatos(String cedula);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param usuarioTO
     * @param preguntasTO
     * @return
     */
    Usuario guardarUsuarioNuevo(UsuarioTO usuarioTO, PreguntasTO preguntasTO);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param usuario
     * @return
     */
    UsuarioTO crearUsuarioTO(Usuario usuario);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @return
     */
    UsuarioTO crearUsuarioTOPorCedula(String cedula);

   
    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @return
     */
    List<UsuarioDTO> buscarTodoUsuarioDTO();
    
    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @return
     */
    UsuarioDTO buscarUsuarioDTOCedula(String cedula);

    /**
     *  Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @return
     */
    PreguntasTO buscarPreguntasUsuario(String cedula);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param preguntasTO
     * @return
     */
    Usuario actualizarPreguntasUsuario(String cedula, PreguntasTO preguntasTO);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param respuestasTO
     * @return
     */
    Usuario actualizarRespuestasUsuario(String cedula, RespuestasTO respuestasTO);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param preguntasTO
     * @param respuestasTO
     * @return
     */
    Usuario actualizarPreguntaRespuesta(String cedula, PreguntasTO preguntasTO,RespuestasTO respuestasTO);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param nuevaContrasenia
     */
    void actualizarContrasenia(String cedula, String nuevaContrasenia);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param usuarioTO
     * @return
     */
    Usuario actualizarUsuario(UsuarioTO usuarioTO);

 
    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param antiguaContrasenia
     * @return
     */
    boolean compararAntiguaContrasenia(String cedula, String antiguaContrasenia);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param antiguaContrasenia
     * @param nuevaContrasenia
     * @param repetirContrasenia
     * @return
     */
    boolean validarNuevaContrasenia(String antiguaContrasenia,String nuevaContrasenia, String repetirContrasenia);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param estado
     */
    void actualizarEstadoUsuario(String cedula, String estado);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param estado
     */
    public void actualizarSeguridadUsuario(String cedula, String estado);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @return
     */
    String nombreUsuario(String cedula);

    
    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param respuestasTO
     */
    void actualizarRespuestasNuevoUsuario(String cedula, RespuestasTO respuestasTO);

    /**
     * Método que obtiene todos los usuarios con todos los datos
     * @param cedula
     * @param nuevaContrasenia
     */
    void actualizarContraseniaUsuario(String cedula, String nuevaContrasenia);

    Usuario buscarPorUsuario(String usuario);



}
