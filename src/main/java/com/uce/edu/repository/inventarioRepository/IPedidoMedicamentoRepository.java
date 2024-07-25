package com.uce.edu.repository.inventarioRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.inventarioModelo.PedidoMedicamento;

public interface IPedidoMedicamentoRepository extends JpaRepository<PedidoMedicamento, Integer>{

    PedidoMedicamento findByCodigo(String codigo);

    List<PedidoMedicamento> findByEstado(String estado);

    @Query("SELECT p FROM PedidoMedicamento p WHERE p.proveedor.ruc = :ruc OR p.proveedor.nombreEmpresa = :nombreEmpresa")
    List<PedidoMedicamento> findByProveedorRucOrNombreEmpresa(@Param("ruc") String ruc, @Param("nombreEmpresa") String nombreEmpresa);

    @Query("SELECT p FROM PedidoMedicamento p WHERE p.medicamento.codigo = :codigo")
    List<PedidoMedicamento> findByMedicamentoCodigo(@Param("codigo") String codigo);

    @Query("SELECT p FROM PedidoMedicamento p WHERE p.medicamento.nombreComun = :nombreComun")
    List<PedidoMedicamento> findByMedicamentoNombreComun(@Param("nombreComun") String nombreComun);

    @Query("SELECT p FROM PedidoMedicamento p WHERE p.medicamento.nombreMarca = :nombreMarca")
    List<PedidoMedicamento> findByMedicamentoNombreMarca(@Param("nombreMarca") String nombreMarca);


   


}
