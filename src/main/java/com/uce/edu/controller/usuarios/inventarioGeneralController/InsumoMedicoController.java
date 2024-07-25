package com.uce.edu.controller.usuarios.inventarioGeneralController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.inventarioModelo.InsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.inventarioService.interfaceService.IProveedorService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.inventarioService.interfaceService.IInventarioService;
import com.uce.edu.service.to.inventarioTO.InsumoMedicoTO;
import com.uce.edu.util.reportes.inventario.ListarInsumosMedicosPDF;

import jakarta.servlet.http.HttpSession;

/**
 * InsumoMedico Controller
 * 
 */
@Controller
@RequestMapping("administracion-inventario")
public class InsumoMedicoController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Inventario";
    private final String NOMBRE_USUARIO_SESSION = "nombrePasoEncargado";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_NAVBAR = "nombreEncargado";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";
    private final String URL_LISTA = "/insumo-ver-todo";
    private final String URL_MENU_INSUMO = "/menu-insumo-medico";
    private final String URL_HTML_FORMULARIO_INSUMO = "inventarioGeneral/formularios/insumo_medico_formulario";
    private final String URL_HTML_LISTA_INSUMO = "inventarioGeneral/listas/insumo_medico_lista";
    private final String URL_HTML_INFORMACION_INSUMO = "inventarioGeneral/informacion/insumo_medico_informacion";

    /**
     * Dependencias
     */

    @Autowired
    private IInventarioService inventarioService;
    @Autowired
    private IUsuarioService UsuarioService;
    @Autowired
    private IProveedorService proveedorService;

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/insumo-nuevo")
    public String montarFormularioParaNuevoInsumoMedico(Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo insumo
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Insumo Médico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Insumo Médico");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/insumo-guardar");
        }
        modelo.addAttribute("insumoMedicoTO", new InsumoMedicoTO());
        return URL_HTML_FORMULARIO_INSUMO;
    }

    /**
     * Guardar un nuevo insumo
     * 
     * @param insumoMedicoTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */

    @PostMapping("/insumo-guardar")
    public String guardarInsumoMedico(@Validated InsumoMedicoTO insumoMedicoTO, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo insumo (Si es que se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Insumo Médico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Insumo Médico");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/insumo-guardar");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("insumoMedicoTO", insumoMedicoTO);
            return URL_HTML_FORMULARIO_INSUMO;
        }

        Proveedor proveedor = proveedorService.buscarPorRucONombreEmpresa(insumoMedicoTO.getParametro(),
                insumoMedicoTO.getParametro());

        if (proveedor == null) {
            modelo.addAttribute("msgError", "No existe ese proveedor en la base de datos");
            return URL_HTML_FORMULARIO_INSUMO;
        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {
            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_INSUMO;
        }

        InsumoMedico insumoGuardado = inventarioService.guardarNuevoInsumoMedico(insumoMedicoTO, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (insumoGuardado != null) {
            redirectAttributes.addFlashAttribute("msgExito",
                    "InsumoMedico con código " + insumoGuardado.getCodigo() + " se guardado exitosamente");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_INSUMO;
        } else {
            modelo.addAttribute("insumoMedicoTO", insumoMedicoTO);
            modelo.addAttribute("msgError", "Error al guardar el insumo");
            return URL_HTML_FORMULARIO_INSUMO;
        }
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param existeBusquedaPevia
     * @param session
     * @return
     */

    @GetMapping("/insumo-ver-todo")
    public String listarInsumoMedicoes(Model modelo,
            @RequestParam(name = "existeBusquedaPevia", required = false) Boolean existeBusquedaPevia,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Insumos Médicos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
        }

        Boolean pasoAnterior = (Boolean) session.getAttribute("pasoAnterior");

        List<InsumoMedico> insumosMedicos = new ArrayList<>();
        String codigo = (String) session.getAttribute("insumosMedicosPorCodigo");
        String proveedor = (String) session.getAttribute("insumosMedicosPorProveedor");
        String nombreComun = (String) session.getAttribute("insumosMedicosPorNombreComun");

        if (existeBusquedaPevia != null && existeBusquedaPevia) {
            insumosMedicos = busquedasPevias(codigo, proveedor, nombreComun);
        } else if (pasoAnterior != null && pasoAnterior) {
            insumosMedicos = busquedasPevias(codigo, proveedor, nombreComun);
        } else {
            insumosMedicos = inventarioService.todosLosInsumosMedicos();
            session.setAttribute("insumosMedicosPorCodigo", null);
            session.setAttribute("insumosMedicosPorProveedor", null);
            session.setAttribute("insumosMedicosPorNombreComun", null);
        }

        if (insumosMedicos == null || insumosMedicos.isEmpty()) {
            modelo.addAttribute("msgError", "No hay insumosMedicos registrados");
            return URL_HTML_LISTA_INSUMO;
        }
        // Agregar la lista de insumosMedicos al modelo
        modelo.addAttribute("listaInsumoMedico", insumosMedicos);

        return URL_HTML_LISTA_INSUMO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/insumo-buscar-por-codigo")
    public String buscarPorRuc(@RequestParam("codigo") String codigo, Model modelo, HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Insumos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("insumosMedicosPorCodigo", codigo);
            session.setAttribute("insumosMedicosPorProveedor", null);
            session.setAttribute("insumosMedicosPorNombreComun", null);
        }

        InsumoMedico insumo = inventarioService.buscarInsumoMedicoPorCodigo(codigo);
        if (insumo == null) {
            modelo.addAttribute("msgError", "No se encontró un insumo con ese código");
            return URL_HTML_LISTA_INSUMO;
        }
        List<InsumoMedico> insumosMedicos = new ArrayList<>();
        insumosMedicos.add(insumo);
        modelo.addAttribute("listaInsumoMedico", insumosMedicos);
        modelo.addAttribute("msgExito", "InsumoMedico con código: " + codigo + " encontrado");
        return URL_HTML_LISTA_INSUMO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param proveedor
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/insumo-buscar-por-proveedor")
    public String buscarPorEmpresa(@RequestParam("proveedor") String proveedor, Model modelo,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Insumos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("insumosMedicosPorCodigo", null);
            session.setAttribute("insumosMedicosPorProveedor", proveedor);
            session.setAttribute("insumosMedicosPorNombreComun", null);
        }

        List<InsumoMedico> insumos = inventarioService.buscarInsumoMedicoPorProveedor(proveedor, proveedor);
        if (insumos == null || insumos.isEmpty()) {
            modelo.addAttribute("msgError", "El proveedor: " + proveedor + " no tiene insumos registrados");
            return URL_HTML_LISTA_INSUMO;
        }

        modelo.addAttribute("listaInsumoMedico", insumos);
        modelo.addAttribute("msgExito", "InsumoS Médicos con proveedor: " + proveedor + " encontrado");
        return URL_HTML_LISTA_INSUMO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param nombreComun
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/insumo-buscar-por-nombre-comun")
    public String buscarPorNombreComun(@RequestParam("nombreComun") String nombreComun, Model modelo,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Insumos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("insumosMedicosPorCodigo", null);
            session.setAttribute("insumosMedicosPorProveedor", null);
            session.setAttribute("insumosMedicosPorNombreComun", nombreComun);
        }

        List<InsumoMedico> insumos = inventarioService.buscarInsumoMedicoPorNombre(nombreComun);
        if (insumos == null || insumos.isEmpty()) {
            modelo.addAttribute("msgError", "No existen insumos con el nombre común: " + nombreComun);
            return URL_HTML_LISTA_INSUMO;
        }

        modelo.addAttribute("listaInsumoMedico", insumos);
        modelo.addAttribute("msgExito", "Existen insumos con el nombre común: " + nombreComun);
        return URL_HTML_LISTA_INSUMO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/insumo-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfInsumo(HttpSession session) throws IOException {
        String codigo = (String) session.getAttribute("insumosMedicosPorCodigo");
        String proveedor = (String) session.getAttribute("insumosMedicosPorProveedor");
        String nombreComun = (String) session.getAttribute("insumosMedicosPorNombreComun");
        List<InsumoMedico> insumosMedicos = busquedasPevias(codigo, proveedor, nombreComun);
        ListarInsumosMedicosPDF listaInsumoMedicoPDF = new ListarInsumosMedicosPDF();
        ByteArrayOutputStream byteArrayOutputStream = listaInsumoMedicoPDF.generatePdfInsumosMedicos(insumosMedicos);

        String filename = "filename=" + "insumos" + LocalDateTime.now() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/insumo-actualizar/{codigo}")
    public String montarFormularioParaActualizarInsumoMedico(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        // Atributos para el formulario de actualización de un insumo
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Actualizar Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Insumo Meéico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Actualizar InsumoMedico: " + codigo);
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Actualizar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/insumo-ver-todo/" + codigo);
        }
        session.setAttribute("existeBusquedaPevia", true);
        // Buscar el insumo por codigo
        InsumoMedicoTO insumoMedicoTO = inventarioService.contruirInsumoMedicoTO(codigo);
        // Agregar el insumoMedicoTO al modelo
        modelo.addAttribute("insumoMedicoTO", insumoMedicoTO);
        return URL_HTML_FORMULARIO_INSUMO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param codigo
     * @param insumoMedicoTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/insumo-ver-todo/{codigo}")
    public String actualizarInsumoMedico(@PathVariable String codigo, @Validated InsumoMedicoTO insumoMedicoTO,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualizacion de un insumo (Si es que se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Actualizar Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Insumo Médico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Actualizar Insumo Médico: " + codigo);
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Actualizar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_INSUMO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/insumo-guardar");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("insumoMedicoTO", insumoMedicoTO);
            return URL_HTML_FORMULARIO_INSUMO;
        }

        Proveedor proveedorBase = proveedorService.buscarPorRucONombreEmpresa(insumoMedicoTO.getParametro(),
                insumoMedicoTO.getParametro());

        if (proveedorBase == null) {
            modelo.addAttribute("msgError", "No existe ese proveedor en la base de datos");
            return URL_HTML_FORMULARIO_INSUMO;
        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {

            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
        }

        InsumoMedico insumoGuardado = inventarioService.actualizarInsumoMedico(insumoMedicoTO, codigo,
                nombreUsuarioActual, cedulaUsuarioActual);
        if (insumoGuardado != null) {
            // Valores guardados
            String codigoPaso = (String) session.getAttribute("insumosMedicosPorCodigo");
            String proveedor = (String) session.getAttribute("insumosMedicosPorProveedor");
            String nombreComun = (String) session.getAttribute("insumosMedicosPorNombreComun");

            if (codigoPaso != null || proveedor != null || nombreComun != null) {
                redirectAttributes.addAttribute("existeBusquedaPevia", true);
            }
            redirectAttributes.addFlashAttribute("msgExito",
                    "InsumoMedico con código: " + insumoGuardado.getCodigo() + " actualizado exitosamente");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
        } else {
            modelo.addAttribute("insumoMedicoTO", insumoMedicoTO);
            modelo.addAttribute("msgError", "Error al actualizar el insumo");
            return URL_HTML_FORMULARIO_INSUMO;
        }
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/insumo-ver-mas-detalles/{codigo}")
    public String verInformacionPersonal(@PathVariable String codigo, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualización de un insumo
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Ver Detalles Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Detalles Insumo Médico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloListaInformacion", "Detalles del Insumo Médico: " + codigo);
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/insumo-ver-todo/" + codigo);
        }
        String codigoPaso = (String) session.getAttribute("insumosMedicosPorCodigo");
        String proveedor = (String) session.getAttribute("insumosMedicosPorProveedor");
        String nombreComun = (String) session.getAttribute("insumosMedicosPorNombreComun");

        boolean pasoAnterior = codigoPaso != null || proveedor != null || nombreComun != null;
        session.setAttribute("pasoAnterior", pasoAnterior);

        // Buscar el insumo por Ruc
        InsumoMedico insumo = inventarioService.buscarInsumoMedicoPorCodigo(codigo);
        // Convertir el insumo a un InsumoMedicoTO
        modelo.addAttribute("insumo", insumo);
        return URL_HTML_INFORMACION_INSUMO;
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param codigo
     * @param proveedor
     * @param nombreComun
     * @return 
     */
    public List<InsumoMedico> busquedasPevias(String codigo, String proveedor, String nombreComun) {
        List<InsumoMedico> insumosMedicos = new ArrayList<>();
        if (codigo != null) {
            InsumoMedico insumo = inventarioService.buscarInsumoMedicoPorCodigo(codigo);
            if (insumo != null) {
                insumosMedicos.add(insumo);
            }
        }
        if (proveedor != null) {
            insumosMedicos = inventarioService.buscarInsumoMedicoPorProveedor(proveedor, proveedor);
        }
        if (nombreComun != null) {
            insumosMedicos = inventarioService.buscarInsumoMedicoPorNombre(nombreComun);
        }
        if (codigo == null && proveedor == null && nombreComun == null) {
            insumosMedicos = inventarioService.todosLosInsumosMedicos();
        }
        return insumosMedicos;
    }

}
