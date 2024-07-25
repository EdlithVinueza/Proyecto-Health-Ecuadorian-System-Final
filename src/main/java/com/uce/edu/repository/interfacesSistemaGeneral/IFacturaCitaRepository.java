package com.uce.edu.repository.interfacesSistemaGeneral;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uce.edu.repository.modelo.FacturaCita;

public interface IFacturaCitaRepository extends JpaRepository<FacturaCita, Integer>{

        FacturaCita findByNumeroFactura(String numeroFactura);
    
}
