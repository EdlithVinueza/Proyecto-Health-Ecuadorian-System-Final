package com.uce.edu.repository.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "factura_cita")
public class FacturaCita {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_factura_cita")
    @SequenceGenerator(name = "seq_factura_cita", sequenceName = "seq_factura_cita", allocationSize = 1)
    private Integer id;
  
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "cita_medica_id", nullable = true)
    private CitaMedica citaMedica;

    /* 
    @OneToOne
    @JoinColumn(name = "orden_cobro_id", nullable = true)
    private OrdenCobro ordenCobro;
*/
    @OneToOne
    @JoinColumn(name = "pago_id", nullable = true)
    private Pago pago;

    @Column(name = "fact_cita_numero_autorizacion", nullable = false, length = 100, unique = true)
    private String numeroAutorizacion;

    @Column(name = "fact_cita_numero_factura", nullable = false, length = 20,unique = true)
    private String numeroFactura;

    @Column(name = "fact_cita_fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "fact_cita_hora_emision", nullable = false)
    private LocalTime horaEmision;

    @Column(name = "fact_cita_cedula_paciente", nullable = false,length = 10)
    private String CedulaPaciente;

    @Column(name = "fact_cita_nombre_cliente", nullable = false,length = 60)
    private String nombreCliente;

    @Column(name = "fact_cita_direccion_paciente", nullable = false)
    private String direccionPaciente;

    @Column(name = "fact_cita_telefono_paciente", nullable = false, length = 10)
    private String telefonoPaciente;

    @Column(name = "fact_cita_correo_paciente", nullable = false)
    private String correoPaciente;

    @Column(name = "fact_cita_especialidad", nullable = false)
    private String especialidad;

    @Column(name = "fact_cita_nombre_auditor", nullable = false,length = 60)
    private String nombreAuditor;

    @Column(name = "fact_cita_cedula_auditor", nullable = false,length = 10)
    private String cedulaAuditor;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "fact_cita_iva", nullable = false)
    private BigDecimal iva;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "fact_cita_descuentos", nullable = false)
    private BigDecimal descuentos;

    @NotNull
    @DecimalMin("0.00")
    @Column(name = "fact_cita_subTotal", nullable = false)
    private BigDecimal subTotal;

    @NotNull
    @DecimalMin("0.01")
    @Column(name = "fact_cita_total_pagar", nullable = false)
    private BigDecimal totalPagar;

    @Column(name = "fact_cita_observaciones", nullable = true)
    private String observaciones;


    

}