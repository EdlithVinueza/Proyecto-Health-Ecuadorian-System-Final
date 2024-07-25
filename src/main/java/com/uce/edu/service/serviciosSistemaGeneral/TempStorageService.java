package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.uce.edu.repository.modelo.OrdenCobro;

@Service
public class TempStorageService {
    private final Map<String, OrdenCobro> storage = new HashMap<>();

    public String storeOrdenCobro(OrdenCobro ordenCobro) {
        String id = UUID.randomUUID().toString();
        storage.put(id, ordenCobro);
        return id;
    }

    public OrdenCobro retrieveOrdenCobro(String id) {
        return storage.remove(id); // Optionally remove after retrieval
    }
}
