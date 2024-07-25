package com.uce.edu.repository.modelo;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "especialidad")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_especialidad")
    @SequenceGenerator(name = "seq_especialidad", sequenceName = "seq_especialidad", allocationSize = 1)
    private Integer id;



    /**
     * Una especialidad tiene muchos doctores
     */

    @OneToMany(mappedBy = "especialidad")
    private List<Doctor> doctores;

    /**
     * Una especialidad tiene muchas citas medicas
     */
    @OneToMany(mappedBy = "especialidad")
    private List<CitaMedica> citasMedicas;

    

    public Especialidad(
             String nombre,
           String descripcion,
          @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0") BigDecimal precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(min = 5, max = 150, message = "El nombre del rol debe tener entre 5 y 15 caracteres")
    @Column(name = "especialidad_nombre", length = 255, nullable = false, unique = true)
    private String nombre;

    @Size(max = 255, message = "La descripción del rol debe tener máximo 255 caracteres")
    @Column(name = "especialidad_descripcion", length = 255, nullable = true)
    private String descripcion;

    @NotNull(message = "El precio de la especialidad es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor que 0")
    @Column(name = "especialidad_precio", nullable = false)
    private BigDecimal precio;

    public Especialidad(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Especialidad(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Especialidad(String nombre) {
        this.nombre = nombre;
    }

    public Especialidad() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Doctor> getDoctores() {
        return doctores;
    }

    public void setDoctores(List<Doctor> doctores) {
        this.doctores = doctores;
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

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

}
