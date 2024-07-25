package com.uce.edu.util.Enum;
/**
 * Enumeración para los estados de un usuario
 */
public enum EstadoUsuario {
    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    BLOQUEADO("Bloqueado"),
    NUEVO("Nuevo");

    private String estado;

    // Constructor
    EstadoUsuario(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }

}
