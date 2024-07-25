package com.uce.edu.util.Enum;
/**
 * Enumeración para los tipos de productos
 */
public enum TipoProducto{
    INSUMO_MEDICO("Insumo Medico"),
    MEDICAMENTO("Medicamento");

    private String estado;

    // Constructor
    TipoProducto(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
