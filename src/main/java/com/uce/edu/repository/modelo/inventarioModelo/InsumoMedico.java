package com.uce.edu.repository.modelo.inventarioModelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@Entity
@Table(name = "insumo_medico")
public class InsumoMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_insumo_medico")
    @SequenceGenerator(name = "seq_insumo_medico", sequenceName = "seq_insumo_medico")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "insumo")
    private List<PedidoInsumoMedico> pedidos;

    @Column(name = "insu_medi_codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Column(name = "insu_medi_nombre", length = 25, nullable = false)
    private String nombre;

    @Column(name = "insu_medi_descripcion", length = 250, nullable = false)
    private String descripcion;

    @Column(name = "insu_medi_precio_comptra", nullable = false)
   private BigDecimal precioCompra;

    @Column(name = "insu_medi_precio_venta", nullable = false)
    private BigDecimal precioVenta;

    @Column(name = "insu_medi_stock", nullable = false)
    private Integer stock;

    @Column(name = "insu_medi_nombre_auditor", length = 50)
    private String nombreAuditor;

    @Column(name = "insu_medi_cedula_auditor", length = 15)
    private String cedulaAuditor;

    @Column(name = "insu_medi_editor_auditor", length = 50)
    private String editorAuditor;

    @Column(name = "insu_medi_editor_cedula_auditor", length = 15)
    private String editorCedulaAuditor;

    @Column(name = "insu_medi_fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
