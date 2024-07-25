package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uce.edu.repository.modelo.Rol;

public interface IRolRepository extends JpaRepository<Rol, Integer>{
    Rol findByNombre(String nombre);
    @SuppressWarnings("null")
    List<Rol> findAll(); 
    List<Rol> findAllByNombre(String nombre);

}
