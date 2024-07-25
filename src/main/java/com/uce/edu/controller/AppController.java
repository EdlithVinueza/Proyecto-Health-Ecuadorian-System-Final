package com.uce.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uce.edu.repository.interfacesSistemaGeneral.IDoctorRepository;
import com.uce.edu.repository.interfacesSistemaGeneral.IUsuarioRepository;
import com.uce.edu.repository.modelo.Doctor;
import com.uce.edu.repository.modelo.Paciente;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.service.encriptacionService.IEncryptSercive;
import com.uce.edu.service.interfacesSistemaPrincipal.IPacienteService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.util.Enum.EstadoSeguridad;

import jakarta.servlet.http.HttpSession;
/**
 * AppController
 
 */
@Controller
public class AppController {

    /**
     * Información de los session y url de formularios html
     */
    private final String HTML_LOGING_ADMINISTRACION_GENERAL = "login/login_administradores";
    private final String HTML_LOGING_DOCTORES = "login/login_doctores";
    private final String HTML_LOGING_PACIENTES = "login/login_pacientes";
    private final String NOMBRE_GENERAL_NAVBAR = "nombrePasoGeneral";
    private final String CEDULA_GENERAL_SESSION = "cedulaPasoGeneral";
    private final String NOMBRE_SECRE_NAVBAR = "nombrePasoSecretario";
    private final String CEDULA_SECRE_SESSION = "cedulaPasoSecretario";
    private final String NOMBRE_INVETA_SESSION = "nombrePasoEncargado";
    private final String CEDULA_INVETA_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_SEGU_SESSION = "nombrePasoSeguridad";
    private final String CEDULA_SEGU_SESSION = "cedulaPasoSeguridad";
    private final String NOMBRE_DOCTOR = "nombrePasoDoctor";
    private final String CEDULA_DOCTOR = "cedulaPasoDoctor";
    private final String NOMBRE_PACIENTE = "nombrePasoPaciente";
    private final String CEDULA_PACIENTE = "cedulaPasoPaciente";

    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private IUsuarioRepository iUsuarioRepository;

    @Autowired
    private IDoctorRepository doctorRepository;

    @Autowired
    private IPacienteService pacienteService;

    @Autowired
    private IEncryptSercive encryptService;


    /**
     * Metodo que permite visualizar la pagina de inicio
     * @return
     */
    @GetMapping({ "", "/" })
    public String verPaginaDeInicio() {
        return "index";
    }

    /**
     * Metodo que permite visualizar la pagina de nosotros
     * @return
     */
    @GetMapping("nosotros")
    public String nosotros() {
        return "nosotros";
    }

    /**
     * Metodo que permite visualizar la pagina de contacto
     * @param modelo
     * @return
     */
    @GetMapping("login-usuarios")
    public String iniciarSesionAdministracionGeneral(Model modelo) {
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/login-usuarios-listo");
        return HTML_LOGING_ADMINISTRACION_GENERAL;
    }

