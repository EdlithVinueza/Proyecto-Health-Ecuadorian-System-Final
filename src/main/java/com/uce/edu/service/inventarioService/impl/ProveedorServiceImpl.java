package com.uce.edu.service.inventarioService.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.inventarioRepository.IProveedorRepository;
import com.uce.edu.repository.modelo.inventarioModelo.Proveedor;
import com.uce.edu.service.inventarioService.interfaceService.IProveedorService;
import com.uce.edu.service.to.inventarioTO.ProveedorTO;

@Service
public class ProveedorServiceImpl implements IProveedorService {

    @Autowired
    private IProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> listarProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor buscarProveedorPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    @Override
    public Proveedor buscarPorNombreEmpresa(String nombreEmpresa) {
        return proveedorRepository.findByNombreEmpresa(nombreEmpresa);
    }

    @Override
    public Proveedor buscarPorRucONombreEmpresa(String ruc, String nombreEmpresa) {
        return proveedorRepository.findByRucOrNombreEmpresa(ruc, nombreEmpresa);
    }

    @Override
    public Proveedor guardaNuevoProveedor(ProveedorTO proveedorTO, String nombreAuditor, String cedulaAuditor) {

        Proveedor proveedorExistente = proveedorRepository.findByRuc(proveedorTO.getRuc());
        if (proveedorExistente == null) {
            Proveedor proveedor = Proveedor.builder()
                    .ruc(proveedorTO.getRuc())
                    .nombreEmpresa(proveedorTO.getNombre())
                    .direccion(proveedorTO.getDireccion())
                    .telefono(proveedorTO.getTelefono())
                    .correo(proveedorTO.getCorreo())
                    .registroNombreAuditor(nombreAuditor)
                    .registroCedulaAuditor(cedulaAuditor)
                    .build();
            return proveedorRepository.save(proveedor);
        } else {
            return null;
        }
    }

    @Override
    public ProveedorTO convertirProveedorTO(String ruc) {
        Proveedor proveedor = proveedorRepository.findByRuc(ruc);
        ProveedorTO proveedorTO = ProveedorTO.builder()
                .ruc(proveedor.getRuc())
                .nombre(proveedor.getNombreEmpresa())
                .direccion(proveedor.getDireccion())
                .telefono(proveedor.getTelefono())
                .correo(proveedor.getCorreo())
                .build();
        return proveedorTO;
    }

    @Override
    public Proveedor actualizaProveedor(ProveedorTO proveedorTO, String nombreAuditor, String cedulaAuditor) {
        Proveedor proveedorExistente = proveedorRepository.findByRuc(proveedorTO.getRuc());
        // Verificar si el proveedor existe
        if (proveedorExistente != null) {
            // Reconstruir el objeto Proveedor con nuevas propiedades usando el patrón
            // Builder
            Proveedor proveedorModificado = proveedorExistente.toBuilder()
                    .direccion(proveedorTO.getDireccion())
                    .telefono(proveedorTO.getTelefono())
                    .correo(proveedorTO.getCorreo())
                    .actualizacionAuditor(nombreAuditor)
                    .actualizacionCedulaAuditor(cedulaAuditor)
                    .fechaActualizacion(LocalDateTime.now())
                    .build();
            return proveedorRepository.save(proveedorModificado);
        } else {
            return null;
        }
    }

}
