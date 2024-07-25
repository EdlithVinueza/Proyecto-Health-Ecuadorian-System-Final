package com.uce.edu.util.Enum;
/**
 * Enumeración para los estados de una orden de cobro
 
 */
public enum EstadoOrdenCobro {
    COBRADA("Cobrada"),
    POR_COBRAR("Por cobrar"),
    CADUCADA("Caducada")
    ;

    private String estado;

    // Constructor
    EstadoOrdenCobro(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
