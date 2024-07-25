package com.uce.edu.controller.usuarios.inventarioGeneralController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.uce.edu.repository.inventarioRepository.IInsumoMedicoRepository;
import com.uce.edu.repository.inventarioRepository.IMedicamentoRepository;
import com.uce.edu.repository.modelo.Usuario;
import com.uce.edu.repository.modelo.inventarioModelo.InsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;
import com.uce.edu.repository.modelo.inventarioModelo.PedidoInsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.PedidoMedicamento;
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.interfacesSistemaPrincipal.IUsuarioService;
import com.uce.edu.service.inventarioService.interfaceService.IPedididoService;
import com.uce.edu.service.inventarioService.interfaceService.IProveedorService;
import com.uce.edu.service.to.inventarioTO.PedidoTO;
import com.uce.edu.util.Enum.PrefijoProducto;

import jakarta.servlet.http.HttpSession;;
/**
 * PedidoGeneralController
 
 */
@Controller
@RequestMapping("administracion-inventario")
public class PedidoGeneralController {
/**
 * Información de los session y url de formularios html
 */
    private final String TITULO_NAVBAR = "Inventario";
    private final String NOMBRE_USUARIO_SESSION = "nombrePasoEncargado";
    private final String CEDULA_USUARIO_SESSION = "cedulaPasoEncargado";
    private final String NOMBRE_NAVBAR = "nombreEncargado";
    private final String URL_MENU_PRINCIPAL = "/administracion-inventario";
    private final String URL_MENU_PEDIDO = "/menu-pedido";
    private final String URL_HTML_FORMULARIO_PEDIDO = "inventarioGeneral/formularios/pedido_formulario";

    /**
     * Dependencias
     */
    @Autowired
    private IPedididoService pedidoService;
    @Autowired
    private IProveedorService proveedorService;
    @Autowired
    private IUsuarioService usuarioService;
    @Autowired
    private IMedicamentoRepository medicamentoRepository;
    @Autowired
    private IInsumoMedicoRepository insumoMedicoRepository;

