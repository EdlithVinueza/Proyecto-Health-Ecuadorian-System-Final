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

@Entity
@Table(name = "consultorio")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_consultorio")
    @SequenceGenerator(name = "seq_consultorio", sequenceName = "seq_consultorio", allocationSize = 1)
    private Integer id;
    /**
     * Un consultorio tiene muchos horarios
     * 
     */
    @OneToMany(mappedBy = "consultorio")
    private List<Horario> horarios;

    /**
     * Un consultorio tiene muchas citas medicas
     */
    @OneToMany(mappedBy = "consultorio")
    private List<CitaMedica> citasMedicas;

    @NotBlank(message = "El nombre del consultorio es obligatorio")
    @Column(name = "cons_nombre", length = 50, nullable = false)
    private String nombre;
    @NotBlank(message = "El numero del consultorio es obligatoria")
    @Column(name = "cons_numero", length = 50, nullable = false)
    private String numero;
    @NotBlank(message = "El piso del consultorio es obligatorio")
    @Column(name = "cons_piso", length = 50, nullable = false)
    private String piso;
    @NotBlank(message = "La especialidad del consultorio es obligatoria")
    @Column(name = "cons_especialidad", length = 50, nullable = false)
    private String especialidad;
    @NotBlank(message = "El codigo del consultorio es obligatorio")
    @Column(name = "cons_codigo", length = 50, nullable = false, unique = true)
    private String codigo;

    public Consultorio() {
    }

    public Consultorio(String nombre,
            String numero,
            String piso,
            String especialidad,
            String codigo) {
        this.nombre = nombre;
        this.numero = numero;
        this.piso = piso;
        this.especialidad = especialidad;
        this.codigo = codigo;
    }

    public Consultorio(Integer id, List<Horario> horarios, List<CitaMedica> citasMedicas,
            String nombre,
            String numero,
            String piso,
            String especialidad,
            String codigo) {
        this.id = id;
        this.horarios = horarios;
        this.citasMedicas = citasMedicas;
        this.nombre = nombre;
        this.numero = numero;
        this.piso = piso;
        this.especialidad = especialidad;
        this.codigo = codigo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public List<CitaMedica> getCitasMedicas() {
        return citasMedicas;
    }

    public void setCitasMedicas(List<CitaMedica> citasMedicas) {
        this.citasMedicas = citasMedicas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
