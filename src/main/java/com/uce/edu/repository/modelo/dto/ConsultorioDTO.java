package com.uce.edu.repository.modelo.dto;

public class ConsultorioDTO {
    private String codigo;

    public ConsultorioDTO(String codigo) {
        this.codigo = codigo;
    }

    // Getters and setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

}
