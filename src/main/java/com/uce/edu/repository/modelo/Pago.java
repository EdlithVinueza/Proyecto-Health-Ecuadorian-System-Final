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
import jakarta.validation.constraints.DecimalMax;
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
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pago")
    @SequenceGenerator(name = "seq_pago", sequenceName = "seq_pago", allocationSize = 1)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @OneToOne(mappedBy = "pago")
    private FacturaCita facturaCita;
/* 
    @OneToOne
    @JoinColumn(name = "orden_cobro_id", nullable = true)
    private OrdenCobro ordenCobro;
*/
    @Column(name = "pago_fecha_pago", nullable = true)
    private LocalDateTime fechaPago;

    @Column(name = "pago_hora_pago", nullable = true)
    private LocalTime horaPago;

    @NotNull(message = "El monto de pago no puede ser nulo.")
    @DecimalMin(value = "0.01", message = "El monto de pago debe ser mayor que cero.")
    @DecimalMax(value = "9999999.99", message = "El monto de pago excede el máximo permitido.")
    @Column(name = "pago_monto_pago", nullable = true)
    private BigDecimal montoPago;

    @Column(name = "pago_metodo_pago", nullable = true)
    private String metodoPago;


    @Column(name = "pago_auditoria_cobrador", nullable = true)
    private String auditoriaCobrador;

    @Column(name = "pago_cedula_cobrador", nullable = true)
    private String cedulaCobrador;

   
    @Column(name = "pago_auditoria_estado", nullable = true)
    private String estado;


    

}