package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IPreguntaRecuperacionRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IRespuestaPreguntaRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.PreguntaRecuperacion;
import com.uce.edu.repository.modelo.RespuestaPregunta;
import com.uce.edu.repository.modelo.Rol;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.dto.UsuarioDTO;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.PreguntasTO;
import com.uce.edu.service.to.RespuestasTO;
import com.uce.edu.service.to.UsuarioTO;
import com.uce.edu.util.Enum.EstadoSeguridad;
import com.uce.edu.util.Enum.EstadoUsuario;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private RolServiceImpl rolService;

    @Autowired
    private IPreguntaRecuperacionRepository preguntaRecuperacionRepository;

    @Autowired
    private IRespuestaPreguntaRepository respuestaPreguntaRepository;

    @Autowired
    private IEncryptSercive encryptSercive;

    /**
     * Método que busca un usuario por su cédula
     */
    @Override
    public Usuario buscarPorCedula(String cedula) {
        return usuarioRepository.findByCedula(cedula);
    }

    @Override
    public UsuarioTO crearUsuarioTO(Usuario usuario) {
        UsuarioTO usuarioTO = new UsuarioTO();
        usuarioTO.setRol(usuario.getRol().getNombre());
        usuarioTO.setNombre(usuario.getNombre());
        usuarioTO.setApellido(usuario.getApellido());
        usuarioTO.setCedula(usuario.getCedula());
        usuarioTO.setCorreo(usuario.getCorreo());
        usuarioTO.setTelefono(usuario.getTelefono());
        usuarioTO.setDireccion(usuario.getDireccion());
        usuarioTO.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioTO.setUsuario(usuario.getUsuario());
        return usuarioTO;

    }

    @Override
    @Transactional
    public UsuarioTO crearUsuarioTOPorCedula(String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        usuario.getRespuestasPregunta().size();
        UsuarioTO usuarioTO = new UsuarioTO();
        usuarioTO.setRol(usuario.getRol().getNombre());
        usuarioTO.setNombre(usuario.getNombre());
        usuarioTO.setApellido(usuario.getApellido());
        usuarioTO.setCedula(usuario.getCedula());
        usuarioTO.setCorreo(usuario.getCorreo());
        usuarioTO.setTelefono(usuario.getTelefono());
        usuarioTO.setDireccion(usuario.getDireccion());
        usuarioTO.setFechaNacimiento(usuario.getFechaNacimiento());
        usuarioTO.setUsuario(usuario.getUsuario());
        return usuarioTO;

    }

    @Override
    @Transactional
    public Usuario buscarPorCedulaConTodosLosDatos(String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        usuario.getRespuestasPregunta().size();
        return usuario;
    }

    @Override
    public List<UsuarioDTO> buscarTodoUsuarioDTO() {

        return usuarioRepository.findAllAsDTO();
    }

    @Override
    public UsuarioDTO buscarUsuarioDTOCedula(String cedula) {
        return usuarioRepository.findByCedulaAsDTO(cedula);
    }

    @Override
    @Transactional
    public PreguntasTO buscarPreguntasUsuario(String cedula) {
        Usuario usuario = this.buscarPorCedulaConTodosLosDatos(cedula);
        PreguntasTO preguntasTO = new PreguntasTO();
        preguntasTO.setPreguntaUno(usuario.getRespuestasPregunta().get(0).getPreguntaRecuperacion().getPregunta());
        preguntasTO.setPreguntaDos(usuario.getRespuestasPregunta().get(1).getPreguntaRecuperacion().getPregunta());
        preguntasTO.setPreguntaTres(usuario.getRespuestasPregunta().get(2).getPreguntaRecuperacion().getPregunta());
        return preguntasTO;
    }

    @Override
    @Transactional
    public Usuario actualizarPreguntasUsuario(String cedula, PreguntasTO preguntasTO) {
        Usuario usuario = this.buscarPorCedulaConTodosLosDatos(cedula);

        if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVDAD_2.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_3.getEstado());
        } else if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVIDAD_1.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_2.getEstado());
        }
        usuario.setEstadoUsuario(EstadoUsuario.ACTIVO.getEstado());

        usuario = usuarioRepository.save(usuario);

        PreguntaRecuperacion preguntaUno = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaUno());
        PreguntaRecuperacion preguntaDos = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaDos());
        PreguntaRecuperacion preguntaTres = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaTres());

        // Actualiza, no guardes directamente
        List<RespuestaPregunta> respuestas = usuario.getRespuestasPregunta();
        respuestas.get(0).setRespuesta("vacio 1" + " " + usuario.getUsuario());
        respuestas.get(0).setPreguntaRecuperacion(preguntaUno);
        respuestas.get(1).setRespuesta("vacio 2" + " " + usuario.getUsuario());
        respuestas.get(0).setPreguntaRecuperacion(preguntaDos);
        respuestas.get(2).setRespuesta("vacio 3" + " " + usuario.getUsuario());
        respuestas.get(0).setPreguntaRecuperacion(preguntaTres);
        respuestaPreguntaRepository.saveAll(respuestas);

        usuario.setRespuestasPregunta(respuestas);

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public Usuario actualizarRespuestasUsuario(String cedula, RespuestasTO respuestasTO) {
        Usuario usuario = this.buscarPorCedulaConTodosLosDatos(cedula);
        List<RespuestaPregunta> respuestas = usuario.getRespuestasPregunta();
        respuestas.get(0).setRespuesta(respuestasTO.getRespuestaUno());
        respuestas.get(1).setRespuesta(respuestasTO.getRespuestaDos());
        respuestas.get(2).setRespuesta(respuestasTO.getRespuestaTres());
        respuestaPreguntaRepository.saveAll(respuestas);
        if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVDAD_2.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVIDAD_1.getEstado());
        } else if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVDAD_3.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_2.getEstado());
        } else if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVIDAD_4.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_3.getEstado());
        }
        usuario.setEstadoUsuario(EstadoUsuario.ACTIVO.getEstado());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario guardarUsuarioNuevo(UsuarioTO usuarioTO, PreguntasTO preguntasTO) {
        Usuario nuevoUsuario = new Usuario();
        Rol rol = rolService.buscarUnRolPorNombre(usuarioTO.getRol());
        nuevoUsuario.setRol(rol);
        nuevoUsuario.setNombre(usuarioTO.getNombre());
        nuevoUsuario.setApellido(usuarioTO.getApellido());
        nuevoUsuario.setCedula(usuarioTO.getCedula());
        nuevoUsuario.setCorreo(usuarioTO.getCorreo());
        nuevoUsuario.setTelefono(usuarioTO.getTelefono());
        nuevoUsuario.setDireccion(usuarioTO.getDireccion());
        nuevoUsuario.setFechaNacimiento(usuarioTO.getFechaNacimiento());
        nuevoUsuario.setUsuario(usuarioTO.getUsuario());
        nuevoUsuario.setContrasena(encryptSercive.encriptar(usuarioTO.getCedula()));
        nuevoUsuario.setEstadoSeguridad(EstadoSeguridad.ACTIVIDAD_4.getEstado());
        nuevoUsuario.setEstadoUsuario(EstadoUsuario.NUEVO.getEstado());

        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        PreguntaRecuperacion preguntaUno = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaUno());
        PreguntaRecuperacion preguntaDos = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaDos());
        PreguntaRecuperacion preguntaTres = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaTres());

        RespuestaPregunta respuestaUno = new RespuestaPregunta(preguntaUno, "vacio");
        respuestaUno.setUsuario(nuevoUsuario);
        respuestaPreguntaRepository.save(respuestaUno);
        RespuestaPregunta respuestaDos = new RespuestaPregunta(preguntaDos, "vacio");
        respuestaDos.setUsuario(nuevoUsuario);
        respuestaPreguntaRepository.save(respuestaDos);
        RespuestaPregunta respuestaTres = new RespuestaPregunta(preguntaTres, "vacio");
        respuestaTres.setUsuario(nuevoUsuario);
        respuestaPreguntaRepository.save(respuestaTres);
        List<RespuestaPregunta> respuestas = new ArrayList<>();
        respuestas.add(respuestaUno);
        respuestas.add(respuestaDos);
        respuestas.add(respuestaTres);

        nuevoUsuario.setRespuestasPregunta(respuestas);

        return usuarioRepository.save(nuevoUsuario);
    }

    @Override
    public void actualizarContrasenia(String cedula, String nuevaContrasenia) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        usuario.setContrasena(encryptSercive.encriptar(nuevaContrasenia));
        if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVIDAD_4.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_2.getEstado());
        } else if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVDAD_2.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVIDAD_1.getEstado());
        }
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(UsuarioTO usuarioTO) {
        Usuario usuario = usuarioRepository.findByCedula(usuarioTO.getCedula());
        Rol rol = rolService.buscarUnRolPorNombre(usuarioTO.getRol());
        usuario.setRol(rol);
        usuario.setNombre(usuarioTO.getNombre());
        usuario.setApellido(usuarioTO.getApellido());
        usuario.setCorreo(usuarioTO.getCorreo());
        usuario.setTelefono(usuarioTO.getTelefono());
        usuario.setDireccion(usuarioTO.getDireccion());
        usuario.setFechaNacimiento(usuarioTO.getFechaNacimiento());
        usuario.setUsuario(usuarioTO.getUsuario());
        return usuarioRepository.save(usuario);

    }

    @Override
    public boolean compararAntiguaContrasenia(String cedula, String antiguaContrasenia) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        return encryptSercive.comparar(antiguaContrasenia, usuario.getContrasena());

    }

    @Override
    public boolean validarNuevaContrasenia(String antiguaContrasenia, String nuevaContrasenia,
            String repetirContrasenia) {
        return (antiguaContrasenia.equals(nuevaContrasenia) || antiguaContrasenia.equals(repetirContrasenia))
                || nuevaContrasenia.equals(repetirContrasenia);

    }

    @Override
    public void actualizarEstadoUsuario(String cedula, String estado) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        usuario.setEstadoUsuario(estado);

        usuarioRepository.save(usuario);
    }

    @Override
    public void actualizarSeguridadUsuario(String cedula, String estado) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        usuario.setEstadoSeguridad(estado);
        usuarioRepository.save(usuario);
    }

    @Override
    public String nombreUsuario(String cedula) {
        Usuario usuario = usuarioRepository.findByCedula(cedula);
        return usuario.getNombre() + " " + usuario.getApellido();
    }

    @Override
    public Usuario actualizarPreguntaRespuesta(String cedula, PreguntasTO preguntasTO, RespuestasTO respuestasTO) {

        Usuario usuario = buscarPorCedula(cedula);

        usuario = usuarioRepository.save(usuario);

        PreguntaRecuperacion preguntaUno = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaUno());
        PreguntaRecuperacion preguntaDos = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaDos());
        PreguntaRecuperacion preguntaTres = preguntaRecuperacionRepository
                .findByPregunta(preguntasTO.getPreguntaTres());

        RespuestaPregunta respuestaUno = new RespuestaPregunta(preguntaUno, respuestasTO.getRespuestaUno());
        respuestaUno.setUsuario(usuario);
        respuestaPreguntaRepository.save(respuestaUno);
        RespuestaPregunta respuestaDos = new RespuestaPregunta(preguntaDos, respuestasTO.getRespuestaDos());
        respuestaDos.setUsuario(usuario);
        respuestaPreguntaRepository.save(respuestaDos);
        RespuestaPregunta respuestaTres = new RespuestaPregunta(preguntaTres, respuestasTO.getRespuestaTres());
        respuestaTres.setUsuario(usuario);
        respuestaPreguntaRepository.save(respuestaTres);
        List<RespuestaPregunta> respuestas = new ArrayList<>();
        respuestas.add(respuestaUno);
        respuestas.add(respuestaDos);
        respuestas.add(respuestaTres);

        usuario.setRespuestasPregunta(respuestas);

        return usuarioRepository.save(usuario);
    }

    @Override
    public void actualizarRespuestasNuevoUsuario(String cedula, RespuestasTO respuestasTO) {
        Usuario usuario = this.buscarPorCedulaConTodosLosDatos(cedula);
        List<RespuestaPregunta> respuestas = usuario.getRespuestasPregunta();
        respuestas.get(0).setRespuesta(respuestasTO.getRespuestaUno());
        respuestas.get(1).setRespuesta(respuestasTO.getRespuestaDos());
        respuestas.get(2).setRespuesta(respuestasTO.getRespuestaTres());
        respuestaPreguntaRepository.saveAll(respuestas);
        if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVDAD_2.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVIDAD_1.getEstado());
        } else if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVDAD_3.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_2.getEstado());
        } else if (usuario.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVIDAD_4.getEstado())) {
            usuario.setEstadoSeguridad(EstadoSeguridad.ACTIVDAD_3.getEstado());
        }
        usuario.setEstadoUsuario(EstadoUsuario.ACTIVO.getEstado());
        usuarioRepository.save(usuario);
    }

    @Override
    public void actualizarContraseniaUsuario(String cedula, String nuevaContrasenia) {
        Usuario usuario = this.buscarPorCedulaConTodosLosDatos(cedula);
        usuario.setContrasena(encryptSercive.encriptar(nuevaContrasenia));
        usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
     }

    

}
