package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IRolRepository;
import com.uce.edu.repository.modelo.Rol;
import com.uce.edu.service.interfacesSistemaPrincipal.IRolService;

@Service
public class RolServiceImpl implements IRolService {

    @Autowired
    private IRolRepository rolRepository;

    @Override
    public Rol buscarUnRolPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    public List<Rol> buscarTodosLosRoles() {
        return rolRepository.findAll(); // Implementación del método para obtener todos los roles
    }

    @Override
    public Rol guardarRol(Rol rol) {
        try {
            return rolRepository.save(rol);
        } catch (DataIntegrityViolationException e) {
            // Retorna null en lugar de lanzar una excepción
            return null;
        }
    }

  

    /**
     * Método que se encarga de verificar y guardar roles que no existan.
     */
    @Override
    public boolean guardarRolSiNoEstaRepetido(Rol rol) {
        try {
            rolRepository.save(rol);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Retorna null en lugar de lanzar una excepción
            return false;
        }
    }

    /**
     * Método que se encarga de obtener un rol por su id.
     */
    @Override
    public Rol obtenerRolPorId(Integer id) {
       return rolRepository.findById(id).get();
    }

    @Override
    public Rol actualizarRol(Rol rol) {
       return rolRepository.save(rol);
    }

    @Override
    public void eliminarRolPorId(Integer id) {
        rolRepository.deleteById(id);
    }

    @Override
    public List<String> traerNombredeRoles() {
        List<Rol> roles = rolRepository.findAll();
        List<String> nombresDeRoles = roles.stream()
                                           .map(Rol::getNombre)
                                           .collect(Collectors.toList());
        return nombresDeRoles;
    }

}
