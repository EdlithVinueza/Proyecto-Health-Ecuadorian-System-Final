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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.inventarioModelo.PedidoInsumoMedico;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.inventarioService.interfaceService.IPedididoService;
import com.uce.edu.util.Enum.EstadoPedido;

import com.uce.edu.util.reportes.inventario.ListarPedidosInsumosMedicosPDF;

import jakarta.servlet.http.HttpSession;

/**
 * PedidoInsumoMedicoController
 * 
 */
@Controller
@RequestMapping("administracion-inventario")
public class PedidoInsumoMedicoController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Inventario";
    private final String NOMBRE_USUARIO_SESSION = "nombrePasoEncargado";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_NAVBAR = "nombreEncargado";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";
    private final String URL_LISTA = "/pedido-insumo-ver-todo";
    private final String URL_MENU_PEDIDO = "/menu-pedido";
    private final String URL_HTML_LISTA_PEDIDO_INSUMO = "inventarioGeneral/listas/pedido_insumo_lista";
    private final String URL_HTML_INFORMACION_PEDIDO_INSUMO = "inventarioGeneral/informacion/pedido_insumo_informacion";

    private static final String ESTADO_EVALUADOR = EstadoPedido.PENDIENTE.getEstado();

    /**
     * Dependencias
     */
    @Autowired
    private IPedididoService pedidoService;

    @Autowired
    private IUsuarioService UsuarioService;

    /**
     * Lista de estados de los pedidos
     */
    private static final List<String> listaEstados;

    static {
        listaEstados = EstadoPedido.convertirEnumALista();
    }

    /**
     * Metodo que permite visualizar la lista de insumos médicos
     * 
     * @param modelo
     * @param existeBusquedaPevia
     * @param session
     * @return
     */
    @GetMapping("/pedido-insumo-ver-todo")
    public String listarPedidosInsumos(Model modelo,
            @RequestParam(name = "existeBusquedaPevia", required = false) Boolean existeBusquedaPevia,
            HttpSession session) {
        // Atributos para la lista de proveedores
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedidos Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Pedidos Insumo Médico");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
        }
        Boolean pasoAnterior = (Boolean) session.getAttribute("pasoAnterior");

        // Obtener la lista de proveedores
        List<PedidoInsumoMedico> PedidoInsumoMedico = new ArrayList<>();
        String proveedoresParametro = (String) session.getAttribute("proveedoresParametro");
        String pedidoInsumoNombreOCodigo = (String) session.getAttribute("pedidoInsumoNombreOCodigo");
        String pedidoInsumoEstado = (String) session.getAttribute("pedidoInsumoEstado");

        if ((existeBusquedaPevia != null && existeBusquedaPevia)) {
            PedidoInsumoMedico = busquedasPevias(proveedoresParametro, pedidoInsumoNombreOCodigo, pedidoInsumoEstado);

        } else if (pasoAnterior != null && pasoAnterior) {
            PedidoInsumoMedico = busquedasPevias(proveedoresParametro, pedidoInsumoNombreOCodigo, pedidoInsumoEstado);

        } else {
            PedidoInsumoMedico = pedidoService.obtenerTodosLosPedidosDeInsumos();
            session.setAttribute("proveedoresParametro", null);
            session.setAttribute("pedidoInsumoNombreOCodigo", null);
            session.setAttribute("pedidoInsumoEstado", null);
        }

        // List<String> listaEstados = EstadoPedido.convertirEnumALista();
        modelo.addAttribute("estados", listaEstados);
        // Atributos para
        modelo.addAttribute("listaPedidos", PedidoInsumoMedico);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);

        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite buscar un insumo por el nombre común
     * 
     * @param proveedor
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pedido-insumo-buscar-por-proveedor")
    public String buscarPorEmpresa(@RequestParam("proveedor") String proveedor, Model modelo,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedidos Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Pedidos Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("proveedoresParametro", proveedor);
            session.setAttribute("pedidoInsumoNombreOCodigo", null);
            session.setAttribute("pedidoInsumoEstado", null);
        }

        List<PedidoInsumoMedico> PedidoInsumoMedico = pedidoService.buscarPedidoInsumoMedicoPorProveedor(proveedor,
                proveedor);
        if (PedidoInsumoMedico == null || PedidoInsumoMedico.isEmpty()) {
            modelo.addAttribute("msgError", "El proveedor: " + proveedor + " no tiene pedidos registrados");
            modelo.addAttribute("estados", listaEstados);
            return URL_HTML_LISTA_PEDIDO_INSUMO;
        }

        modelo.addAttribute("listaPedidos", PedidoInsumoMedico);
        // List<String> listaEstados = EstadoPedido.convertirEnumALista();
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        modelo.addAttribute("msgExito", "InsumoS Médicos con proveedor: " + proveedor + " encontrado");
        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite buscar un insumo por el código
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pedido-insumo-buscar-por-codigo")
    public String buscarPorRuc(@RequestParam("codigo") String codigo, Model modelo, HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedidos Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Pedidos Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("proveedoresParametro", null);
            session.setAttribute("pedidoInsumoNombreOCodigo", codigo);
            session.setAttribute("pedidoInsumoEstado", null);
        }

        List<PedidoInsumoMedico> PedidoInsumoMedico = pedidoService
                .buscarPedidoInsumoMedicoPorCodigoONombreInsumo(codigo);

        if (PedidoInsumoMedico == null || PedidoInsumoMedico.isEmpty()) {
            modelo.addAttribute("msgError", "No se encontraron pedidos para el insumo con ese código o nombre");
            modelo.addAttribute("estados", listaEstados);
            return URL_HTML_LISTA_PEDIDO_INSUMO;
        }

        modelo.addAttribute("listaPedidos", PedidoInsumoMedico);
        // List<String> listaEstados = EstadoPedido.convertirEnumALista();
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        modelo.addAttribute("msgExito", "Pedido de Insumo medico con código: " + codigo + " encontrado");
        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite buscar un insumo por el nombre común
     * 
     * @param estado
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pedido-insumo-buscar-por-estado")
    public String buscarPorNombreComun(@RequestParam("estado") String estado, Model modelo,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedido Insumo Médicos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Pedido Insumo Médicos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("proveedoresParametro", null);
            session.setAttribute("pedidoInsumoNombreOCodigo", null);
            session.setAttribute("pedidoInsumoEstado", estado);
        }
        List<PedidoInsumoMedico> PedidoInsumoMedico = pedidoService.buscarPedidoInsumoMedicoPorEstado(estado);

        if (PedidoInsumoMedico == null || PedidoInsumoMedico.isEmpty()) {
            modelo.addAttribute("msgError", "No existen pedidos con el estado: " + estado);
            return URL_HTML_LISTA_PEDIDO_INSUMO;
        }
        modelo.addAttribute("listaPedidos", PedidoInsumoMedico);
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        modelo.addAttribute("msgExito", "Existen insumos con el nombre común: " + estado);
        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite generar un pdf de la lista de insumos médicos
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/pedido-insumo-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfInsumo(HttpSession session) throws IOException {
        String proveedor = (String) session.getAttribute("proveedoresParametro");
        String codigo = (String) session.getAttribute("pedidoInsumoNombreOCodigo");
        String estado = (String) session.getAttribute("pedidoInsumoEstado");
        List<PedidoInsumoMedico> insumosMedicos = busquedasPevias(codigo, proveedor, estado);
        ListarPedidosInsumosMedicosPDF listaInsumoMedicoPDF = new ListarPedidosInsumosMedicosPDF();
        ByteArrayOutputStream byteArrayOutputStream = listaInsumoMedicoPDF
                .generatePdfPedidosInsumosMedicos(insumosMedicos);

        String filename = "filename=" + "pedidos_insumos_medicos" + LocalDateTime.now() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

    /**
     * Metodo que permite ver más detalles de un insumo
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-insumo-ver-mas-detalles/{codigo}")
    public String verInformacionPersonal(@PathVariable String codigo, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualización de un insumo
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Ver Detalles Pedido de Insumo Médico");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Detalles Pedido de Insumo Médico");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloListaInformacion", "Detalles del Pedido de Insumo Médico: " + codigo);
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);

        }

        String proveedor = (String) session.getAttribute("proveedoresParametro");
        String codigoPaso = (String) session.getAttribute("pedidoInsumoNombreOCodigo");
        String estado = (String) session.getAttribute("pedidoInsumoEstado");
        boolean pasoAnterior = codigoPaso != null || proveedor != null || estado != null;
        session.setAttribute("pasoAnterior", pasoAnterior);

        // Buscar el insumo por Ruc
        PedidoInsumoMedico pedidoInsumoMedico = pedidoService.buscarPedidoInsumoMedicoPorCodigo(codigo);
        modelo.addAttribute("pedidoInsumo", pedidoInsumoMedico);
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        return URL_HTML_INFORMACION_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite recibir un pedido de insumo médico
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-insumo-recibir/{codigo}")
    public String recibirPedidoInsumoMedico(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();
        PedidoInsumoMedico pedidoInsumoMedico = pedidoService.recibirPedidoInsumoMedico(codigo, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (pedidoInsumoMedico != null) {
            modelo.addAttribute("msgExito", "Pedido de insumo médico recibido con éxito");
        } else {
            modelo.addAttribute("msgError", "No se pudo recibir el pedido de insumo médico");
        }
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
    }

    /**
     * Metodo que permite rechazar un pedido de insumo médico
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-insumo-rechazar/{codigo}")
    public String rechazarPedidoInsumoMedico(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();
        PedidoInsumoMedico pedidoInsumoMedico = pedidoService.rechazarPedidoInsumoMedico(codigo, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (pedidoInsumoMedico != null) {
            modelo.addAttribute("msgExito", "Pedido de insumo médico fue rechazado con éxito");
        } else {
            modelo.addAttribute("msgError", "No se pudo rechazar el pedido de insumo médico");
        }
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
    }

    /**
     * Metodo que permite buscar un pedido de insumo médico por proveedor, código o
     * estado
     * 
     * @param proveedor
     * @param codigo
     * @param estado
     * @return
     */
    public List<PedidoInsumoMedico> busquedasPevias(String proveedor, String codigo, String estado) {
        List<PedidoInsumoMedico> pedidoInsumoMedicos = new ArrayList<>();
        if (proveedor != null) {
            pedidoInsumoMedicos = pedidoService.buscarPedidoInsumoMedicoPorProveedor(proveedor, proveedor);

        }
        if (codigo != null) {
            pedidoInsumoMedicos = pedidoService.buscarPedidoInsumoMedicoPorCodigoONombreInsumo(codigo);
        }
        if (estado != null) {
            pedidoInsumoMedicos = pedidoService.buscarPedidoInsumoMedicoPorEstado(estado);
        }
        if (codigo == null && proveedor == null && estado == null) {
            pedidoInsumoMedicos = pedidoService.obtenerTodosLosPedidosDeInsumos();
        }
        return pedidoInsumoMedicos;
    }

}
