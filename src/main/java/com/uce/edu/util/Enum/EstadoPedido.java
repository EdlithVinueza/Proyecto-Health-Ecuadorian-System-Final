package com.uce.edu.util.Enum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Enumeración para los estados de un pedido
 */
public enum EstadoPedido{
    PENDIENTE("Pendiente"),
    CANCELADO("Cancelado"),
    RECIBIDO("Recibido");

    private String estado;

    // Constructor
    EstadoPedido(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
    // Método para convertir el enum a una lista de String
    public static List<String> convertirEnumALista() {
        return Arrays.stream(EstadoPedido.values())
                     .map(EstadoPedido::getEstado)
                     .collect(Collectors.toList());
    }
}
