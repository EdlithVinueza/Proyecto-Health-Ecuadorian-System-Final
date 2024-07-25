package com.uce.edu.service.codigosTemporales;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.CodigoTemporal;
import com.uce.edu.repository.modelo.ICodigoTemporalRepository;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.emailService.EmailService;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.ActualizarContraseniaRespuestasNuevasTO;
import com.uce.edu.service.to.ActualizarContraseniaTO;
import com.uce.edu.service.to.RespuestasTO;
import com.uce.edu.util.Enum.EstadoSeguridad;
import com.uce.edu.util.Enum.EstadoUsuario;

import jakarta.mail.MessagingException;

@Service
public class CodigosTemporalesServiceImpl implements ICodigosTemporalesService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private IEncryptSercive encryptSercive;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private ICodigoTemporalRepository codigoTemporalRepository;


    @Override
    public Boolean enviarCodigoUsuario(String cedulaUsuario, String correo) {
        Usuario usuario = usuarioRepository.findByCedula(cedulaUsuario);
        String codigo = UUID.randomUUID().toString().substring(0, 13);
        LocalDateTime fechaCaducidad = LocalDateTime.now().plusMinutes(10);
        if (usuario != null) {
            try {
                CodigoTemporal codigoTemporal = codigoTemporalRepository.findByUsuario(usuario);
                if (codigoTemporal != null) {
                    codigoTemporal.setEstado("activo");
                    codigoTemporal.setCodigo(encryptSercive.encriptar(codigo));
                    usuario.setCodigoTemporal(codigoTemporal);
                    usuarioRepository.save(usuario);
                    codigoTemporalRepository.save(codigoTemporal);
                } else {
                    codigoTemporal = new CodigoTemporal();
                    codigoTemporal.setCodigo(encryptSercive.encriptar(codigo));
                    codigoTemporal.setFechaCaducidad(fechaCaducidad);
                    codigoTemporal.setEstado("activo");
                    codigoTemporal.setUsuario(usuario);
                    usuario.setCodigoTemporal(codigoTemporal);
                    usuarioRepository.save(usuario);
                    codigoTemporalRepository.save(codigoTemporal);
                }
                String subject = "Hola, somo H.E.S. \nTu código de seguridades: ";
                emailService.sendEmailWithCode(correo, subject, codigo,fechaCaducidad);
                return true;
            
         
            }catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
        }

          
        return false;

    }

    @Override
    public Boolean actualizarNuevoUsuario(String cedulaUsuario, ActualizarContraseniaRespuestasNuevasTO respuestas) {
        Usuario usuario = usuarioRepository.findByCedula(cedulaUsuario);
        if (usuario != null) {
            CodigoTemporal codigoTemporal = codigoTemporalRepository.findByUsuario(usuario);
            if (codigoTemporal != null) {
                if (encryptSercive.comparar(respuestas.getCodigo(), codigoTemporal.getCodigo())) {
                    if (codigoTemporal.getFechaCaducidad().isAfter(LocalDateTime.now())) {
                        RespuestasTO respuestasTO = obtenerRespuestas(respuestas);
                        usuarioService.actualizarRespuestasNuevoUsuario(cedulaUsuario, respuestasTO);
                        usuarioService.actualizarContrasenia(cedulaUsuario, respuestas.getNuevaContrasenia());
                        usuarioService.actualizarEstadoUsuario(cedulaUsuario, EstadoUsuario.ACTIVO.getEstado());
                        usuarioService.actualizarSeguridadUsuario(cedulaUsuario, EstadoSeguridad.ACTIVIDAD_1.getEstado());
                        codigoTemporal.setEstado("confirmado");
                        codigoTemporalRepository.save(codigoTemporal);
                        return true;
                    } else {
                        codigoTemporal.setEstado("inactivo");
                        codigoTemporalRepository.save(codigoTemporal);
                        return false;

                    }
                }
            }
        }
        return false;

    }

    @Override
    public Boolean actualizarContraseniaUsuario(String cedulaUsuario, ActualizarContraseniaTO respuestas) {
        Usuario usuario = usuarioRepository.findByCedula(cedulaUsuario);
        if (usuario != null) {
            CodigoTemporal codigoTemporal = codigoTemporalRepository.findByUsuario(usuario);
            if (codigoTemporal != null) {
                if (encryptSercive.comparar(respuestas.getCodigo(), codigoTemporal.getCodigo())) {
                    if (codigoTemporal.getFechaCaducidad().isAfter(LocalDateTime.now())) {
                        usuarioService.actualizarContrasenia(cedulaUsuario, respuestas.getNuevaContrasenia());
                        codigoTemporal.setEstado("confirmado");
                        codigoTemporalRepository.save(codigoTemporal);
                        return true;
                    } else {
                        codigoTemporal.setEstado("inactivo");
                        codigoTemporalRepository.save(codigoTemporal);
                        return false;

                    }
                }
            }
        }
        return false;
    }

    @Override
    public RespuestasTO obtenerRespuestas(ActualizarContraseniaRespuestasNuevasTO respuestas) {
        RespuestasTO respuestasTO = new RespuestasTO();
        respuestasTO.setRespuestaUno(respuestas.getRespuestaUno());
        respuestasTO.setRespuestaDos(respuestas.getRespuestaDos());
        respuestasTO.setRespuestaTres(respuestas.getRespuestaTres());
        return respuestasTO;
    }

    @Override
    public boolean validarCaducidadCodigoUsuario(Usuario usuario) {
        CodigoTemporal codigoTemporal = codigoTemporalRepository.findByUsuario(usuario);
        if (codigoTemporal != null && codigoTemporal.getEstado().equals("activo")) {
            if (LocalDateTime.now().isAfter(codigoTemporal.getFechaCaducidad())) {
                return true;
            } else {
                codigoTemporal.setEstado("inactivo");
                codigoTemporalRepository.save(codigoTemporal);
                return false;
            }

        } else {
            return false;
        }
    }

    @Override
    public boolean valdiarSiElCodigoLePerteneceAlUsuario(Usuario usuario, String codigo) {
        CodigoTemporal codigoTemporal = codigoTemporalRepository.findByUsuario(usuario);
        if (codigoTemporal != null) {
            if (encryptSercive.comparar(codigo, codigoTemporal.getCodigo())) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
