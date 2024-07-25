package com.uce.edu.service.serviciosSistemaGeneral;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IFacturaCitaRepository;
import com.uce.edu.service.interfacesSistemaPrincipal.IFacturaCitaService;

@Service
public class FacturaCitaServiceImpl implements IFacturaCitaService {

    @Autowired
    private IFacturaCitaRepository facturaRepository;

    @Override
    public String generarNumeroAutorizacion() {
        // Tomar los primeros 8 caracteres del UUID
        String uuidPart = UUID.randomUUID().toString().substring(0, 8);
        // Obtener la fecha y hora actual en formato compacto
        String dateTimePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        // Concatenar ambas partes con un guion
        String codigo = uuidPart + "-" + dateTimePart;
        // Asegurarse de que el código no exceda los 30 caracteres
        return codigo.length() > 30 ? codigo.substring(0, 30) : codigo;
    }

    @Override
    public long generarNumeroFactura() {
        return facturaRepository.count() + 1;
    }

    @Override
    public String generarNumeroParaFactura() {
         Random random = new Random();
        
        // Generar dos números aleatorios para los primeros dos grupos de tres dígitos
        int parte1 = random.nextInt(999) + 1; // +1 para asegurar que el número no sea 0
        int parte2 = random.nextInt(999) + 1;
        
        // Obtener el contador de facturas de la base de datos
        long contadorFacturas = contarFacturas(); // Este método debe ser implementado para acceder a la base de datos
        
        // Asegurarse de que el contador de facturas no exceda 9 dígitos
        String contadorFormateado = String.format("%09d", contadorFacturas % 1000000000);
        
        // Formatear los números para que tengan tres dígitos, rellenando con ceros a la izquierda
        String grupo1 = String.format("%03d", parte1);
        String grupo2 = String.format("%03d", parte2);
        
        // Concatenar los grupos con guiones
        String numeroFinal = grupo1 + "-" + grupo2 + "-" + contadorFormateado;
        
        return numeroFinal;
    }

    @Override
    public long contarFacturas() {
        return facturaRepository.count();
    }

  

}
