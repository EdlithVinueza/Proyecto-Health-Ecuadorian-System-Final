package com.uce.edu.util.Enum;
/**
 * Enumeración para los estados de un paciente
 
 */
public enum EstadoPaciente {
    NUEVO("Nuevo"),
    ACTIVO("Activo"),
    VACACIONES("Vacaciones"),
    BAJA_MEDICA("Baja Medica");

    private String estado;

    // Constructor
    EstadoPaciente(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
