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
@Table(name ="medicamento")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medicamento")
    @SequenceGenerator(name = "seq_medicamento", sequenceName = "seq_medicamento")
    private Integer id;

    @ManyToOne
    @JoinColumn(name="proveedor_id")
    private Proveedor proveedor;
    
    @OneToMany(mappedBy = "medicamento")
    private List<PedidoMedicamento> pedidos;

    @Column(name = "medi_codigo", length = 10, nullable = false, unique = true)
    private String codigo;

    @Column(name ="medi_nombre_comun",length = 50, nullable = false)
    private String nombreComun;

    @Column(name ="medi_nombre_marca",length = 50, nullable = false, unique = true)
    private String nombreMarca;

    @Column(name ="medi_descripcion",length = 255, nullable = false)
    private String descripcion;

    @Column(name ="medi_precio_comptra", nullable = false)
    private BigDecimal precioCompra;

    @Column(name ="medi_precio_venta", nullable = false)
    private BigDecimal precioVenta;
    
    @Column(name ="medi_stock", nullable = false)
    private Integer stock;

    @Column(name = "medi_nombre_auditor",length = 50, nullable = false)
    private String nombreAuditor;

    @Column(name = "medi_cedula_auditor",length = 15, nullable = false)
    private String cedulaAuditor;

    @Column(name = "medi_editor_auditor",length = 50)
    private String editorAuditor;

    @Column(name = "medi_editor_cedula_auditor",length = 15)
    private String editorCedulaAuditor;

    @Column(name = "medi_fecha_modificacion")
    private LocalDateTime fechaModificacion;

    
}
