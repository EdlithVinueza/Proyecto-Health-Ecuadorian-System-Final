package com.uce.edu.repository.modelo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_rol")
    @SequenceGenerator(name = "seq_rol", sequenceName = "seq_rol", allocationSize = 1)
    private Integer id;

    /**
     * Un rol tiene muchos usuarios
     */
    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 3, max = 15, message = "El nombre del rol debe tener entre 5 y 15 caracteres")
    @Column(name = "rol_nombre", length = 15, nullable = false, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción del rol debe tener máximo 255 caracteres")
    @Column(name = "rol_descripcion", length = 255, nullable = true)
    private String descripcion;

    public Rol(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Rol(String nombre) {
        this.nombre = nombre;
    }

    public Rol() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
