package com.uce.edu.service.inventarioService.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.inventarioRepository.IInsumoMedicoRepository;
import com.uce.edu.repository.inventarioRepository.IMedicamentoRepository;
import com.uce.edu.repository.inventarioRepository.IPedidoInsumoMeditoRepository;
import com.uce.edu.repository.inventarioRepository.IPedidoMedicamentoRepository;
import com.uce.edu.repository.inventarioRepository.IProveedorRepository;
import com.uce.edu.repository.modelo.inventarioModelo.InsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;
import com.uce.edu.repository.modelo.inventarioModelo.PedidoInsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.PedidoMedicamento;
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.inventarioService.interfaceService.IInventarioService;
import com.uce.edu.service.inventarioService.interfaceService.IPedididoService;
import com.uce.edu.service.to.inventarioTO.PedidoTO;
import com.uce.edu.util.Enum.EstadoPedido;
import com.uce.edu.util.Enum.PrefijoPedido;

@Service
public class IPedididoServiceImpl implements IPedididoService {

    //Todos los metodos para Pedidos de Medicamentos

    @Autowired
    private IPedidoMedicamentoRepository pedidoMedicamentoRepository;

    @Autowired
    private IMedicamentoRepository medicamentoRepository;

    @Autowired
    private IPedidoInsumoMeditoRepository pedidoInsumoMeditoRepository;

    @Autowired
    private IInsumoMedicoRepository insumoMedicoRepository;

    @Autowired
    private IInventarioService inventarioService;

    @Autowired
    private IProveedorRepository proveedorRepository;

    @Override
    public List<PedidoMedicamento> obtenerTodosLosPedidosDeMedicamento() {
        return pedidoMedicamentoRepository.findAll();
    }

    @Override
    public PedidoMedicamento buscarPedidoMedicamentoPorCodigo(String codigo) {
        return pedidoMedicamentoRepository.findByCodigo(codigo);
    }

    @Override
    public List<PedidoMedicamento> buscarPedidoMedicamentoPorProveedor(String ruc, String nombreEmpresa) {
        return pedidoMedicamentoRepository.findByProveedorRucOrNombreEmpresa(ruc, nombreEmpresa);
    }

    @Override
    public List<PedidoMedicamento> buscarPedidoMedicamentoPorCodigoONombreInsumo(String parametro) {
        List<PedidoMedicamento> resultadoBusqueda = new ArrayList<>();
        List<PedidoMedicamento> pedidosMedicamentosPorCodigo = pedidoMedicamentoRepository.findByMedicamentoCodigo(parametro);
        List<PedidoMedicamento> pedidosMedicamentosPorNombreComun = pedidoMedicamentoRepository.findByMedicamentoNombreComun(parametro);
        List<PedidoMedicamento> pedidosMedicamentosPorNombreMarca = pedidoMedicamentoRepository.findByMedicamentoNombreMarca(parametro);
        if (pedidosMedicamentosPorCodigo != null && !pedidosMedicamentosPorCodigo.isEmpty()) {
            resultadoBusqueda.addAll(pedidosMedicamentosPorCodigo);
        } else if (pedidosMedicamentosPorNombreComun != null && !pedidosMedicamentosPorNombreComun.isEmpty()) {
            resultadoBusqueda.addAll(pedidosMedicamentosPorNombreComun);
        } else if (pedidosMedicamentosPorNombreMarca != null && !pedidosMedicamentosPorNombreMarca.isEmpty()) {
            resultadoBusqueda.addAll(pedidosMedicamentosPorNombreMarca);
        }
       return resultadoBusqueda;
    
    }

    @Override
    public List<PedidoMedicamento> buscarPedidoMedicamentoPorEstado(String estado) {
        return pedidoMedicamentoRepository.findByEstado(estado);
    }

