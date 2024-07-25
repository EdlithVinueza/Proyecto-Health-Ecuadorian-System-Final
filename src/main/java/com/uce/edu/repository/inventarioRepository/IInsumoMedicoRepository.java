package com.uce.edu.repository.inventarioRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uce.edu.repository.modelo.inventarioModelo.InsumoMedico;

public interface IInsumoMedicoRepository extends JpaRepository<InsumoMedico, Long>{
    InsumoMedico findByCodigo(String codigo);
    List<InsumoMedico> findByNombre(String nombre);
    @Query("SELECT im FROM InsumoMedico im JOIN im.proveedor p WHERE p.ruc = :ruc OR p.nombreEmpresa = :nombreEmpresa")
    List<InsumoMedico> findByProveedorRucOrNombreEmpresa(String ruc, String nombreEmpresa);
}
