package com.uce.edu.repository.modelo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class UsuarioDTO {

    private Integer id;
    private String cedula;
    private String usuario;
    private String correo;
    private String telefono;
    private String rol;
    private String estadoUsuario;

    public UsuarioDTO(Integer id, String cedula, String usuario, String correo, String telefono, String rol,
            String estadoUsuario) {
        this.id = id;
        this.cedula = cedula;
        this.usuario = usuario;
        this.correo = correo;
        this.telefono = telefono;
        this.rol = rol;
        this.estadoUsuario = estadoUsuario;
    }
    

  
  
    

}
