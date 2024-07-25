package com.uce.edu.repository.modelo.inventarioModelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Builder

@Entity
@Table(name = "pedido_medicamento")
public class PedidoMedicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pedido_medicamento")
    @SequenceGenerator(name = "seq_pedido_medicamento", sequenceName = "seq_pedido_medicamento", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="medicamento_id")
    private Medicamento medicamento;

    @ManyToOne
    @JoinColumn(name="proveedor_id")
    private Proveedor proveedor;

    @Column(name = "pedi_medi_codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Column(name = "pedi_medi_cantidad", nullable = false)
   private Integer cantidad;

    @Column(name = "pedi_medi_fecha_pedido", nullable = false)
    private LocalDateTime fechaPedido;

    @Column(name = "pedi_medi_estado",length = 25,nullable = false)
    private String estado;

    @Column(name = "pedi_medi_total",nullable = false)
    private BigDecimal total;

    @Column(name ="pedi_medi_nombre_auditor",length = 50, nullable = false)
    private String nombreAuditor;

    @Column(name ="pedi_medi_cedula_auditor",length = 15, nullable = false)
    private String cedulaAuditor;

    @Column(name ="pedi_medi_editor_auditor",length = 50, nullable = true)
    private String editorAuditor;

    @Column(name ="pedi_medi_editor_cedula",length = 15, nullable = true)
    private String editorCedula;

    @Column(name ="pedi_medi_fecha_modificacion",nullable = true)
    private LocalDateTime fechaModificacion;

}
