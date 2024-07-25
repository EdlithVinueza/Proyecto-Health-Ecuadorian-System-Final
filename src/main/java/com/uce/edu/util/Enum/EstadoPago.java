package com.uce.edu.util.Enum;
/**
 * Enumeración para los estados de un pago
 */
public enum EstadoPago{
    PAGADO("Pagado"),
    PENDIENTE("Pendiente");

    private String estado;

    // Constructor
    EstadoPago(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
