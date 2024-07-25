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
import com.uce.edu.repository.modelo.inventarioModelo.PedidoMedicamento;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.inventarioService.interfaceService.IPedididoService;
import com.uce.edu.util.Enum.EstadoPedido;

import com.uce.edu.util.reportes.inventario.ListarPedidosMedicamentosPDF;

import jakarta.servlet.http.HttpSession;

/**
 * PedidoMedicamentoController
 * 
 */
@Controller
@RequestMapping("administracion-inventario")
public class PedidoMedicamentoController {
    /**
     * Información de los session y url de formularios html
     */
    private final String TITULO_NAVBAR = "Inventario";
    private final String NOMBRE_USUARIO_SESSION = "nombrePasoEncargado";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_NAVBAR = "nombreEncargado";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";
    private final String URL_LISTA = "/pedido-medicamento-ver-todo";
    private final String URL_MENU_PEDIDO = "/menu-pedido";
    private final String URL_HTML_LISTA_PEDIDO_INSUMO = "inventarioGeneral/listas/pedido_medicamento_lista";
    private final String URL_HTML_INFORMACION_PEDIDO_INSUMO = "inventarioGeneral/informacion/pedido_medicamento_informacion";
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
     * Metodo que permite visualizar el menu de administracion inventario
     * medicamentos
     * 
     * @param modelo
     * @param existeBusquedaPevia
     * @param session
     * @return
     */
    @GetMapping("/pedido-medicamento-ver-todo")
    public String listarPedidosMedicamentos(Model modelo,
            @RequestParam(name = "existeBusquedaPevia", required = false) Boolean existeBusquedaPevia,
            HttpSession session) {
        // Atributos para la lista de proveedores
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedidos Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista de Pedidos Medicamento");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
        }
        Boolean pasoAnterior = (Boolean) session.getAttribute("pasoAnterior");

        // Obtener la lista de proveedores
        List<PedidoMedicamento> PedidoMedicamento = new ArrayList<>();
        String proveedoresParametro = (String) session.getAttribute("proveedoresParametro");
        String pedidoMedicamentoNombreOCodigo = (String) session.getAttribute("pedidoMedicamentoNombreOCodigo");
        String pedidoMedicamentoEstado = (String) session.getAttribute("pedidoMedicamentoEstado");

        if ((existeBusquedaPevia != null && existeBusquedaPevia)) {
            PedidoMedicamento = busquedasPevias(proveedoresParametro, pedidoMedicamentoNombreOCodigo,
                    pedidoMedicamentoEstado);

        } else if (pasoAnterior != null && pasoAnterior) {
            PedidoMedicamento = busquedasPevias(proveedoresParametro, pedidoMedicamentoNombreOCodigo,
                    pedidoMedicamentoEstado);

        } else {
            PedidoMedicamento = pedidoService.obtenerTodosLosPedidosDeMedicamento();
            session.setAttribute("proveedoresParametro", null);
            session.setAttribute("pedidoMedicamentoNombreOCodigo", null);
            session.setAttribute("pedidoMedicamentoEstado", null);
        }