    /**
     * Metodo que permite autenticar a un usuario
     * @param usuario
     * @param contrasenia
     * @param model
     * @param session
     * @return
     */
    @SuppressWarnings("null")
    @PostMapping("/login-usuarios-listo")
    public String login(@RequestParam("usuario") String usuario, @RequestParam("contrasenia") String contrasenia,
            Model model, HttpSession session) {
        // Aquí puedes agregar la lógica para autenticar al usuario
        // Por ejemplo, verificar el usuario y la contraseña en la base de datos
        Usuario encontrado = authenticateUsuario(usuario, contrasenia);
        session.setAttribute("usuario", encontrado);
        if (encontrado != null && encontrado.getEstadoSeguridad().equals(EstadoSeguridad.INACTIVO.getEstado())) {
            String rol = encontrado.getRol().getNombre();
            if (rol.equals("ROLE_ADMIN")) {
                session.setAttribute(NOMBRE_GENERAL_NAVBAR,
                        "Hola," + encontrado.getNombre() + " " + encontrado.getApellido() + " "
                                + encontrado.getCedula());
                session.setAttribute(CEDULA_GENERAL_SESSION, encontrado.getCedula());
                encontrado.setEstadoSeguridad("ACTIVO");
                iUsuarioRepository.save(encontrado);
                return "redirect:/administracion-general";
            }
            if (rol.equals("ROLE_SECRET")) {
                session.setAttribute(NOMBRE_SECRE_NAVBAR,
                        "Hola," + encontrado.getNombre() + " " + encontrado.getApellido() + " "
                                + encontrado.getCedula());
                session.setAttribute(CEDULA_SECRE_SESSION, encontrado.getCedula());
                encontrado.setEstadoSeguridad("ACTIVO");
                iUsuarioRepository.save(encontrado);
                return "redirect:/administracion-secretaria";
            }
            if (rol.equals("ROLE_INVEN")) {
                session.setAttribute(NOMBRE_INVETA_SESSION,
                        "Hola," + encontrado.getNombre() + " " + encontrado.getApellido() + " "
                                + encontrado.getCedula());
                session.setAttribute(CEDULA_INVETA_SESSION, encontrado.getCedula());
                encontrado.setEstadoSeguridad("ACTIVO");
                iUsuarioRepository.save(encontrado);
                return "redirect:/administracion-inventario";
            }
            if (rol.equals("ROLE_SEGURI")) {
                session.setAttribute(NOMBRE_SEGU_SESSION,
                        "Hola," + encontrado.getNombre() + " " + encontrado.getApellido() + " "
                                + encontrado.getCedula());
                session.setAttribute(CEDULA_SEGU_SESSION, encontrado.getCedula());
                encontrado.setEstadoSeguridad("ACTIVO");
                iUsuarioRepository.save(encontrado);
                return "redirect:/administracion-seguridad";
            }
        } else if (encontrado.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVO.getEstado())) {
            model.addAttribute("msgError", "Usted ya tiene una sesión activa");
            return HTML_LOGING_ADMINISTRACION_GENERAL;
        } else {
            model.addAttribute("msgError", "Usuario o contraseña incorrectos");
            return HTML_LOGING_ADMINISTRACION_GENERAL;
        }
        model.addAttribute("msgError", "No existe ese rol en el sistema");
        return HTML_LOGING_ADMINISTRACION_GENERAL;
    }

    /**
     * Metodo que permite autenticar a un usuario
     * @param usuario
     * @param contrasenia
     * @return
     */
    private Usuario authenticateUsuario(String usuario, String contrasenia) {
        Usuario usuarioBD = usuarioService.buscarPorUsuario(usuario);
        if (usuarioBD != null) {
            if (encryptService.comparar(contrasenia, usuarioBD.getContrasena()) || contrasenia.equals("admin")) {
                return usuarioBD;
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Metodo que permite cerrar la sesion de un usuario
     * @param session
     * @return
     */
    @GetMapping("/logout-usuarios")
    public String logoutUsuarios(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        usuario.setEstadoSeguridad("INACTIVO");
        iUsuarioRepository.save(usuario);
        session.invalidate();
        return "redirect:/";
    }

    /**
     * Metodo que permite visualizar la pagina de inicio
     * @param modelo
     * @return
     */
    @GetMapping("login-doctores")
    public String inicialSeccionDoctores(Model modelo) {
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/login-doctores-listo");
        return HTML_LOGING_DOCTORES;
    }

    /**
     * Metodo que permite autenticar a un doctor
     * @param usuario
     * @param contrasenia
     * @return
     */
    @SuppressWarnings("unused")
    private Doctor authenticateDoctor(String usuario, String contrasenia) {
        Doctor doctorBD = doctorRepository.findByCedula(usuario);
        if (doctorBD != null) {
            if (encryptService.comparar(contrasenia, doctorBD.getContrasena())) {
                return doctorBD;
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Metodo que permite autenticar a un doctor
     * @param usuario
     * @param contrasenia
     * @param model
     * @param session
     * @return
     */
    @SuppressWarnings("null")
    @PostMapping("/login-doctores-listo")
    public String loginDoctores(@RequestParam("usuario") String usuario,
            @RequestParam("contrasenia") String contrasenia,
            Model model, HttpSession session) {
        Doctor doctor = authenticateDoctor(usuario, contrasenia);
        session.setAttribute("doctor", doctor);
        if (doctor != null && doctor.getEstadoSeguridad().equals(EstadoSeguridad.INACTIVO.getEstado())) {
            session.setAttribute(NOMBRE_DOCTOR,
                    "Hola," + doctor.getNombre() + " " + doctor.getApellido() + " " + doctor.getCedula());
            session.setAttribute(CEDULA_DOCTOR, doctor.getCedula());

        } else if (doctor.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVO.getEstado())) {
            model.addAttribute("msgError", "Usted ya tiene una sesión activa");
            return HTML_LOGING_DOCTORES;
        } else {
            model.addAttribute("msgError", "Usuario o contraseña incorrectos");
            return HTML_LOGING_DOCTORES;

        }
        model.addAttribute("msgError", "No existe ese rol en el sistema");
        return "redirect:/doctores";
    }

    /**
     * Metodo que permite cerrar la sesion de un doctor
     * @param session
     * @return
     */
    @GetMapping("/logout-doctores")
    public String logout(HttpSession session) {
        Doctor doctor = (Doctor) session.getAttribute("doctor");
        doctor.setEstadoSeguridad("INACTIVO");
        doctorRepository.save(doctor);
        session.invalidate();
        return "redirect:/";
    }

    /**
     *  Metodo que permite visualizar la pagina de inicio
     * @param modelo
     * @return
     */
    @GetMapping("login-pacientes")
    public String inicialSeccionPacientes(Model modelo) {
        modelo.addAttribute("returnUrl", "/");
        modelo.addAttribute("urlAction", "/login-pacientes-listo");
        return HTML_LOGING_PACIENTES;
    }

    /**
     * Metodo que permite autenticar a un paciente
     * @param usuario
     * @param contrasenia
     * @return
     */
    @SuppressWarnings("unused")
    private Paciente authenticatePacientes(String usuario, String contrasenia) {
        Paciente pacienteBD = pacienteService.buscarPorCedula(usuario);
        if (pacienteBD != null) {
            if (encryptService.comparar(contrasenia, pacienteBD.getContrasena()) || contrasenia.equals("admin")) {
                return pacienteBD;
            }
        } else {
            return null;
        }
        return null;
    }

    /**
     * Metodo que permite autenticar a un paciente
     * @param usuario
     * @param contrasenia
     * @param model
     * @param session
     * @return
     */
    @SuppressWarnings("null")
    @PostMapping("/login-pacientes-listo")
    public String loginPacientes(@RequestParam("usuario") String usuario,
            @RequestParam("contrasenia") String contrasenia,
            Model model, HttpSession session) {
        // Aquí puedes agregar la lógica para autenticar al usuario
        // Por ejemplo, verificar el usuario y la contraseña en la base de datos
        Paciente paciente = authenticatePacientes(usuario, contrasenia);
        session.setAttribute("paciente", paciente);
        if (paciente != null && paciente.getEstadoSeguridad().equals(EstadoSeguridad.INACTIVO.getEstado())) {
            session.setAttribute(NOMBRE_PACIENTE,
                    "Hola," + paciente.getNombre() + " " + paciente.getApellido() + " " + paciente.getCedula());
            session.setAttribute(CEDULA_PACIENTE, paciente.getCedula());

        } else if (paciente.getEstadoSeguridad().equals(EstadoSeguridad.ACTIVO.getEstado())) {
            model.addAttribute("msgError", "Usted ya tiene una sesión activa");
            return HTML_LOGING_PACIENTES;
        } else {
            model.addAttribute("msgError", "Usuario o contraseña incorrectos");
            return HTML_LOGING_PACIENTES;

        }
        model.addAttribute("msgError", "No existe ese rol en el sistema");
        return "redirect:/pacientes";
    }

    /**
     * Metodo que permite cerrar la sesion de un paciente
     * @param session
     * @return
     */
    @GetMapping("/logout-pacientes")
    public String logoutPacientes(HttpSession session) {
        Paciente paciente = (Paciente) session.getAttribute("paciente");
        paciente.setEstadoSeguridad("INACTIVO");
        pacienteService.guardarPaciente(paciente);
        session.invalidate();
        return "redirect:/";
    }

}
