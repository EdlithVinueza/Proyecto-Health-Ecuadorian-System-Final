package com.uce.edu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IRolRepository;
import com.uce.edu.repository.modelo.Rol;

/**
 * RolRepositoryTest
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)

public class RolRepositoryTest {
    /**
     * Dependencias
     */
    @Autowired
    private IRolRepository rolRepository;

    /**
     * Test para crear un rol
     */
    @Test
    public void testCreateRol() {
        Rol rolAdmin = new Rol("Admin", "Administrador");
        Rol rolUser = new Rol("User", "Usuario");
        Rol rolGuest = new Rol("Guest", "Invitado");

        rolRepository.save(rolAdmin);
        rolRepository.save(rolUser);
        rolRepository.save(rolGuest);
        /**
         * Se verifica que se hayan creado los roles
         */
        Iterable<Rol> roles = rolRepository.findAll();
        assertThat(roles).hasSize(3);
    }

    /**
     * Test para buscar un rol por nombre
     */
    @Test
    public void testFindRolByName() {
        String nombre = "Admin";
        Rol rol = rolRepository.findByNombre(nombre);
        /**
         * Se verifica que el rol no sea nulo
         */
        assertThat(rol).isNotNull();
    }

    /**
     * Test para buscar un rol por nombre
     */
    @Test
    public void testFindRolAllName() {
        String nombre = "x";
        Iterable<Rol> roles = rolRepository.findAllByNombre(nombre);
        /**
         * Se verifica que se hayan encontrado roles
         */
        assertThat(roles).isNotEmpty();
    }

    /**
     * Test para actualizar un rol
     */
    @Test
    public void testUpdateRol() {
        String nombreActual = "Guest";
        String nombreNuevo = "Secre";
        Rol rol = rolRepository.findByNombre(nombreActual);
        rol.setNombre(nombreNuevo);
        rol.setDescripcion("Secretario");
        rolRepository.save(rol);
        /**
         * Se verifica que el rol se haya actualizado
         */
        Rol rolUpdated = rolRepository.findByNombre(nombreNuevo);
        assertThat(rolUpdated.getNombre()).isEqualTo(nombreNuevo);
    }

    /**
     * Test para eliminar un rol
     */
    @Test
    public void testDeleteRol() {
        String nombre = "User";
        Rol rol = rolRepository.findByNombre(nombre);
        rolRepository.delete(rol);
        /**
         * Se verifica que el rol se haya eliminado
         */
        Rol rolDeleted = rolRepository.findByNombre(nombre);
        assertThat(rolDeleted).isNull();
    }

    /**
     * Test para obtener todos los roles
     */
    @Test
    public void testGetRoles() {
        Iterable<Rol> roles = rolRepository.findAll();
        /**
         * Se verifica que se hayan obtenido los roles
         */
        assertThat(roles).isNotEmpty();
    }

    /**
     * Test para insertar un solo rol en la tabla
     */
    @Test
    public void testInsertRol() {
        Rol rol = new Rol("Admin", "Administrador");
        rolRepository.save(rol);
        /**
         * Se verifica que se haya insertado un solo rol
         */
        Iterable<Rol> roles = rolRepository.findAll();
        assertThat(roles).hasSize(1);
    }

}