        // List<String> listaEstados = EstadoPedido.convertirEnumALista();
        modelo.addAttribute("estados", listaEstados);
        // Atributos para
        modelo.addAttribute("listaPedidos", PedidoMedicamento);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);

        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite buscar un pedido de medicamento por proveedor
     * 
     * @param proveedor
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pedido-medicamento-buscar-por-proveedor")
    public String buscarPorEmpresa(@RequestParam("proveedor") String proveedor, Model modelo,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedidos Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Pedidos Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("proveedoresParametro", proveedor);
            session.setAttribute("pedidoMedicamentoNombreOCodigo", null);
            session.setAttribute("pedidoMedicamentoEstado", null);
        }

        List<PedidoMedicamento> PedidoMedicamento = pedidoService.buscarPedidoMedicamentoPorProveedor(proveedor,
                proveedor);
        if (PedidoMedicamento == null || PedidoMedicamento.isEmpty()) {
            modelo.addAttribute("msgError", "El proveedor: " + proveedor + " no tiene pedidos registrados");
            modelo.addAttribute("estados", listaEstados);
            return URL_HTML_LISTA_PEDIDO_INSUMO;
        }

        modelo.addAttribute("listaPedidos", PedidoMedicamento);
        // List<String> listaEstados = EstadoPedido.convertirEnumALista();
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        modelo.addAttribute("msgExito", "InsumoS Médicos con proveedor: " + proveedor + " encontrado");
        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite buscar un pedido de medicamento por codigo
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pedido-medicamento-buscar-por-codigo")
    public String buscarPorCodigo(@RequestParam("codigo") String codigo, Model modelo, HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedidos Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Pedidos Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("proveedoresParametro", null);
            session.setAttribute("pedidoMedicamentoNombreOCodigo", codigo);
            session.setAttribute("pedidoMedicamentoEstado", null);
        }

        List<PedidoMedicamento> PedidoMedicamento = pedidoService.buscarPedidoMedicamentoPorCodigoONombreInsumo(codigo);

        if (PedidoMedicamento == null || PedidoMedicamento.isEmpty()) {
            modelo.addAttribute("msgError", "No se encontraron pedidos para el medicamento con ese código o nombre");
            modelo.addAttribute("estados", listaEstados);
            return URL_HTML_LISTA_PEDIDO_INSUMO;
        }

        modelo.addAttribute("listaPedidos", PedidoMedicamento);
        // List<String> listaEstados = EstadoPedido.convertirEnumALista();
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        modelo.addAttribute("msgExito", "Pedido de Medicamento con código: " + codigo + " encontrado");
        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite buscar un pedido de medicamento por estado
     * 
     * @param estado
     * @param modelo
     * @param session
     * @return
     */
    @PostMapping("/pedido-medicamento-buscar-por-estado")
    public String buscarPorEstado(@RequestParam("estado") String estado, Model modelo,
            HttpSession session) {
        // Atributos para la lista de insumosMedicos
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Lista Pedido Medicamentos");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Ver Pedidos");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título de la lista
            modelo.addAttribute("tituloLista", "Lista Pedido Medicamentos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Actualizamos los atributos de busqueda guardado en la session para generar
            // lista de
            // insumos Médicos
            session.setAttribute("proveedoresParametro", null);
            session.setAttribute("pedidoMedicamentoNombreOCodigo", null);
            session.setAttribute("pedidoMedicamentoEstado", estado);
        }
        List<PedidoMedicamento> PedidoMedicamento = pedidoService.buscarPedidoMedicamentoPorEstado(estado);

        if (PedidoMedicamento == null || PedidoMedicamento.isEmpty()) {
            modelo.addAttribute("msgError", "No existen pedidos con el estado: " + estado);
            return URL_HTML_LISTA_PEDIDO_INSUMO;
        }
        modelo.addAttribute("listaPedidos", PedidoMedicamento);
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        modelo.addAttribute("msgExito", "Existen medicamentos con el nombre común: " + estado);
        return URL_HTML_LISTA_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite generar un pdf de los pedidos de medicamentos
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/pedido-medicamento-generar-pdf")
    public ResponseEntity<InputStreamResource> generarPdfPedidosMedicamentos(HttpSession session) throws IOException {
        String proveedor = (String) session.getAttribute("proveedoresParametro");
        String codigo = (String) session.getAttribute("pedidoMedicamentoNombreOCodigo");
        String estado = (String) session.getAttribute("pedidoMedicamentoEstado");
        List<PedidoMedicamento> medicamentos = busquedasPevias(codigo, proveedor, estado);
        ListarPedidosMedicamentosPDF listaMedicamentosoPDF = new ListarPedidosMedicamentosPDF();
        ByteArrayOutputStream byteArrayOutputStream = listaMedicamentosoPDF
                .generatePdfPedidosMedicamentos(medicamentos);

        String filename = "filename=" + "pedidos_medicamentos_medicos" + LocalDateTime.now() + ".pdf";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + filename); // Cambiado de "attachment" a "inline"

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
    }

    /**
     * Metodo que permite ver los detalles de un pedido de medicamento
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-medicamento-ver-mas-detalles/{codigo}")
    public String verInformacionPersonal(@PathVariable String codigo, Model modelo, HttpSession session) {
        // Atributos para el formulario de actualización de un medicamento
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Ver Detalles Pedido de Medicamento");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Detalles Pedido de Medicamento");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloListaInformacion", "Detalles del Pedido de Medicamento: " + codigo);
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_LISTA);

        }

        String proveedor = (String) session.getAttribute("proveedoresParametro");
        String codigoPaso = (String) session.getAttribute("pedidoMedicamentoNombreOCodigo");
        String estado = (String) session.getAttribute("pedidoMedicamentoEstado");
        boolean pasoAnterior = codigoPaso != null || proveedor != null || estado != null;
        session.setAttribute("pasoAnterior", pasoAnterior);

        // Buscar el medicamento por Ruc
        PedidoMedicamento pedidoInsumoMedico = pedidoService.buscarPedidoMedicamentoPorCodigo(codigo);
        modelo.addAttribute("pedidoInsumo", pedidoInsumoMedico);
        modelo.addAttribute("estados", listaEstados);
        // Si el pedido es pendiente se muestran los botones para editar en la lista
        modelo.addAttribute("estadoEvaluador", ESTADO_EVALUADOR);
        return URL_HTML_INFORMACION_PEDIDO_INSUMO;
    }

    /**
     * Metodo que permite recibir un pedido de medicamento
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-medicamento-recibir/{codigo}")
    public String recibirPedidoMedicamento(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();
        PedidoMedicamento pedidoInsumoMedico = pedidoService.recibirPedidoMedicamento(codigo, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (pedidoInsumoMedico != null) {
            modelo.addAttribute("msgExito", "Pedido de medicamento recibido con éxito");
        } else {
            modelo.addAttribute("msgError", "No se pudo recibir el pedido de medicamento");
        }
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
    }

    /**
     * Metodo que permite rechazar un pedido de medicamento
     * 
     * @param codigo
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-medicamento-rechazar/{codigo}")
    public String rechazarPedidoMedicamento(@PathVariable String codigo, Model modelo,
            HttpSession session) {
        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = UsuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();
        PedidoMedicamento pedidoInsumoMedico = pedidoService.rechazarPedidoMedicamento(codigo, nombreUsuarioActual,
                cedulaUsuarioActual);
        if (pedidoInsumoMedico != null) {
            modelo.addAttribute("msgExito", "Pedido de medicamento fue rechazado con éxito");
        } else {
            modelo.addAttribute("msgError", "No se pudo rechazar el pedido de medicamento");
        }
        return "redirect:" + URL_MENU_PRINCIPAL + URL_LISTA;
    }

    /**
     * Metodo que permite buscar pedidos de medicamentos previos
     * 
     * @param proveedor
     * @param codigo
     * @param estado
     * @return
     */
    public List<PedidoMedicamento> busquedasPevias(String proveedor, String codigo, String estado) {
        List<PedidoMedicamento> pedidoMedicamento = new ArrayList<>();
        if (proveedor != null) {
            pedidoMedicamento = pedidoService.buscarPedidoMedicamentoPorProveedor(proveedor, proveedor);

        }
        if (codigo != null) {
            pedidoMedicamento = pedidoService.buscarPedidoMedicamentoPorCodigoONombreInsumo(codigo);
        }
        if (estado != null) {
            pedidoMedicamento = pedidoService.buscarPedidoMedicamentoPorEstado(estado);
        }
        if (codigo == null && proveedor == null && estado == null) {
            pedidoMedicamento = pedidoService.obtenerTodosLosPedidosDeMedicamento();
        }
        return pedidoMedicamento;
    }

}
