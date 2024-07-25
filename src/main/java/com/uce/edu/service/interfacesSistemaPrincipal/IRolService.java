package com.uce.edu.service.interfacesSistemaPrincipal;

import java.util.List;

import com.uce.edu.repository.modelo.Rol;

/**
 * IRolService
 */
public interface IRolService {

  /**
   * Método para buscar un rol por nombre
   * 
   * @param nombre
   * @return
   */
  Rol buscarUnRolPorNombre(String nombre);

  /**
   * Método para buscar todos los roles
   * 
   * @return
   */
  List<Rol> buscarTodosLosRoles();

  /**
   * Método para traer el nombre de los roles
   */
  List<String> traerNombredeRoles();

  /**
   * Método para guardar un rol
   * 
   * @param rol
   * @return
   */
  Rol guardarRol(Rol rol);

  /**
   * Método para guardar un rol si no esta repetido
   * 
   * @param rol
   * @return
   */
  boolean guardarRolSiNoEstaRepetido(Rol rol);

  /**
   * Método para obtener un rol por id
   * 
   * @param id
   * @return
   */
  Rol obtenerRolPorId(Integer id);

  /**
   * Método para actualizar un rol
   * 
   * @param rol
   * @return
   */
  Rol actualizarRol(Rol rol);

  /**
   * Método para eliminar un rol por id
   * 
   * @param id
   */
  void eliminarRolPorId(Integer id);

}
