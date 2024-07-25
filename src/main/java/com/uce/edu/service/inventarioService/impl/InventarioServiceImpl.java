package com.uce.edu.service.inventarioService.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.inventarioRepository.IInsumoMedicoRepository;
import com.uce.edu.repository.inventarioRepository.IMedicamentoRepository;
import com.uce.edu.repository.modelo.inventarioModelo.InsumoMedico;
import com.uce.edu.repository.modelo.inventarioModelo.Medicamento;
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.inventarioService.interfaceService.IProveedorService;
import com.uce.edu.service.inventarioService.interfaceService.IInventarioService;
import com.uce.edu.service.to.inventarioTO.InsumoMedicoTO;
import com.uce.edu.service.to.inventarioTO.MedicamentoTO;
import com.uce.edu.util.Enum.PrefijoProducto;

@Service
public class InventarioServiceImpl implements IInventarioService {

    @Autowired
    private IInsumoMedicoRepository insumoMedicoRepository;

    @Autowired
    private IMedicamentoRepository medicamentoRepository;

    @Autowired
    private IProveedorService proveedorService;

    @Override
    public String generarCaracteresAleatorios(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public String generarCodigoUnicoProducto(String prefijo) {
        // Ajustar la longitud fija del código teniendo en cuenta el prefijo y el guion
        int longitudCodigo = 10 - (prefijo.length() + 1);

        // Generar caracteres aleatorios
        String codigoProducto = prefijo + "-" + generarCaracteresAleatorios(longitudCodigo);

        return codigoProducto;
    }

    @Override
    public List<Medicamento> todosLosMedicamentos() {
        return medicamentoRepository.findAll();
    }

    @Override
    public Medicamento buscarMedicamentoPorCodigo(String codigo) {
        return medicamentoRepository.findByCodigo(codigo);
    }

    @Override
    public List<Medicamento> buscarMedicamentoPorNombreComun(String nombre) {
        return medicamentoRepository.findByNombreComun(nombre);
    }

    @Override
    public Medicamento buscarMedicamentoPorNombreMarca(String nombre) {
        return medicamentoRepository.findByNombreMarca(nombre);
    }

    @Override
    public List<Medicamento> buscarMedicamentoPorProveedor(String ruc, String nombreEmpresa) {
        return medicamentoRepository.findByProveedorRucOrNombreEmpresa(ruc, nombreEmpresa);
    }

    @SuppressWarnings("static-access")
    @Override
    public Medicamento guardarNuevoMedicamento(MedicamentoTO medicamentoTO, String nombreAuditor,
            String cedulaAuditor) {

        String nuevoCodigo;
        Medicamento medicamentoExistente;

        do {
            nuevoCodigo = generarCodigoUnicoProducto(PrefijoProducto.MEDICAMENTO.getEstado());
            medicamentoExistente = medicamentoRepository.findByCodigo(nuevoCodigo);
        } while (medicamentoExistente != null);

        // En este punto, nuevoCodigo es único y puedes proceder a usarlo para crear un
        // nuevo medicamento.

        Proveedor proveedor = proveedorService.buscarPorRucONombreEmpresa(medicamentoTO.getParametro(), medicamentoTO.getParametro());

        BigDecimal precioAdicional = medicamentoTO.getPrecioCompra().multiply(new BigDecimal(1.2));
        Medicamento medicamento = new Medicamento().builder()
                .proveedor(proveedor)
                .codigo(nuevoCodigo)
                .nombreComun(medicamentoTO.getNombreComun())
                .nombreMarca(medicamentoTO.getNombreMarca())
                .descripcion(medicamentoTO.getDescripcion())
                .precioCompra(medicamentoTO.getPrecioCompra())
                .precioVenta(medicamentoTO.getPrecioCompra().add(precioAdicional))
                .stock(medicamentoTO.getStock())
                .nombreAuditor(nombreAuditor)
                .cedulaAuditor(cedulaAuditor)
                .build();

        return medicamentoRepository.save(medicamento);
    }

    @Override
    public Medicamento actualizarMedicamento(MedicamentoTO medicamentoTO, String codigo, String nombreAuditor,
            String cedulaAuditor) {
        Medicamento medicamentoExistente = medicamentoRepository.findByCodigo(codigo);
        BigDecimal precioAdicional = medicamentoTO.getPrecioCompra().multiply(new BigDecimal(1.2));
        if (medicamentoExistente != null) {
            Medicamento medicamentoModificado = medicamentoExistente.toBuilder()
                    .nombreComun(medicamentoTO.getNombreComun())
                    .nombreMarca(medicamentoTO.getNombreMarca())
                    .descripcion(medicamentoTO.getDescripcion())
                    .precioCompra(medicamentoTO.getPrecioCompra())
                    .precioVenta(medicamentoTO.getPrecioCompra().add(precioAdicional))
                    .stock(medicamentoTO.getStock())
                    .editorAuditor(nombreAuditor)
                    .editorCedulaAuditor(cedulaAuditor)
                    .fechaModificacion(LocalDateTime.now())
                    .build();
            return medicamentoRepository.save(medicamentoModificado);

        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public MedicamentoTO contruMedicamentoTO(String codigoMedicamento) {
        Medicamento medicamentoExistente = medicamentoRepository.findByCodigo(codigoMedicamento);
        if (medicamentoExistente != null) {
            MedicamentoTO medicamentoTO = new MedicamentoTO().builder()
                    .parametro(medicamentoExistente.getProveedor().getRuc())
                    .nombreComun(medicamentoExistente.getNombreComun())
                    .nombreMarca(medicamentoExistente.getNombreMarca())
                    .descripcion(medicamentoExistente.getDescripcion())
                    .precioCompra(medicamentoExistente.getPrecioCompra())
                    .stock(medicamentoExistente.getStock())
                    .build();
            return medicamentoTO;
        } else {
            return null;
        }
    }

    @Override
    public List<InsumoMedico> todosLosInsumosMedicos() {
        return insumoMedicoRepository.findAll();
    }

    @Override
    public InsumoMedico buscarInsumoMedicoPorCodigo(String codigo) {
        return insumoMedicoRepository.findByCodigo(codigo);
    }

    @Override
    public List<InsumoMedico> buscarInsumoMedicoPorNombre(String nombre) {
        return insumoMedicoRepository.findByNombre(nombre);
    }

    @SuppressWarnings("static-access")
    @Override
    public InsumoMedico guardarNuevoInsumoMedico(InsumoMedicoTO insumoMedicoTO, String nombreAuditor,
            String cedulaAuditor) {

        String nuevoCodigo;
        InsumoMedico insumoMedicoExistente;

        do {
            nuevoCodigo = generarCodigoUnicoProducto(PrefijoProducto.INSUMO_MEDICO.getEstado());
            insumoMedicoExistente = insumoMedicoRepository.findByCodigo(nuevoCodigo);
        } while (insumoMedicoExistente != null);

        Proveedor proveedor = proveedorService.buscarPorRucONombreEmpresa(insumoMedicoTO.getParametro(),insumoMedicoTO.getParametro());
        BigDecimal precioAdicional = insumoMedicoTO.getPrecioCompra().multiply(new BigDecimal(1.2));
        InsumoMedico insumoMedico = new InsumoMedico().builder()
                .proveedor(proveedor)
                .codigo(nuevoCodigo)
                .nombre(insumoMedicoTO.getNombre())
                .descripcion(insumoMedicoTO.getDescripcion())
                .precioCompra(insumoMedicoTO.getPrecioCompra())
                .precioVenta(insumoMedicoTO.getPrecioCompra().add(precioAdicional))
                .stock(insumoMedicoTO.getStock())
                .nombreAuditor(nombreAuditor)
                .cedulaAuditor(cedulaAuditor)
                .build();
        return insumoMedicoRepository.save(insumoMedico);
    }

    @Override
    public InsumoMedico actualizarInsumoMedico(InsumoMedicoTO insumoMedicoTO, String codigo, String nombreAuditor,
            String cedulaAuditor) {
        InsumoMedico insumoMedicoExistente = insumoMedicoRepository.findByCodigo(codigo);
        BigDecimal precioAdicional = insumoMedicoTO.getPrecioCompra().multiply(new BigDecimal(1.2));
        if (insumoMedicoExistente != null) {
            InsumoMedico insumoMedicoModificado = insumoMedicoExistente.toBuilder()
                    .nombre(insumoMedicoTO.getNombre())
                    .descripcion(insumoMedicoTO.getDescripcion())
                    .precioCompra(insumoMedicoTO.getPrecioCompra())
                    .precioVenta(insumoMedicoTO.getPrecioCompra().add(precioAdicional))
                    .stock(insumoMedicoTO.getStock())
                    .editorAuditor(nombreAuditor)
                    .editorCedulaAuditor(cedulaAuditor)
                    .fechaModificacion(LocalDateTime.now())
                    .build();
            return insumoMedicoRepository.save(insumoMedicoModificado);
        } else {
            return null;
        }
    }

    @SuppressWarnings("static-access")
    @Override
    public InsumoMedicoTO contruirInsumoMedicoTO(String codigoInsumoMedico) {
        InsumoMedico insumoMedico = insumoMedicoRepository.findByCodigo(codigoInsumoMedico);
        if (insumoMedico != null) {
            InsumoMedicoTO insumoMedicoTO = new InsumoMedicoTO().builder()
                    .parametro(insumoMedico.getProveedor().getRuc())
                    .nombre(insumoMedico.getNombre())
                    .descripcion(insumoMedico.getDescripcion())
                    .precioCompra(insumoMedico.getPrecioCompra())
                    .stock(insumoMedico.getStock())
                    .build();
            return insumoMedicoTO;
        } else {
            return null;
        }
    }

    @Override
    public List<InsumoMedico> buscarInsumoMedicoPorProveedor(String ruc, String nombreEmpresa) {
        return insumoMedicoRepository.findByProveedorRucOrNombreEmpresa(ruc, nombreEmpresa);
    }

 

  

}
