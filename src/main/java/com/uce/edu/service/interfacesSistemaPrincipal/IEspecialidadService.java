package com.uce.edu.service.interfacesSistemaPrincipal;

import java.util.List;

import com.uce.edu.repository.modelo.Especialidad;

/**
 * Interfaz para el servicio de especialidades
 */
public interface IEspecialidadService {
    /**
     * Método para guardar una especialidad
     * 
     * @param nombre
     * @return
     */
    Especialidad buscarPorEspecialidadPorNombre(String nombre);

    /**
     * Método para buscar todas las especialidades
     * 
     * @return
     */
    List<String> buscarNombresEspecialidades();
}
