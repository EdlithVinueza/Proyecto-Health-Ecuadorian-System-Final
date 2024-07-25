package com.uce.edu.util.Enum;
/**
 * Enumeración para las respuestas cortas
 
 */
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum RespuestaCorta {
    SI("Sí"),
    NO("No");

    private final String displayName;

    RespuestaCorta(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static List<String> getDisplayNames() {
        return Arrays.stream(RespuestaCorta.values())
                     .map(RespuestaCorta::getDisplayName)
                     .collect(Collectors.toList());
    }
}
