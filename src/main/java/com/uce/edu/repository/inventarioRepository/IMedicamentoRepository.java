package com.uce.edu.repository.inventarioRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;

public interface IMedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    Medicamento findByCodigo(String codigo);

    List<Medicamento> findByNombreComun(String nombreComun);

    Medicamento findByNombreMarca(String nombreMarca);

    @Query("SELECT m FROM Medicamento m JOIN m.proveedor p WHERE p.ruc = :ruc OR p.nombreEmpresa = :nombreEmpresa")
    List<Medicamento> findByProveedorRucOrNombreEmpresa(String ruc, String nombreEmpresa);

}
