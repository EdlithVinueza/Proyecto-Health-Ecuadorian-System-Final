package com.uce.edu.repository.interfacesSistemaGeneral;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.dto.UsuarioDTO;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
        Usuario findByCedula(String cedula);

        Usuario findByUsuario(String usuario);

        @Query("SELECT new com.uce.edu.repository.modelo.dto.UsuarioDTO(u.usuario, u.cedula) FROM Usuario u WHERE u.cedula = :cedula OR u.usuario = :usuario")
        UsuarioDTO findDtoByCedulaOrUsuario(@Param("cedula") String cedula, @Param("usuario") String usuario);

        @Query("SELECT new com.uce.edu.repository.modelo.dto.UsuarioDTO(u.id, u.cedula, u.usuario, u.correo, u.telefono, u.rol.nombre, u.estadoUsuario) "
                        +
                        "FROM Usuario u")
        List<UsuarioDTO> findAllAsDTO();

        @Query("SELECT new com.uce.edu.repository.modelo.dto.UsuarioDTO(u.id, u.cedula, u.usuario, u.correo, u.telefono, u.rol.nombre, u.estadoUsuario) "
                        +
                        "FROM Usuario u WHERE u.cedula = :cedula")
        UsuarioDTO findByCedulaAsDTO(@Param("cedula") String cedula);
}
