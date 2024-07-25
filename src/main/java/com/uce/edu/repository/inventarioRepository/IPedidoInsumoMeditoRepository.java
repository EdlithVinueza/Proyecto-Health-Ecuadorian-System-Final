package com.uce.edu.repository.inventarioRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.inventarioModelo.PedidoInsumoMedico;

public interface IPedidoInsumoMeditoRepository extends JpaRepository<PedidoInsumoMedico, Integer> {

    PedidoInsumoMedico findByCodigo(String codigo);

    List<PedidoInsumoMedico> findByEstado(String estado);

    @Query("SELECT p FROM PedidoInsumoMedico p WHERE p.proveedor.ruc = :ruc OR p.proveedor.nombreEmpresa = :nombreEmpresa")
    List<PedidoInsumoMedico> findByProveedorRucOrNombreEmpresa(@Param("ruc") String ruc, @Param("nombreEmpresa") String nombreEmpresa);

    @Query("SELECT p FROM PedidoInsumoMedico p WHERE p.insumo.codigo = :codigo")
    List<PedidoInsumoMedico> findByInsumoCodigo(@Param("codigo") String codigo);

    @Query("SELECT p FROM PedidoInsumoMedico p WHERE p.insumo.nombre = :nombre")
    List<PedidoInsumoMedico> findByInsumoNombre(@Param("nombre") String nombre);

}
