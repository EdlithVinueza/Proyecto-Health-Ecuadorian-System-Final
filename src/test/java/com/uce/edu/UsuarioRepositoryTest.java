package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.dto.UsuarioDTO;

/**
 * UsuarioRepositoryTest
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UsuarioRepositoryTest {
    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioRepository usuarioRepository;

    /**
     * Metodo para probar la busqueda de un usuario por cedula o usuario
     */
    @Test
    public void testFindDtoByCedulaOrUsuario() {
        UsuarioDTO usuarioDTO = usuarioRepository.findDtoByCedulaOrUsuario("1234567890", "johndoe");
        assertNotNull(usuarioDTO);
        assertEquals("johndoe", usuarioDTO.getUsuario());
        assertEquals("1234567890", usuarioDTO.getCedula());
    }

}
