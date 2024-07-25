package com.uce.edu.service.serviciosSistemaGeneral;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IOrdenCobroRepository;
import com.uce.edu.repository.modelo.OrdenCobro;
import com.uce.edu.service.interfacesSistemaPrincipal.IOrdenCobroService;
import com.uce.edu.util.Enum.EstadoOrdenCobro;

@Service
public class OrdenCobroServiceImpl implements IOrdenCobroService {

    @Autowired
    private IOrdenCobroRepository ordenCobroRepository;

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
    public String generarCodigoUnicoOrdenCobro() {
        // Obtener el número total de órdenes de cobro
        int numeroOrdenes = (int) ordenCobroRepository.count();

        // Generar prefijo basado en la fecha y hora actual
        String prefijoTiempo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        // Generar caracteres aleatorios
        String caracteresAleatorios = generarCaracteresAleatorios(3);

        // Combinar los elementos para formar el código único
        return prefijoTiempo + "-" + numeroOrdenes + "-" + caracteresAleatorios;
    }

    @Override
    public OrdenCobro buscarOrdenCobroPorCodigo(String ordenCobro) {
        return ordenCobroRepository.findByCodigo(ordenCobro);
    }

    @Override
    public boolean validarCaducidadOrdenCobro(OrdenCobro ordenCobro) {
        LocalDateTime fechaLimite = ordenCobro.getFechaLimitePago();
        LocalDateTime ahora = LocalDateTime.now();
        boolean verdad = false;
        if (fechaLimite.isBefore(ahora)){
            ordenCobro.setEstado(EstadoOrdenCobro.CADUCADA.getEstado());
            ordenCobroRepository.save(ordenCobro);
            verdad  = true;
        }
        return verdad;
        
    }

    @Override
    public boolean esOrdenCobroPagada(OrdenCobro ordenCobro) {
        String canselada = EstadoOrdenCobro.COBRADA.getEstado();
        String estado = ordenCobro.getEstado();
        return canselada.equals(estado);
     }

    @Override
    public OrdenCobro buscarOrdenCobroPorId(Integer id) {
        return ordenCobroRepository.findById(id).orElse(null);}


}
