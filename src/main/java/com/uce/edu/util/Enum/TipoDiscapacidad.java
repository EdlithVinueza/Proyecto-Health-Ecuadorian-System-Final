package com.uce.edu.util.Enum;
/**
 * Enumeración para los tipos de discapacidad
 */
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoDiscapacidad {
    NINGUNA("Ninguna"),
    FISICA("Física"),
    VISUAL("Visual"),
    AUDITIVA("Auditiva"),
    INTELECTUAL("Intelectual");

    private final String displayName;

    TipoDiscapacidad(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    
    public static List<String> getDisplayNames() {
        return Arrays.stream(TipoDiscapacidad.values())
                     .map(TipoDiscapacidad::getDisplayName)
                     .collect(Collectors.toList());
    }
}