    /**
     * Metodo que permite visualizar formulario de creación de un nuevo pedido
     * @param modelo
     * @param session
     * @return
     */
    @GetMapping("/pedido-nuevo")
    public String montarFormularioParaNuevoPedido(Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo pedido
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Pedido");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Pedido");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Pedido");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Confirmar Datos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pedido-vista-previa");
        }
        PedidoTO pedidoTO = (PedidoTO) session.getAttribute("miPedidoTO");
        modelo.addAttribute("pedidoMedicamento", null);
        modelo.addAttribute("pedidoInsumo", null);

        if (pedidoTO != null) {
            modelo.addAttribute("pedidoTO", pedidoTO);
            modelo.addAttribute("msgAdvertencia", "No se ha confirmado el pedido anterior, por favor confirmar o regresar, o ingresa nuevos datos");
        } else {
            modelo.addAttribute("pedidoTO", new PedidoTO());
        }

        return URL_HTML_FORMULARIO_PEDIDO;
    }
    /**
     * Metodo que permite visualizar la vista previa del pedido
     * @param pedidoTO
     * @param result
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */

    @PostMapping("/pedido-vista-previa")
    public String vistaPreviaPedido(@Validated PedidoTO pedidoTO, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {
        // Atributos para el formulario de creación de un nuevo pedido (Si es que
        // se
        // dan errores)
        {
            // Títulos de la página
            modelo.addAttribute("tituloPestaña", "Crear Pedido");
            // Título del navbar
            modelo.addAttribute("tituloNavbar", TITULO_NAVBAR + " - Crear Pedido");
            // nombre del usuario logueado para el navbar
            String nombreUsuario = (String) session.getAttribute(NOMBRE_USUARIO_SESSION);
            modelo.addAttribute(NOMBRE_NAVBAR, nombreUsuario);
            // Título del formulario
            modelo.addAttribute("tituloFormulario", "Crear un nuevo Pedido");
            // Texto del botón submit
            modelo.addAttribute("botonSubmit", "Confirmar Datos");
            // Url de retorno
            modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + URL_MENU_PEDIDO);
            // Url de acción del formulario
            modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pedido-vista-previa");
        }

        if (result.hasErrors()) {
            modelo.addAttribute("pedidoTO", pedidoTO);
            modelo.addAttribute("pedidoPedido", null);
            modelo.addAttribute("pedidoInsumo", null);
            return URL_HTML_FORMULARIO_PEDIDO;
        }

        Proveedor proveedor = proveedorService.buscarPorRucONombreEmpresa(pedidoTO.getParametro(),
                pedidoTO.getParametro());

        if (proveedor == null) {
            modelo.addAttribute("msgError", "No existe ese proveedor en la base de datos");
            modelo.addAttribute("pedidoTO", pedidoTO);
            modelo.addAttribute("pedidoPedido", null);
            modelo.addAttribute("pedidoInsumo", null);
            return URL_HTML_FORMULARIO_PEDIDO;
        }
        boolean esMedicamento = pedidoTO.getCodigoProducto().startsWith(PrefijoProducto.MEDICAMENTO.getEstado());
        Medicamento medicamento = null;
        boolean esInsumoMedico = pedidoTO.getCodigoProducto().startsWith(PrefijoProducto.INSUMO_MEDICO.getEstado());
        InsumoMedico insumoMedico = null;

        if (esMedicamento) {
            medicamento = medicamentoRepository.findByCodigo(pedidoTO.getCodigoProducto());
            if (medicamento == null) {
                modelo.addAttribute("msgError",
                        "No existe un medicamento en la base de datos con el código" + pedidoTO.getCodigoProducto());
                modelo.addAttribute("pedidoTO", pedidoTO);
                modelo.addAttribute("pedidoPedido", null);
                modelo.addAttribute("pedidoInsumo", null);
                return URL_HTML_FORMULARIO_PEDIDO;
            } else if (esInsumoMedico) {
                insumoMedico = insumoMedicoRepository.findByCodigo(pedidoTO.getCodigoProducto());
                if (insumoMedico == null) {
                    modelo.addAttribute("msgError", "No existe un insumo médico en la base de datos con el código"
                            + pedidoTO.getCodigoProducto());
                    modelo.addAttribute("pedidoTO", pedidoTO);
                    modelo.addAttribute("pedidoPedido", null);
                    modelo.addAttribute("pedidoInsumo", null);
                    return URL_HTML_FORMULARIO_PEDIDO;
                }
            } else {
                modelo.addAttribute("msgError", "El código del producto no es válido");
                modelo.addAttribute("pedidoTO", pedidoTO);
                modelo.addAttribute("pedidoPedido", null);
                modelo.addAttribute("pedidoInsumo", null);
                return URL_HTML_FORMULARIO_PEDIDO;
            }

        }

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = usuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {
            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PEDIDO;
        }
        session.setAttribute("miPedidoTO", pedidoTO);
        if (esMedicamento) {
            PedidoMedicamento pedidoMedicamento = pedidoService.antesDeGuardarPedidoMedicamento(pedidoTO,
                    nombreUsuarioActual, cedulaUsuarioActual);
            if (pedidoMedicamento == null) {
                modelo.addAttribute("pedidoTO", pedidoTO);
                modelo.addAttribute("pedidoMedicamento", null);
                modelo.addAttribute("pedidoInsumo", null);
                modelo.addAttribute("msgError", "Error al generar datos,no existe ese medicamento en nuestro sistema");
                modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + "/pedido-nuevo");
                return URL_HTML_FORMULARIO_PEDIDO;
            } else {
                modelo.addAttribute("pedidoTO", pedidoTO);
                modelo.addAttribute("pedidoMedicamento", pedidoMedicamento);
                modelo.addAttribute("pedidoInsumo", null);
                modelo.addAttribute("botonSubmit", "Pedir");
                modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pedido-guardar");
                modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + "/pedido-nuevo");

                return URL_HTML_FORMULARIO_PEDIDO;
            }
        }
        if (esInsumoMedico) {
            PedidoInsumoMedico pedidoInsumoMedico = pedidoService.antesDeGuardarPedidoInsumoMedico(pedidoTO,
                    nombreUsuarioActual, cedulaUsuarioActual);
            if (pedidoInsumoMedico == null) {
                modelo.addAttribute("pedidoTO", pedidoTO);
                modelo.addAttribute("pedidoMedicamento", null);
                modelo.addAttribute("pedidoInsumo", null);
                modelo.addAttribute("msgError", "Error al generar datos, no existe ese insumo médico en nuestro sistema");
                modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + "/pedido-nuevo");
                return URL_HTML_FORMULARIO_PEDIDO;
            } else {
                modelo.addAttribute("pedidoTO", pedidoTO);
                modelo.addAttribute("pedidoInsumo", pedidoInsumoMedico);
                modelo.addAttribute("pedidoMedicamento", null);
                modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pedido-nuevo");
                modelo.addAttribute("botonSubmit", "Pedir");
                modelo.addAttribute("urlAction", URL_MENU_PRINCIPAL + "/pedido-guardar");
                modelo.addAttribute("returnUrl", URL_MENU_PRINCIPAL + "/pedido-nuevo");

                return URL_HTML_FORMULARIO_PEDIDO;
            }
        }
        return URL_HTML_FORMULARIO_PEDIDO;
    }

    /**
     * Metodo que permite guardar un pedido
     * @param pedidoTO
     * @param pedidoMedicamento
     * @param pedidoInsumoMedico
     * @param redirectAttributes
     * @param modelo
     * @param session
     * @return
     */

    @PostMapping("/pedido-guardar")
    public String guardarPedido(@ModelAttribute("pedidoTO") PedidoTO pedidoTO,
            @ModelAttribute("pedidoMedicamento") PedidoMedicamento pedidoMedicamento,
            @ModelAttribute("pedidoInsumo") PedidoInsumoMedico pedidoInsumoMedico,
            RedirectAttributes redirectAttributes,
            Model modelo, HttpSession session) {

        boolean esMedicamento = pedidoTO.getCodigoProducto().startsWith(PrefijoProducto.MEDICAMENTO.getEstado());

        boolean esInsumoMedico = pedidoTO.getCodigoProducto().startsWith(PrefijoProducto.INSUMO_MEDICO.getEstado());

        String cedulaUsuarioActual = (String) session.getAttribute(CEDULA_USUARIO_SESSION);
        Usuario usuarioActual = usuarioService.buscarPorCedula(cedulaUsuarioActual);
        String nombreUsuarioActual = usuarioActual.getNombre() + " " + usuarioActual.getApellido();

        if (cedulaUsuarioActual == null || nombreUsuarioActual == null) {
            redirectAttributes.addFlashAttribute("msgError", "Error al obtener el usuario actual");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PEDIDO;
        }

        if (esMedicamento) {
            PedidoMedicamento pedidoGuardado = pedidoService.guardarNuevoPedidoMedicamento(pedidoTO,
                    nombreUsuarioActual, cedulaUsuarioActual);
            if (pedidoGuardado != null) {
                redirectAttributes.addFlashAttribute("msgExito",
                        "El pedido del medicamento: " + pedidoGuardado.getCodigo() + " se realizo con éxito");
                return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PEDIDO;
            }
        } else if (esInsumoMedico) {
            PedidoInsumoMedico pedidoGuardado = pedidoService.guardarNuevoPedidoInsumoMedico(pedidoTO,
                    nombreUsuarioActual, cedulaUsuarioActual);
            if (pedidoGuardado != null) {
                redirectAttributes.addFlashAttribute("msgExito",
                        "El pedido del insumo médico: " + pedidoGuardado.getCodigo() + " se realizo con éxito");
                return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PEDIDO;
            }
        } else {
            redirectAttributes.addFlashAttribute("msgError", "Error al generar el pedido");
            return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PEDIDO;
        }
        return "redirect:" + URL_MENU_PRINCIPAL + URL_MENU_PEDIDO;
    }

}
