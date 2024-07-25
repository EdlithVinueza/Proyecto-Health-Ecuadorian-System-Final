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

@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    private Integer id;

    /**
     * Un usuario tiene un rol
     */
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    /**
     * Un usuario tiene muchas respuestas
     */
    @OneToMany(mappedBy = "usuario")
    private List<RespuestaPregunta> respuestasPregunta;

    /**
     * Un usuario tiene un codigo temporal
     */
    @OneToOne(mappedBy = "usuario")
    private CodigoTemporal codigoTemporal;

    @Column(name = "usua_nombre", length = 50, nullable = false)
    private String nombre;

    @Column(name = "usua_apellido", length = 50, nullable = false)
    private String apellido;

    @Column(name = "usua_cedula", length = 15, nullable = false, unique = true)
    private String cedula;

    @Column(name = "usua_correo", length = 50, nullable = false, unique = true)
    private String correo;

    @Column(name = "usua_telefono", length = 15, nullable = false)
    private String telefono;

    @Column(name = "usua_direccion", length = 255, nullable = false)
    private String direccion;

    @Column(name = "usua_fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "usua_usuario", length = 15, nullable = false, unique = true)
    private String usuario;

    @Column(name = "usua_contrasena", length = 255, nullable = false)
    private String contrasena;

    @Column(name = "usua_estado_seguridad", length = 15, nullable = false)
    private String estadoSeguridad;

    @Column(name = "usua_estado_Usuario", length = 15, nullable = false)
    private String estadoUsuario;

}
