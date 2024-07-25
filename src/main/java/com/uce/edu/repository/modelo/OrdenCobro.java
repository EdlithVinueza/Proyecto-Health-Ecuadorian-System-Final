package com.uce.edu.repository.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orden_cobro")
public class OrdenCobro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_orden_cobro")
    @SequenceGenerator(name = "seq_orden_cobro", sequenceName = "seq_orden_cobro", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @OneToOne(mappedBy = "ordenCobro")
    private AgendaCitaTemporal agendaCitaTemporal;

    /* 
    @OneToOne(mappedBy = "ordenCobro")
    private FacturaCita facturaCita;

    @OneToOne(mappedBy = "ordenCobro")
    private Pago pago;

    @OneToOne(mappedBy = "ordenCobro")
    private CitaMedica citaMedica;
*/
    @Column(name = "orde_cobr_codigo", nullable = false, length = 30)
    private String codigo;

    @Column(name = "orde_cobr_fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "orde_cobr_fecha_limite_pago", nullable = false)
    private LocalDateTime fechaLimitePago;

    @Column(name = "orde_cobr_monto", nullable = false)
    private BigDecimal monto;

    @Column(name = "orde_cobr_estado", nullable = false)
    private String estado;

    @Column(name = "orde_cobr_observaciones", nullable = true)
    private String observaciones;

    @Column(name = "orde_cobr_nombre_auditor_emisor", nullable = true,length = 60)
    private String nombreAuiditorEmisor;

    @Column(name = "orde_cobr_cedula_auditor_emisor", nullable = true,length = 10)
    private String cedulaAuditorEmisor;

    @Column(name = "orde_cobr_nombre_auditor_cobrador", nullable = true,length = 60)
    private String nombreAuiditorCobrador;

    @Column(name = "orde_cobr_cedula_auditor_cobrador", nullable = true,length = 10)
    private String cedulaAuditorCobrador;

   

}