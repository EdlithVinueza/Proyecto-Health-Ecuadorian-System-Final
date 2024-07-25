package com.uce.edu.repository.modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_doctor")
    @SequenceGenerator(name = "seq_doctor", sequenceName = "seq_doctor", allocationSize = 1)
    private Integer id;

    /**
     * Un doctor tiene muchas respuestas
     * 
     */

    @OneToMany(mappedBy = "doctor")
    private List<RespuestaPregunta> respuestasPregunta;

    /**
     * Un doctor tiene una especialidad
     */
    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private Especialidad especialidad;

    /**
     * Un doctor tiene muchos horarios
     */
    @OneToMany(mappedBy = "doctor")
    private List<Horario> horarios;

    /**
     * Un doctor tiene muchas citas medicas
     */
    @OneToMany(mappedBy = "doctor")
    private List<CitaMedica> citasMedicas;

    /**
     * Un doctor tiene muchas historias clinicas
     */
    @OneToMany(mappedBy = "doctor")
    private List<HistoriaClinica> historiaClinicas;

     /**
     * Un doctor tiene un codigo temporal
     */
    @OneToOne(mappedBy = "doctor")
    private CodigoTemporal codigoTemporal;

    @Column(name = "doct_nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "doct_apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "doct_cedula", length = 15, nullable = false, unique = true)
    private String cedula;

    @Column(name = "doct_correo", length = 50, nullable = false, unique = true)
    private String correo;

    @Column(name = "doct_telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "doct_direccion", length = 255, nullable = false)
    private String direccion;

    @Column(name = "doct_fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "doct_estado", length = 15, nullable = false)
    private String estado;

    @Column(name = "doct_usuario", length = 15, nullable = false, unique = true)
    private String usuario;

    @Column(name = "doct_contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "doct_estado_seguridad", length = 15, nullable = false)
    private String estadoSeguridad;

   

}
