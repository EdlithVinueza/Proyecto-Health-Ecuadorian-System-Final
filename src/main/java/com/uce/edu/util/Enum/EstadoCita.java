package com.uce.edu.util.Enum;
/**
 * Enumeración para los estados de una cita
 
 */
public enum EstadoCita {
    AGENDADA("Agendada"),
    REAGENDADA("Reagendada"),
    ATENDIDA("Atendida"),
    CANCELADA("cancelada"),
    SIN_PAGAR("Sin pagar"),;

    private String estado;

    // Constructor
    EstadoCita(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }
}
