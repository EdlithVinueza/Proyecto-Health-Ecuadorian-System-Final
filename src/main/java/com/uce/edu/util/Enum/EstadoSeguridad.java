package com.uce.edu.util.Enum;
/**
 * Enumeración para los estados de la seguridad
 */
public enum EstadoSeguridad {

    ACTIVIDAD_1("INACTIVO"),
    ACTIVDAD_2("INACTIVO"),
    ACTIVDAD_3("INACTIVO"),
    ACTIVIDAD_4("Desprotegido"),
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");

    private String estado;

    // Constructor
    EstadoSeguridad(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }

}
