package com.uce.edu.util.Enum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 * Enumeración para los géneros
 */
public enum Genero {
    MASCULINO("Masculino"),
    FEMENINO("Femenino"),
    OTRO("Otro");

    private final String displayName;

    Genero(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

      public static List<String> getDisplayNames() {
        return Arrays.stream(Genero.values())
                     .map(Genero::getDisplayName)
                     .collect(Collectors.toList());
    }
}
