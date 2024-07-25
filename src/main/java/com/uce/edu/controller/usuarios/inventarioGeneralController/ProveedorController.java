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
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.inventarioService.interfaceService.IProveedorService;
import com.uce.edu.service.to.inventarioTO.ProveedorTO;
import com.uce.edu.util.reportes.inventario.ListaProveedoresPDF;

import jakarta.servlet.http.HttpSession;

/**
 * Proveedor Controller
 * 
 */
@Controller
@RequestMapping("administracion-inventario")
public class ProveedorController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Inventario";
    private final String NOMBRE_USUARIO_SESSION = "nombrePasoEncargado";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_NAVBAR = "nombreEncargado";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";
    private final String URL_LISTA = "/proveedor-ver-todo";
    private final String URL_MENU_PROVEEDOR = "/menu-proveedores";
    private final String URL_HTML_FORMULARIO_PROVEEDOR = "inventarioGeneral/formularios/proveedor_formulario";
    private final String URL_HTML_LISTA_PROVEEDOR = "inventarioGeneral/listas/proveedor_lista";
    private final String URL_HTML_INFORMACION_PROVEEDOR = "inventarioGeneral/informacion/proveedor_informacion";

    /**
     * Dependencias
     */
    @Autowired
    private IProveedorService proveedorService;
    @Autowired
    private IUsuarioService UsuarioService;

    /**
     * Metodo que permite visualizar el menu de administracion general
     * 
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/proveedor-nuevo")
    public String montarFormularioParaNuevoProveedor(Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo proveedor
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Proveedor");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Proveedor");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Proveedor");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/proveedor-guardar");
        }
        modelo.addAttribute("proveedorTO", new ProveedorTO());
        return URL_HTML_FORMULARIO_PROVEEDOR;
    }

    /**
     * Metodo que permite guardar un nuevo proveedor
     * 
     * @param proveedorTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/proveedor-guardar")
    public String guardarProveedor(@Validated ProveedorTO proveedorTO, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo proveedor (Si es que se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Proveedor");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Proveedor");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Proveedor");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/proveedor-guardar");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("proveedorTO", proveedorTO);
            return URL_HTML_FORMULARIO_PROVEEDOR;
        }

        Proveedor proveedorExistentePorRuc = proveedorService.buscarProveedorPorRuc(proveedorTO.getRuc());
        if (proveedorExistentePorRuc != null) {
            modelo.addAttribute("msgError", "Ya existe ese Ruc en el sistema");
            return URL_HTML_FORMULARIO_PROVEEDOR;
        }

        Proveedor proveedorExistentePorNombreEmpresa = proveedorService.buscarPorNombreEmpresa(proveedorTO.getNombre());
        if (proveedorExistentePorNombreEmpresa != null) {
            modelo.addAttribute("msgError", "Ya existe ese nombre de empresa en el sistema");
            return URL_HTML_FORMULARIO_PROVEEDOR;

        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {
            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR;
        }

        Proveedor proveedorGuardado = proveedorService.guardaNuevoProveedor(proveedorTO, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (proveedorGuardado != null) {
            redirectAttributes.addFlashAttribute("msgExito",
                    "Proveedor con Ruc " + proveedorTO.getRuc() + " guardado exitosamente");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR;
        } else {
            modelo.addAttribute("proveedorTO", proveedorTO);
            modelo.addAttribute("msgError", "Error al guardar el proveedor");
            return URL_HTML_FORMULARIO_PROVEEDOR;
        }
    }

    /**
     * Metodo que permite visualizar la lista de proveedores
     * 
     * @param modelo
     * @param existeBusquedaPevia
     * @param session
     * @return
     */
    @GetMapping("/proveedor-ver-todo")
    public String listarProveedores(Model modelo,
            @RequestParam(name = "existeBusquedaPevia", required = false) Boolean existeBusquedaPevia,
            HttpSession session) {
        // Atributos para la lista de proveedores
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Proveedores");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Usuarios");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Proveedores");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR);
        }
        Boolean pasoAnterior = (Boolean) session.getAttribute("pasoAnterior");

        // Obtener la lista de proveedores
        List<Proveedor> proveedores = new ArrayList<>();
        String ruc = (String) session.getAttribute("proveedoresPorRuc");
        String nombreEmpresa = (String) session.getAttribute("proveedoresPorEmpresa");

        if ((existeBusquedaPevia != null && existeBusquedaPevia)) {
            proveedores = busquedasPevias(ruc, nombreEmpresa);
        } else if (pasoAnterior != null && pasoAnterior) {
            proveedores = busquedasPevias(ruc, nombreEmpresa);
        } else {
            proveedores = proveedorService.listarProveedores();
            session.setAttribute("proveedoresPorRuc", null);
            session.setAttribute("proveedoresPorEmpresa", null);
        }

        if (proveedores == null || proveedores.isEmpty()) {
            modelo.addAttribute("msgError", "No hay proveedores registrados");
            return URL_HTML_LISTA_PROVEEDOR;
        }

        modelo.addAttribute("listaProveedores",
                proveedores);

        return URL_HTML_LISTA_PROVEEDOR;
    }

    /**
     * Metodo que permite buscar un proveedor por Ruc
     * 
     * @param ruc
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/proveedor-buscar-por-ruc")
    public String buscarPorRuc(@RequestParam("ruc") String ruc, Model modelo, HttpSession session) {
        // Atributos para la lista de proveedores
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Proveedores");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Usuarios");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Proveedores");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // proveedores
            session.setAttribute("proveedoresPorRuc", ruc);
            session.setAttribute("proveedoresPorEmpresa", null);
        }

        Proveedor proveedor = proveedorService.buscarProveedorPorRuc(ruc);
        if (proveedor == null) {
            modelo.addAttribute("msgError", "No se encontró un proveedor con ese Ruc");
            return URL_HTML_LISTA_PROVEEDOR;
        }
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(proveedor);
        modelo.addAttribute("listaProveedores", proveedores);
        // Actualizamos la lista de proveedores en la sesion
        session.setAttribute("proveedores", proveedores);
        modelo.addAttribute("msgExito", "Proveedor con RUC: " + ruc + " encontrado");
        return URL_HTML_LISTA_PROVEEDOR;
    }

    /**
     * Metodo que permite buscar un proveedor por Nombre de Empresa
     * 
     * @param nombreEmpresa
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/proveedor-buscar-por-empresa")
    public String buscarPorEmpresa(@RequestParam("nombreEmpresa") String nombreEmpresa, Model modelo,
            HttpSession session) {
        // Atributos para la lista de proveedores
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Proveedores");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Usuarios");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Proveedores");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // proveedores
            session.setAttribute("proveedoresPorRuc", null);
            session.setAttribute("proveedoresPorEmpresa", nombreEmpresa);
        }

        Proveedor proveedor = proveedorService.buscarPorNombreEmpresa(nombreEmpresa);
        if (proveedor == null) {
            modelo.addAttribute("msgError", "No se encontró un proveedor con ese Nombre de Empresa");
            return URL_HTML_LISTA_PROVEEDOR;
        }
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(proveedor);
        modelo.addAttribute("listaProveedores", proveedores);
        // Actualizamos la lista de proveedores en la sesion
        session.setAttribute("proveedores", proveedores);
        modelo.addAttribute("msgExito", "Empresa: " + nombreEmpresa + " encontrada");
        return URL_HTML_LISTA_PROVEEDOR;
    }

    /**
     * Metodo que permite generar un pdf con la lista de proveedores
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/proveedor-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdf(HttpSession session) throws IOException {
        String ruc = (String) session.getAttribute("proveedoresPorRuc");
        String nombreEmpresa = (String) session.getAttribute("proveedoresPorEmpresa");
        List<Proveedor> proveedores = new ArrayList<>();
        if (ruc != null) {
            Proveedor proveedor = proveedorService.buscarProveedorPorRuc(ruc);
            if (proveedor == null) {
                return ResponseEntity.badRequest().body(null);
            }
            proveedores.add(proveedor);
        }

        if (nombreEmpresa != null) {
            Proveedor proveedor = proveedorService.buscarPorNombreEmpresa(nombreEmpresa);
            if (proveedor == null) {
                return ResponseEntity.badRequest().body(null);
            }
            proveedores.add(proveedor);
        }

        if (nombreEmpresa == null && ruc == null) {
            proveedores = proveedorService.listarProveedores();
            if (proveedores == null || proveedores.isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }

        }
        ListaProveedoresPDF listaProveedoresPDF = new ListaProveedoresPDF();
        ByteArrayOutputStream byteArrayOutputStream = listaProveedoresPDF.generarPdfProveedores(proveedores);

        String filename = "filename=" + "provedores" + LocalDateTime.now() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

    /**
     * Metodo que permite montar el formulario para actualizar un proveedor
     * 
     * @param ruc
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/proveedor-actualizar/{ruc}")
    public String montarFormularioParaActualizarProveedor(@PathVariable String ruc, Model modelo,
            HttpSession session) {
        // Atributos para el formulario de actualización de un proveedor
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Actualizar Proveedor");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Proveedor");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Actualizar Proveedor: " + ruc);
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Actualizar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/proveedor-ver-todo/" + ruc);
        }
        // Guardar en la session que existe una busqueda previa
        session.setAttribute("existeBusquedaPevia", true);
        // Convertir el proveedor a un ProveedorTO
        ProveedorTO proveedorTO = proveedorService.convertirProveedorTO(ruc);
        // Agregar el proveedorTO al modelo
        modelo.addAttribute("proveedorTO", proveedorTO);
        return URL_HTML_FORMULARIO_PROVEEDOR;
    }

    /**
     * Metodo que permite actualizar un proveedor
     * 
     * @param ruc
     * @param proveedorTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/proveedor-ver-todo/{ruc}")
    public String actualizarProveedor(@PathVariable String ruc, @Validated ProveedorTO proveedorTO,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo proveedor (Si es que se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Proveedor");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Proveedor");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Proveedor");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PROVEEDOR);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/proveedor-guardar");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("proveedorTO", proveedorTO);
            return URL_HTML_FORMULARIO_PROVEEDOR;
        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {
            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
        }

        Proveedor proveedorGuardado = proveedorService.actualizaProveedor(proveedorTO, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (proveedorGuardado != null) {
            String rucPaso = (String) session.getAttribute("proveedoresPorRuc");
            String nombreEmpresaPaso = (String) session.getAttribute("proveedoresPorEmpresa");
            if (rucPaso != null || nombreEmpresaPaso != null) {
                redirectAttributes.addAttribute("existeBusquedaPevia", true);
            }
            redirectAttributes.addFlashAttribute("msgExito",
                    "Proveedor con Ruc: " + proveedorTO.getRuc() + " actualizado exitosamente");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
        } else {
            modelo.addAttribute("proveedorTO", proveedorTO);
            modelo.addAttribute("msgError", "Error al guardar el proveedor");
            return URL_HTML_FORMULARIO_PROVEEDOR;
        }
    }

    /**
     * Metodo que permite ver mas detalles de un proveedor
     * 
     * @param ruc
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/proveedor-ver-mas-detalles/{ruc}")
    public String verInformacionPersonal(@PathVariable String ruc, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualización de un proveedor
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Ver Detalles Proveedor");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Detalles Proveedor");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloListaInformacion", "Detalles del Proveedor: " + ruc);
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/proveedor-ver-todo/" + ruc);
        }

        String rucPaso = (String) session.getAttribute("proveedoresPorRuc");
        String nombreEmpresaPaso = (String) session.getAttribute("proveedoresPorEmpresa");

        boolean pasoAnterior = rucPaso != null || nombreEmpresaPaso != null;
        session.setAttribute("pasoAnterior", pasoAnterior);

        // Buscar el proveedor por Ruc
        Proveedor proveedor = proveedorService.buscarProveedorPorRuc(ruc);
        // Convertir el proveedor a un ProveedorTO
        modelo.addAttribute("proveedor", proveedor);
        return URL_HTML_INFORMACION_PROVEEDOR;
    }

    /**
     * Metodo que permite buscar proveedores previamente
     * 
     * @param ruc
     * @param nombreEmpresa
     * @return
     */
    public List<Proveedor> busquedasPevias(String ruc, String nombreEmpresa) {
        List<Proveedor> proveedores = new ArrayList<>();
        if (ruc != null) {
            Proveedor proveedor = proveedorService.buscarProveedorPorRuc(ruc);
            if (proveedor != null) {
                proveedores.add(proveedor);
            }
        }
        if (nombreEmpresa != null) {
            Proveedor proveedor = proveedorService.buscarPorNombreEmpresa(nombreEmpresa);
            if (proveedor != null) {
                proveedores.add(proveedor);
            }
        }
        return proveedores;
    }

}
