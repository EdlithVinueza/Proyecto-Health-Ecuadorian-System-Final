package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IPacienteRepository;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;

/**
 * EcryptServiceTest
 * 
 */
@SpringBootTest
@Rollback(true)
public class EcryptServiceTest {

    /**
     * Dependencias
     */
    @Autowired
    private IEncryptSercive encryptSercive;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IPacienteRepository pacienteRepository;

    @Autowired
    private IDoctorRepository doctorRepository;

    /**
     * Método que permite probar la encriptación de contraseñas
     */
    @Test
    public void testEncriptarContrasenia() {

        Doctor doctor = doctorRepository.findByCedula("1751674001");

        String contraseniaSinEncriptar = "1751674001";

        String contraseniaEncriptada = encryptSercive.encriptar(contraseniaSinEncriptar);
        doctor.setContrasena(contraseniaEncriptada);
        doctorRepository.save(doctor);
    }

    /**
     * Método que permite probar la comparación de contraseñas
     */
    @Test
    public void testCompararContrasenias() {

        Usuario usuario = usuarioService.buscarPorCedulaConTodosLosDatos("1234567890");

        String contraseniaSinEncriptar = "1234567890";

        Boolean igualdad = encryptSercive.comparar(contraseniaSinEncriptar, usuario.getContrasena());

        assertTrue(igualdad);

        System.out.println("Las contraseñas son iguales: " + igualdad);

    }

    /**
     * Método que permite probar la comparación de contraseñas
     */

    @Test
    public void testAuthenticateDoctor_AdminPassword() {
        String usuario = "1751674001";
        String contrasenia = "1751674001";
        Doctor doctorBD = doctorRepository.findByCedula(usuario);

        assertNotNull(doctorBD);

        Boolean igualdad = encryptSercive.comparar(contrasenia, doctorBD.getContrasena());
        assertTrue(igualdad);

    }

    /**
     * Método que permite probar la comparación de contraseñas
     */

    @Test
    public void testAuthenticatePaciente_AdminPassword() {
        String usuario = "1751678465";
        String contrasenia = "1751678465";
        Paciente doctorBD = pacienteRepository.findByCedula(usuario);
        doctorBD.setContrasena(encryptSercive.encriptar(contrasenia));
        doctorBD = pacienteRepository.save(doctorBD);

        assertNotNull(doctorBD);

        Boolean igualdad = encryptSercive.comparar(contrasenia, doctorBD.getContrasena());
        assertTrue(igualdad);

    }

}