    @SuppressWarnings({ "static-access", "unused" })
    @Override
    public PedidoMedicamento antesDeGuardarPedidoMedicamento(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAuditor) {
        Medicamento medicamento = inventarioService.buscarMedicamentoPorCodigo(pedidoTO.getCodigoProducto());
        Proveedor proveedor = proveedorRepository.findByRucOrNombreEmpresa(pedidoTO.getParametro(),
                pedidoTO.getParametro());
        if (medicamento != null) {
            PedidoMedicamento pedidoMedicamento = new PedidoMedicamento().builder()
                    .medicamento(medicamento)
                    .proveedor(proveedor)
                    .cantidad(pedidoTO.getCantidad())
                    .total(medicamento.getPrecioCompra().multiply(new BigDecimal(pedidoTO.getCantidad())))
                    .nombreAuditor(nombreAuditor)
                    .cedulaAuditor(cedulaAuditor)
                    .build();
            return pedidoMedicamento;
        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public PedidoMedicamento guardarNuevoPedidoMedicamento(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAudito) {
        Medicamento medicamento = inventarioService.buscarMedicamentoPorCodigo(pedidoTO.getCodigoProducto());
        Proveedor proveedor = proveedorRepository.findByRucOrNombreEmpresa(pedidoTO.getParametro(),
                pedidoTO.getParametro());
        if (medicamento != null) {
            PedidoMedicamento pedidoMedicamento = new PedidoMedicamento().builder()
                    .medicamento(medicamento)
                    .proveedor(proveedor)
                    .codigo(inventarioService
                            .generarCodigoUnicoProducto(PrefijoPedido.PEDIDO_MEDICAMENTO.getEstado()))
                    .cantidad(pedidoTO.getCantidad())
                    .fechaPedido(LocalDateTime.now())
                    .estado(EstadoPedido.PENDIENTE.getEstado())
                    .total(medicamento.getPrecioCompra().multiply(new BigDecimal(pedidoTO.getCantidad())))
                    .nombreAuditor(nombreAuditor)
                    .cedulaAuditor(cedulaAudito)
                    .build();
            medicamento.getPedidos().add(pedidoMedicamento);
            proveedor.getPedidosMedicamentos().add(pedidoMedicamento);
            medicamentoRepository.save(medicamento);
            proveedorRepository.save(proveedor);
            return pedidoMedicamentoRepository.save(pedidoMedicamento);
        } else {
            return null;
        }
    }

    @Override
    public PedidoMedicamento recibirPedidoMedicamento(String codigo, String nombreAuditor, String cedulaAuditor) {
        PedidoMedicamento pedidoMedicamento = pedidoMedicamentoRepository.findByCodigo(codigo);
        if (pedidoMedicamento != null) {
            pedidoMedicamento.setEstado(EstadoPedido.RECIBIDO.getEstado());
            Medicamento medicamento = pedidoMedicamento.getMedicamento();
            Medicamento medicamentoActualizado = medicamento.toBuilder()
                    .stock(medicamento.getStock() + pedidoMedicamento.getCantidad())
                    .editorAuditor(nombreAuditor)
                    .editorCedulaAuditor(cedulaAuditor)
                    .fechaModificacion(LocalDateTime.now())
                    .build();
            medicamentoRepository.save(medicamentoActualizado);
            return pedidoMedicamentoRepository.save(pedidoMedicamento);
        } else {
            return null;

        }
    }

    @Override
    public PedidoMedicamento rechazarPedidoMedicamento(String codigo, String nombreAuditor, String cedulaAuditor) {
        PedidoMedicamento pedidoMedicamento = pedidoMedicamentoRepository.findByCodigo(codigo);
        if (pedidoMedicamento != null) {
            pedidoMedicamento.setEstado(EstadoPedido.RECIBIDO.getEstado());
            return pedidoMedicamentoRepository.save(pedidoMedicamento);
        } else {
            return null;

        }
    }

    @Override
    public List<PedidoInsumoMedico> obtenerTodosLosPedidosDeInsumos() {
        return pedidoInsumoMeditoRepository.findAll();
    }

    @Override
    public PedidoInsumoMedico buscarPedidoInsumoMedicoPorCodigo(String codigo) {
        return pedidoInsumoMeditoRepository.findByCodigo(codigo);
    }

    // Todos los metodos para Pedidos de Insumos medicos

    @Override
    public List<PedidoInsumoMedico> buscarPedidoInsumoMedicoPorProveedor(String ruc, String nombreEmpresa) {
        return pedidoInsumoMeditoRepository.findByProveedorRucOrNombreEmpresa(ruc, nombreEmpresa);
    }

    @Override
    public List<PedidoInsumoMedico> buscarPedidoInsumoMedicoPorCodigoONombreInsumo(String parametro) {
        List<PedidoInsumoMedico> resultadoBusqueda = new ArrayList<>();
        List<PedidoInsumoMedico> pedidosInsumosPorCodigo = pedidoInsumoMeditoRepository.findByInsumoCodigo(parametro);
        List<PedidoInsumoMedico> pedidosInsumosPorNombre = pedidoInsumoMeditoRepository.findByInsumoNombre(parametro);

        if (pedidosInsumosPorCodigo != null && !pedidosInsumosPorCodigo.isEmpty()) {
            resultadoBusqueda.addAll(pedidosInsumosPorCodigo);
        } else if (pedidosInsumosPorNombre != null && !pedidosInsumosPorNombre.isEmpty()) {
            resultadoBusqueda.addAll(pedidosInsumosPorNombre);
        }
        return resultadoBusqueda;
    }

    @Override
    public List<PedidoInsumoMedico> buscarPedidoInsumoMedicoPorEstado(String estado) {
        return pedidoInsumoMeditoRepository.findByEstado(estado);
    }

    @SuppressWarnings("static-access")
    @Override
    public PedidoInsumoMedico antesDeGuardarPedidoInsumoMedico(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAuditor) {

        InsumoMedico insumoMedico = inventarioService.buscarInsumoMedicoPorCodigo(pedidoTO.getCodigoProducto());
        Proveedor proveedor = proveedorRepository.findByRucOrNombreEmpresa(pedidoTO.getParametro(),
                pedidoTO.getParametro());
        if (insumoMedico != null) {
            PedidoInsumoMedico pedidoInsumoMedico = new PedidoInsumoMedico().builder()
                    .insumo(insumoMedico)
                    .proveedor(proveedor)
                    .cantidad(pedidoTO.getCantidad())
                    .total(insumoMedico.getPrecioCompra().multiply(new BigDecimal(pedidoTO.getCantidad())))
                    .nombreAuditor(nombreAuditor)
                    .cedulaAuditor(cedulaAuditor)
                    .build();
            return pedidoInsumoMedico;
        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public PedidoInsumoMedico guardarNuevoPedidoInsumoMedico(PedidoTO pedidoTO, String nombreAuditor,
            String cedulaAudito) {
        InsumoMedico insumoMedico = inventarioService.buscarInsumoMedicoPorCodigo(pedidoTO.getCodigoProducto());
        Proveedor proveedor = proveedorRepository.findByRucOrNombreEmpresa(pedidoTO.getParametro(),
                pedidoTO.getParametro());
        if (insumoMedico != null) {
            PedidoInsumoMedico pedidoInsumoMedico = new PedidoInsumoMedico().builder()
                    .insumo(insumoMedico)
                    .proveedor(proveedor)
                    .codigo(inventarioService
                            .generarCodigoUnicoProducto(PrefijoPedido.PEDIDO_INSUMO_MEDICO.getEstado()))
                    .cantidad(pedidoTO.getCantidad())
                    .fechaPedido(LocalDateTime.now())
                    .estado(EstadoPedido.PENDIENTE.getEstado())
                    .total(insumoMedico.getPrecioCompra().multiply(new BigDecimal(pedidoTO.getCantidad())))
                    .nombreAuditor(nombreAuditor)
                    .cedulaAuditor(cedulaAudito)
                    .build();

            insumoMedico.getPedidos().add(pedidoInsumoMedico);
            proveedor.getPedidosInsumos().add(pedidoInsumoMedico);
            insumoMedicoRepository.save(insumoMedico);
            proveedorRepository.save(proveedor);

            return pedidoInsumoMeditoRepository.save(pedidoInsumoMedico);
        } else {
            return null;
        }

    }

    @Override
    public PedidoInsumoMedico recibirPedidoInsumoMedico(String codigo, String nombreAuditor,
            String cedulaAuditor) {
        PedidoInsumoMedico pedidoInsumoMedico = pedidoInsumoMeditoRepository.findByCodigo(codigo);
        if (pedidoInsumoMedico != null) {
            pedidoInsumoMedico.setEstado(EstadoPedido.RECIBIDO.getEstado());
            InsumoMedico insumoMedico = pedidoInsumoMedico.getInsumo();
            InsumoMedico insumoMedicoActualizado = insumoMedico.toBuilder()
                    .stock(insumoMedico.getStock() + pedidoInsumoMedico.getCantidad())
                    .editorAuditor(nombreAuditor)
                    .editorCedulaAuditor(cedulaAuditor)
                    .fechaModificacion(LocalDateTime.now())
                    .build();
            insumoMedicoRepository.save(insumoMedicoActualizado);
            return pedidoInsumoMeditoRepository.save(pedidoInsumoMedico);
        } else {
            return null;
        }
    }

    @Override
    public PedidoInsumoMedico rechazarPedidoInsumoMedico(String codigo, String nombreAuditor,
            String cedulaAuditor) {
        PedidoInsumoMedico pedidoInsumoMedico = pedidoInsumoMeditoRepository.findByCodigo(codigo);
        if (pedidoInsumoMedico != null) {
            pedidoInsumoMedico.setEstado(EstadoPedido.CANCELADO.getEstado());
            return pedidoInsumoMeditoRepository.save(pedidoInsumoMedico);
        } else {
            return null;
        }
    }
}
