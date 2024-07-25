package com.uce.edu.repository.modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_paciente")
    @SequenceGenerator(name = "seq_paciente", sequenceName = "seq_paciente", allocationSize = 1)
    private Integer id;

    /**
     * Un paciente tiene muchas respuestas
     */

    @OneToMany(mappedBy = "paciente")
    private List<RespuestaPregunta> respuestasPregunta;

    /**
     * Un paciente tiene muchas citas medicas
     */
    @OneToMany(mappedBy = "paciente")
    private List<CitaMedica> citasMedicas;

    /**
     * Un paciente tiene muchas citasTemporales
     */
    @OneToMany(mappedBy = "paciente")
    private List<AgendaCitaTemporal> agendaCitaTemporals;

    /**
     * Un paciente tiene muchos pagos
     */
    @OneToMany(mappedBy = "paciente")
    private List<Pago> pagos;

    /**
     * Un paciente tiene muchas facturas
     */
    @OneToMany(mappedBy = "paciente")
    private List<FacturaCita> facturaCitas;

    /**
     * Un paciente tiene muchas ordenes de cobro
     */
    @OneToMany(mappedBy = "paciente")
    private List<OrdenCobro> ordenCobros;

    /**
     * Un paciente tiene muchas historias clinicas
     */
    @OneToMany(mappedBy = "paciente")
    private List<HistoriaClinica> historiaClinicas;

    /**
     * Un paciente tiene un codigo temporal
     */
    @OneToOne(mappedBy = "paciente")
    private CodigoTemporal codigoTemporal;

    @Column(name = "paci_nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "paci_apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "paci_cedula", length = 15, nullable = false, unique = true)
    private String cedula;

    @Column(name = "paci_correo", length = 50, nullable = false, unique = true)
    private String correo;

    @Column(name = "paci_telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "paci_direccion", length = 255, nullable = false)
    private String direccion;

    @Column(name = "paci_fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "paci_discapacidad", nullable = false)
    private Boolean discapacidad;

    @Column(name = "paci_tipo_discapacidad", length = 255, nullable = true)
    private String tipoDiscapacidad;

    @Column(name = "paci_genero", length = 20, nullable = false)
    private String genero;

    @Column(name = "paci_usuario", length = 15, nullable = false, unique = true)
    private String usuario;

    @Column(name = "paci_contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "paci_estado_seguridad", length = 15, nullable = false)
    private String estadoSeguridad;

    @Column(name = "paci_estado_Usuario", length = 15, nullable = false)
    private String estado;

}