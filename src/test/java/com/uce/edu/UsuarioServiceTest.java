package com.uce.edu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.RespuestaPregunta;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.dto.UsuarioDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.service.to.RespuestasTO;
import com.uce.edu.service.to.UsuarioTO;

import jakarta.transaction.Transactional;

/**
 * UsuarioServiceTest
 * 
 */
@SpringBootTest
@Rollback(true)
public class UsuarioServiceTest {
    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IUsuarioService usuarioService;

    /**
     * Método que permite probar la búsqueda de un usuario por cédula
     */
    @Test
    public void testGuardarUsuarioNuevo() {
        // Preparar los datos de prueba
        UsuarioTO usuarioTO = new UsuarioTO();
        usuarioTO.setRol("Admin");
        usuarioTO.setNombre("John");
        usuarioTO.setApellido("Doe");
        usuarioTO.setCedula("1234567890");
        usuarioTO.setCorreo("john.doe@example.com");
        usuarioTO.setTelefono("1234567890");
        usuarioTO.setDireccion("123 Main St");
        usuarioTO.setFechaNacimiento(LocalDate.of(1990, 1, 1));
        usuarioTO.setUsuario("johndoe");

        PreguntasTO preguntasTO = new PreguntasTO();
        preguntasTO.setPreguntaUno("¿Cuál es tu color favorito?");
        preguntasTO.setPreguntaDos("¿Cuál es tu comida favorita?");
        preguntasTO.setPreguntaTres("¿Como se llama tu madre?");

        Usuario usuario = usuarioService.guardarUsuarioNuevo(usuarioTO, preguntasTO);

        // Verificar el resultado
        assertEquals("John", usuario.getNombre());
        assertEquals("Doe", usuario.getApellido());
        assertEquals("1234567890", usuario.getCedula());
        assertEquals("john.doe@example.com", usuario.getCorreo());
        assertEquals("1234567890", usuario.getTelefono());
        assertEquals("123 Main St", usuario.getDireccion());
        assertEquals("johndoe", usuario.getUsuario());
        assertEquals("password", usuario.getContrasena());
    }

    /**
     * Método que permite probar la búsqueda de un usuario por cédula
     */
    @Test
    public void testActualizarRespuestasUsuario() {
        String cedula = "1234567890";
        RespuestasTO respuestasTO = new RespuestasTO();
        respuestasTO.setRespuestaUno("Rojo");
        respuestasTO.setRespuestaDos("Pizza");
        respuestasTO.setRespuestaTres("Maria");
        Usuario usuario = usuarioService.actualizarRespuestasUsuario(cedula, respuestasTO);
        assertEquals("Rojo", usuario.getRespuestasPregunta().get(0).getRespuesta());
        assertEquals("Pizza", usuario.getRespuestasPregunta().get(1).getRespuesta());
        assertEquals("Maria", usuario.getRespuestasPregunta().get(2).getRespuesta());
        for (RespuestaPregunta respuesta : usuario.getRespuestasPregunta()) {
            System.out.println("Pregunta ID: " + respuesta.getPreguntaRecuperacion().getId());
            System.out.println("Pregunta: " + respuesta.getPreguntaRecuperacion().getPregunta());
        }
    }

    /**
     * Método que permite ver las preguntas y respuestas de un usuario
     */
    @Test
    public void verPreguntaRespuesta() {
        String cedula = "1234567890";
        Usuario usuario = usuarioService.buscarPorCedulaConTodosLosDatos(cedula);
        System.out.println("Ver Pregunta Respuesta");
        for (RespuestaPregunta respuesta : usuario.getRespuestasPregunta()) {
            System.out.println("Pregunta: " + respuesta.getPreguntaRecuperacion().getPregunta());
            System.out.println("Respuesta: " + respuesta.getRespuesta());
        }
    }

    /**
     * Método que permite probar la actualización de las preguntas de recuperación
     * de un usuario
     */
    @Test
    public void actualizarPreguntasUsuario() {
        String cedula = "1234567890";
        PreguntasTO preguntasTO = new PreguntasTO();
        preguntasTO.setPreguntaTres("¿Cuál es tu color favorito?");
        preguntasTO.setPreguntaDos("¿Cuál es tu comida favorita?");
        preguntasTO.setPreguntaUno("¿Como se llama tu madre?");
        Usuario usuario = usuarioService.actualizarPreguntasUsuario(cedula, preguntasTO);
        System.out.println("Preguntas Actualizadas");
        for (RespuestaPregunta respuesta : usuario.getRespuestasPregunta()) {
            System.out.println("Pregunta: " + respuesta.getPreguntaRecuperacion().getPregunta());
            System.out.println("Respuesta: " + respuesta.getRespuesta());
        }
    }

    /**
     * Método que permite probar la conversión de un usuario a un UsuarioTO
     */
    @Test
    @Transactional
    void testCrearUsuarioTO() {
        Usuario usuario = usuarioRepository.findByCedula("1234567890");
        usuario.getRespuestasPregunta().size();
        UsuarioTO usuarioTO = usuarioService.crearUsuarioTO(usuario);
        assertEquals("Admin", usuarioTO.getRol());
        assertEquals("John", usuarioTO.getNombre());
        assertEquals("1234567890", usuarioTO.getCedula());

    }

    /**
     * Método que permite probar la creación de un usuario a partir de una cédula
     */
    @Test
    void testCrearUsuarioTOPorCedula() {
        String cedula = "1234567890";
        UsuarioTO usuarioTO = usuarioService.crearUsuarioTOPorCedula(cedula);
        assertEquals("Admin", usuarioTO.getRol());
        assertEquals("John", usuarioTO.getNombre());
        assertEquals("1234567890", usuarioTO.getCedula());

    }

    /**
     * Método para ver todos los usuarios
     */
    @Test
    public void testFindAllAsDTO() {
        List<UsuarioDTO> usuariosDTO = usuarioRepository.findAllAsDTO();
        assertNotNull(usuariosDTO);
        assertFalse(usuariosDTO.isEmpty());
        assertNotNull(usuariosDTO.get(0));

    }

    @Test
    public void buscarPreguntasUsuario() {
        String cedula = "1234567890";
        PreguntasTO preguntasTO = usuarioService.buscarPreguntasUsuario(cedula);
        System.out.println(preguntasTO.getPreguntaUno());
        System.out.println(preguntasTO.getPreguntaDos());
        System.out.println(preguntasTO.getPreguntaTres());
    }

    @Test
    public void actualizarContrasenia() {
        String cedula = "1234567890";
        usuarioService.actualizarContrasenia(cedula, cedula);
    }
}
