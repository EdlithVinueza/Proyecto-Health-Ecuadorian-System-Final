package com.uce.edu.util.Enum;
/**
 * Enumeración para las redirecciones de login
 
 */
public enum RedireccionLogin {
    INGRESAR_DIRECTAMENTE("ingresar"),
    CAMBIAR_CONTRASENIA("cambiarContrasenia"),
    RECUPERAR_CONTRASENIA("recuperarContrasenia"),
    VALIDAR_SEGURIDAD("validar Seguridad"),
    REFORZAR_SEGURIDAD("reforzar Seguridad"),
    CAMBIAR_PREGUNTAS("cambiar Preguntas"),
    VALIDAR_SEGURIDAD_PRIMERA_VEZ("validar Seguridad PRIMERA VEZ");

    private String estado;

    // Constructor
    RedireccionLogin(String estado) {
        this.estado = estado;
    }

    // Método para obtener el valor del enum como String
    public String getEstado() {
        return this.estado;
    }


}
