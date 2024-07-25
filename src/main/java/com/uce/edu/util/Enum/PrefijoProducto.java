package com.uce.edu.util.Enum;
/**
 * Enumeración para los prefijos de un producto
 */
public enum PrefijoProducto {
    MEDICAMENTO("MED"),
    INSUMO_MEDICO("INS"),;

    private String estado;

    // Constructor
    PrefijoProducto(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
