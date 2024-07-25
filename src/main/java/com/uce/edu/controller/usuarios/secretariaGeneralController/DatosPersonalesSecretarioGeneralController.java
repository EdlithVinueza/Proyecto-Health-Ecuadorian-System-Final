package com.uce.edu.controller.usuarios.secretariaGeneralController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.interfacesSistemaGeneral.IRolRepository;
import com.uce.edu.repository.modelo.Rol;
import com.uce.edu.repository.modelo.dto.UsuarioDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.to.UsuarioTO;
import jakarta.servlet.http.HttpSession;
/**
 * Datos Personales Secretario GeneralC ontroller
 
 */
@Controller
@RequestMapping("administracion-secretaria")
public class DatosPersonalesSecretarioGeneralController {
/**
 * Información de los session y url de formularios html
 */
    private final String TITULO_NAVBAR = "Secretaria";
    private final String NOMBRE_NAVBAR = "nombrePasoSecretario";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoSecretario";
    private final String URL_MENU_PRINCIPAL = "/administracion-secretaria";
    private final String HTML_URL_FORMULARIO_USUARIOS = "secretariaGeneral/formularios/usuario_informacion";
    private final String HTML_URL_FORMULARIO_USUARIOS_INFORMACION = "secretariaGeneral/informacion/usuario_informacion";

    /**
     * Dependencias
     */
    @Autowired
    private IUsuarioService usuarioService;
    
    /**
     * Lista de roles
     */
    @SuppressWarnings("unused")
    @Autowired
    private IRolRepository rolRepository;

    private List<Rol> roles;
   
    public DatosPersonalesSecretarioGeneralController(IRolRepository rolRepository) {
        this.rolRepository=rolRepository;
        this.roles = rolRepository.findAll();
    }

    /**
     * Metodo que permite al usuario que este usando la secion ver su informacion
     * personal
     * 
     * @param cedula
     * @param redirectAttributes
     * @param modelo
     * @return
     */
    @GetMapping("/usuario-informacion-personal")
    public String verInformacionPersonalUsuario(Model modelo, HttpSession session) {
        String tmpCedula = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        UsuarioDTO usuarioDTO = usuarioService.buscarUsuarioDTOCedula(tmpCedula);
        modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
        modelo.addAttribute("usuarioDTO", usuarioDTO);
        return HTML_URL_FORMULARIO_USUARIOS_INFORMACION;
    }

    /**
     * Metodo que permite al usuario que este usando la secion ver formulario para
     * actualizar su informacion personal
     * 
     * @param cedula
     * @param modelo
     * @param session
     * @return
     */

    @GetMapping("/usuario-actualizar-tu-usuario/{cedula}")
    public String montarFormularioParaActualizarTuUsuario(@PathVariable String cedula, Model modelo,
            HttpSession session) {
        {
            modelo.addAttribute("listaRoles", roles);
            modelo.addAttribute("tituloPestaña", "Actualizar Usuario");
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Usuario");
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("tituloFormulario", "Actualizar Usuario");
            modelo.addAttribute("botonSubmit", "Actualizar");
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "usuario-guardar-tu-usuario/" + cedula);
        }
        modelo.addAttribute("usuarioTO", usuarioService.crearUsuarioTOPorCedula(cedula));
        return HTML_URL_FORMULARIO_USUARIOS;
    }

    /**
     * Metodo que permite actualizar la informacion personal del usuario que esta
     * usando la sesion
     * 
     * @param cedula
     * @param usuarioTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/usuario-guardar-tu-usuario/{cedula}")
    public String actualizarTuUsuario(@PathVariable String cedula, @Validated UsuarioTO usuarioTO, BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        
            modelo.addAttribute("listaRoles", roles);
            modelo.addAttribute("tituloPestaña", "Actualizar Usuario");
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Usuario");
            String nombreUsuario = (String) session.getAttribute(NOMBRE_NAVBAR);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            modelo.addAttribute("tituloFormulario", "Actualizar Usuario");
            modelo.addAttribute("botonSubmit", "Actualizar");
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL);
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "usuario-guardar-tu-usuario/" + cedula);
        
        if (result.hasErrors()) {
            return HTML_URL_FORMULARIO_USUARIOS;
        }
        
        usuarioService.actualizarUsuario(usuarioTO);
        redirectAttributes.addFlashAttribute("msgExito", "El Usuario ha sido actualizado correctamente");
        return "redirect:" + URL_MENU_PRINCIPAL;
    }

}
