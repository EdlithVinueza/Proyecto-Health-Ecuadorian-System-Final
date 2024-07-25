package com.uce.edu.service.encriptacionService;
/**
 * Interfaz para el servicio de encriptación
 */
public interface IEncryptSercive {
    /**
     * Método para encriptar una contraseña
     * 
     * @param contraseniaSinEncriptar
     * @return
     */
    String encriptar(String contraseniaSinEncriptar);

    /**
     * Método para comparar una contraseña encriptada con una sin encriptar
     * 
     * @param contraseniaSinEncriptar
     * @param contraseniaEncriptado
     * @return
     */
    boolean comparar(String contraseniaSinEncriptar, String contraseniaEncriptado);

}
