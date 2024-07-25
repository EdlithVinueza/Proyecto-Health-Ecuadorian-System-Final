package com.uce.edu.repository.interfacesSistemaGeneral;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uce.edu.repository.modelo.OrdenCobro;

public interface IOrdenCobroRepository extends JpaRepository<OrdenCobro, Integer>{
    OrdenCobro findByCodigo(String codigo);
}