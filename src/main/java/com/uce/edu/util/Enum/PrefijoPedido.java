package com.uce.edu.util.Enum;
/**
 * Enumeración para los prefijos de un pedido
 */
public enum PrefijoPedido {
    PEDIDO_MEDICAMENTO("PME"),
    PEDIDO_INSUMO_MEDICO("PIS"),;

    private String estado;

    // Constructor
    PrefijoPedido(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
