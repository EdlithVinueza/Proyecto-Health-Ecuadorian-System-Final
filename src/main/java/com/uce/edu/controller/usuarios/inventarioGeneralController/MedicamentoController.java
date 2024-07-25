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
import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.inventarioService.interfaceService.IProveedorService;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.inventarioService.interfaceService.IInventarioService;
import com.uce.edu.service.to.inventarioTO.MedicamentoTO;
import com.uce.edu.util.reportes.inventario.ListarMedicamentoPDF;

import jakarta.servlet.http.HttpSession;
/**
 * Medicamento Controller
 
 */
@Controller
@RequestMapping("administracion-inventario")
public class MedicamentoController {

    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Inventario";
    private final String NOMBRE_USUARIO_SESSION = "nombrePasoEncargado";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_NAVBAR = "nombreEncargado";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";
    private final String URL_LISTA = "/medicamento-ver-todo";
    private final String URL_MENU_MEDICAMENTO = "/menu-medicamento";
    private final String URL_HTML_FORMULARIO_MEDICAMENTO = "inventarioGeneral/formularios/medicamento_formulario";
    private final String URL_HTML_LISTA_MEDICAMENTO = "inventarioGeneral/listas/medicamento_lista";
    private final String URL_HTML_INFORMACION_MEDICAMENTO = "inventarioGeneral/informacion/medicamento_informacion";
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
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/medicamento-nuevo")
    public String montarFormularioParaNuevoMedicamento(Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo medicamento
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Medicamento");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Medicamento");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/medicamento-guardar");
        }
        modelo.addAttribute("medicamentoTO", new MedicamentoTO());
        return URL_HTML_FORMULARIO_MEDICAMENTO;
    }

    /**
     * Metodo que permite guardar un nuevo medicamento
     * @param medicamentoTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/medicamento-guardar")
    public String guardarMedicamento(@Validated MedicamentoTO medicamentoTO, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo medicamento (Si es que
        // se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Medicamento");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Medicamento");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Guardar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/medicamento-guardar");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("medicamentoTO", medicamentoTO);
            return URL_HTML_FORMULARIO_MEDICAMENTO;
        }

        Proveedor proveedor = proveedorService.buscarPorRucONombreEmpresa(medicamentoTO.getParametro(),
                medicamentoTO.getParametro());

        if (proveedor == null) {
            modelo.addAttribute("msgError", "No existe ese proveedor en la base de datos");
            return URL_HTML_FORMULARIO_MEDICAMENTO;
        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {
            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO;
        }

        Medicamento medicamentoGuardado = inventarioService.guardarNuevoMedicamento(medicamentoTO, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (medicamentoGuardado != null) {
            redirectAttributes.addFlashAttribute("msgExito",
                    "Medicamento con código " + medicamentoGuardado.getCodigo() + " se guardado exitosamente");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO;
        } else {
            modelo.addAttribute("medicamentoTO", medicamentoTO);
            modelo.addAttribute("msgError", "Error al guardar el medicamento");
            return URL_HTML_FORMULARIO_MEDICAMENTO;
        }
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param modelo
     * @param existeBusquedaPevia
     * @param session
     * @return
     */
    @GetMapping("/medicamento-ver-todo")
    public String listarMedicamentoes(Model modelo,
            @RequestParam(name = "existeBusquedaPevia", required = false) Boolean existeBusquedaPevia,
            HttpSession session) {
        // Atributos para la lista de medicamentos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Medicamentos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
        }
        Boolean pasoAnterior = (Boolean) session.getAttribute("pasoAnterior");

        List<Medicamento> medicamentos = new ArrayList<>();
        String codigo = (String) session.getAttribute("medicamentoPorCodigo");
        String proveedor = (String) session.getAttribute("medicamentoPorProveedor");
        String nombreComun = (String) session.getAttribute("medicamentoPorNombreComun");

        if (existeBusquedaPevia != null && existeBusquedaPevia) {
            medicamentos = busquedasPevias(codigo, proveedor, nombreComun);
        } else if (pasoAnterior != null && pasoAnterior) {
            medicamentos = busquedasPevias(codigo, proveedor, nombreComun);
        
        } else {
            medicamentos = inventarioService.todosLosMedicamentos();
            session.setAttribute("medicamentoPorCodigo", null);
            session.setAttribute("medicamentoPorProveedor", null);
            session.setAttribute("medicamentoPorNombreComun", null);
        }

        if (medicamentos == null || medicamentos.isEmpty()) {
            modelo.addAttribute("msgError", "No hay medicamentos registrados");
            return URL_HTML_LISTA_MEDICAMENTO;
        }
        // Agregar la lista de medicamentos al modelo
        modelo.addAttribute("listaMedicamento", medicamentos);

        return URL_HTML_LISTA_MEDICAMENTO;
    }

    /**
     * Metodo que permite buscar un medicamento por su código
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/medicamento-buscar-por-codigo")
    public String buscarPorRuc(@RequestParam("codigo") String codigo, Model modelo, HttpSession session) {
        // Atributos para la lista de medicamentos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Medicamentos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // medicamentos Médicos y evitar repetir escribir nuevamente el dato igresado
            session.setAttribute("medicamentoPorCodigo", codigo);
            session.setAttribute("medicamentoPorProveedor", null);
            session.setAttribute("medicamentoPorNombreComun", null);
        }

        Medicamento medicamento = inventarioService.buscarMedicamentoPorCodigo(codigo);
        if (medicamento == null) {
            modelo.addAttribute("msgError", "No se encontró un medicamento con ese código");
            return URL_HTML_LISTA_MEDICAMENTO;
        }
        List<Medicamento> medicamentos = new ArrayList<>();
        medicamentos.add(medicamento);
        modelo.addAttribute("listaMedicamento", medicamentos);
        modelo.addAttribute("msgExito", "Medicamento con código: " + codigo + " encontrado");
        return URL_HTML_LISTA_MEDICAMENTO;
    }

    /**
     * Metodo que permite buscar un medicamento por su proveedor
     * @param proveedor
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/medicamento-buscar-por-proveedor")
    public String buscarPorEmpresa(@RequestParam("proveedor") String proveedor, Model modelo,
            HttpSession session) {
        // Atributos para la lista de medicamentos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Medicamentos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // medicamentos Médicos
            session.setAttribute("medicamentoPorCodigo", null);
            session.setAttribute("medicamentoPorProveedor", proveedor);
            session.setAttribute("medicamentoPorNombreComun", null);

        }

        List<Medicamento> medicamentos = inventarioService.buscarMedicamentoPorProveedor(proveedor, proveedor);
        if (medicamentos == null || medicamentos.isEmpty()) {
            modelo.addAttribute("msgError", "El proveedor: " + proveedor + " no tiene medicamentos registrados");
            return URL_HTML_LISTA_MEDICAMENTO;
        }

        modelo.addAttribute("listaMedicamento", medicamentos);
        modelo.addAttribute("msgExito", "InsumoS Médicos con proveedor: " + proveedor + " encontrado");
        return URL_HTML_LISTA_MEDICAMENTO;
    }

    /**
     * Metodo que permite buscar un medicamento por su nombre común
     * @param nombreComun
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/medicamento-buscar-por-nombre-comun")
    public String buscarPorNombreComun(@RequestParam("nombreComun") String nombreComun, Model modelo,
            HttpSession session) {
        // Atributos para la lista de medicamentos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Medicamentos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // medicamentos Médicos
            session.setAttribute("medicamentoPorCodigo", null);
            session.setAttribute("medicamentoPorProveedor", null);
            session.setAttribute("medicamentoPorNombreComun", nombreComun);
        }

        List<Medicamento> medicamentos = inventarioService.buscarMedicamentoPorNombreComun(nombreComun);
        if (medicamentos == null || medicamentos.isEmpty()) {
            modelo.addAttribute("msgError", "No existen medicamentos con el nombre común: " + nombreComun);
            return URL_HTML_LISTA_MEDICAMENTO;
        }

        modelo.addAttribute("listaMedicamento", medicamentos);
        modelo.addAttribute("msgExito", "Existen medicamentos con el nombre común: " + nombreComun);
        return URL_HTML_LISTA_MEDICAMENTO;
    }

    /**
     * Metodo que permite generar un pdf con la lista de medicamentos
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/medicamento-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfMedicamento(HttpSession session) throws IOException {
        String codigo = (String) session.getAttribute("medicamentoPorCodigo");
        String proveedor = (String) session.getAttribute("medicamentoPorProveedor");
        String nombreComun = (String) session.getAttribute("medicamentoPorNombreComun");
        List<Medicamento> medicamentos = busquedasPevias(codigo, proveedor, nombreComun);
        ListarMedicamentoPDF listaMedicamentoPDF = new ListarMedicamentoPDF();
        ByteArrayOutputStream byteArrayOutputStream = listaMedicamentoPDF.generatePdfmedicamentos(medicamentos);

        String filename = "filename=" + "medicamentos" + LocalDateTime.now() + ".pdf";

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
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/medicamento-actualizar/{codigo}")
    public String montarFormularioParaActualizarMedicamento(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        // Atributos para el formulario de actualización de un medicamento
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Actualizar Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Insumo Meéico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Actualizar Medicamento: " + codigo);
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Actualizar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/medicamento-ver-todo/" + codigo);
        }
        // Para saber que existen datos necesarios para la busqueda previa
        session.setAttribute("existeBusquedaPevia", true);

        MedicamentoTO medicamentoTO = inventarioService.contruMedicamentoTO(codigo);
        // Agregar el medicamentoTO al modelo
        modelo.addAttribute("medicamentoTO", medicamentoTO);
        return URL_HTML_FORMULARIO_MEDICAMENTO;
    }

    /**
     * Metodo que permite actualizar un medicamento
     * @param codigo
     * @param medicamentoTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/medicamento-ver-todo/{codigo}")
    public String actualizarMedicamento(@PathVariable String codigo, @Validated MedicamentoTO medicamentoTO,
            BindingResult result,
            RedirectAttributes redirectAttributes, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualizacion de un medicamento (Si es que se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Actualizar Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Actualizar Medicamento");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Actualizar Medicamento: " + codigo);
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Actualizar");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_MEDICAMENTO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/medicamento-guardar");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("medicamentoTO", medicamentoTO);
            return URL_HTML_FORMULARIO_MEDICAMENTO;
        }

        Proveedor proveedorBase = proveedorService.buscarPorRucONombreEmpresa(medicamentoTO.getParametro(),
                medicamentoTO.getParametro());

        if (proveedorBase == null) {
            modelo.addAttribute("msgError", "No existe ese proveedor en la base de datos");
            return URL_HTML_FORMULARIO_MEDICAMENTO;
        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {

            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
        }

        Medicamento medicamentoGuardado = inventarioService.actualizarMedicamento(medicamentoTO, codigo,
                nombreUsuarioActual, cedulaUsuarioActual);
        if (medicamentoGuardado != null) {
            // Valores guardados
            String codigoPaso = (String) session.getAttribute("medicamentoPorCodigo");
            String proveedor = (String) session.getAttribute("medicamentoPorProveedor");
            String nombreComun = (String) session.getAttribute("medicamentoPorNombreComun");
            if (codigoPaso != null || proveedor != null || nombreComun != null) {
                redirectAttributes.addAttribute("existeBusquedaPevia", true);
            }
            redirectAttributes.addFlashAttribute("msgExito",
                    "Medicamento con código: " + medicamentoGuardado.getCodigo() + " actualizado exitosamente");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
        } else {
            modelo.addAttribute("medicamentoTO", medicamentoTO);
            modelo.addAttribute("msgError", "Error al actualizar el medicamento");
            return URL_HTML_FORMULARIO_MEDICAMENTO;
        }
    }

    /**
     * Metodo que permite visualizar el menu de administracion general
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/medicamento-ver-mas-detalles/{codigo}")
    public String verInformacionPersonal(@PathVariable String codigo, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualización de un medicamento
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Ver Detalles Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Detalles Medicamento");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloListaInformacion", "Detalles del Medicamento: " + codigo);
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + URL_LISTA + "/" + codigo);
        }
        String codigoPaso = (String) session.getAttribute("medicamentoPorCodigo");
        String proveedor = (String) session.getAttribute("medicamentoPorProveedor");
        String nombreComun = (String) session.getAttribute("medicamentoPorNombreComun");
        boolean pasoAnterior = codigoPaso != null || proveedor != null || nombreComun != null;
        session.setAttribute("pasoAnterior", pasoAnterior);
        // Buscar el medicamento por Ruc
        Medicamento medicamento = inventarioService.buscarMedicamentoPorCodigo(codigo);
        // Convertir el medicamento a un MedicamentoTO
        modelo.addAttribute("medicamento", medicamento);
        return URL_HTML_INFORMACION_MEDICAMENTO;
    }

    public List<Medicamento> busquedasPevias(String codigo, String proveedor, String nombreComun) {
        List<Medicamento> medicamentos = new ArrayList<>();
        if (codigo != null) {
            Medicamento medicamento = inventarioService.buscarMedicamentoPorCodigo(codigo);
            if (medicamento != null) {
                medicamentos.add(medicamento);
            }
        }
        if (proveedor != null) {
            medicamentos = inventarioService.buscarMedicamentoPorProveedor(proveedor, proveedor);
        }
        if (nombreComun != null) {
            medicamentos = inventarioService.buscarMedicamentoPorNombreComun(nombreComun);
        }
        if (codigo == null && proveedor == null && nombreComun == null) {
            medicamentos = inventarioService.todosLosMedicamentos();
        }
        return medicamentos;
    }

}
