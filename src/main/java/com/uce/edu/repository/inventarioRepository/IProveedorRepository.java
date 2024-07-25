package com.uce.edu.repository.inventarioRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;

public interface IProveedorRepository extends JpaRepository<Proveedor, Integer> {

    Proveedor findByRuc(String ruc);

    Proveedor findByNombreEmpresa(String nombreEmpresa);

    @Query("SELECT p FROM Proveedor p WHERE p.ruc = ?1 OR p.nombreEmpresa = ?2")
    Proveedor findByRucOrNombreEmpresa(String ruc, String nombreEmpresa);

}
