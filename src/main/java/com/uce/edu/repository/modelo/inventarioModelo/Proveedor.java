package com.uce.edu.repository.modelo.inventarioModelo;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name ="proveedor")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_proveedor")
    @SequenceGenerator(name = "seq_proveedor", sequenceName = "seq_proveedor", allocationSize = 1)
    private Integer id;

    @OneToMany(mappedBy = "proveedor")
    private List<Medicamento> medicamentos;

    @OneToMany(mappedBy = "proveedor")
    private List<PedidoMedicamento> pedidosMedicamentos;
    
    @OneToMany(mappedBy = "proveedor")
    private List<InsumoMedico> insumos;

    @OneToMany(mappedBy = "proveedor")
    private List<PedidoInsumoMedico> pedidosInsumos;

    @Column(name= "prov_ruc", length = 13, nullable = false,unique = true)
    private String ruc;

    @Column(name= "prov_nombre_empresa",length = 15, nullable = false, unique = true)
    private String nombreEmpresa; 
    
    @Column(name= "prov_direccion",length = 250, nullable = false)
    private String direccion;

    @Column(name= "prov_telefono",length = 15, nullable = false)
    private String telefono;

    @Column(name = "prov_correo", length = 50, nullable = false, unique = true)
    private String correo;

    @Column(name ="prov_registro_nombre_auditor",length = 50, nullable = false)
    private String registroNombreAuditor;

    @Column(name ="prov_registro_cedula_auditor",length = 15, nullable = false)
    private String registroCedulaAuditor;

    @Column(name="prov_actualizacion_auditor",length = 50)
    private String actualizacionAuditor;

    @Column(name="prov_Actualizacion_cedula_auditor",length = 15)
    private String actualizacionCedulaAuditor;

    @Column(name="prov_fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public class builder {

        public Object ruc(String ruc) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'ruc'");
        }
    }




    

    
}
