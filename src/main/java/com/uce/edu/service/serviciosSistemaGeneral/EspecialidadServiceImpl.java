package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IEspecialiadRepository;
import com.uce.edu.repository.modelo.Especialidad;
import com.uce.edu.repository.modelo.dto.EspecialidadNombreDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IEspecialidadService;

@Service
public class EspecialidadServiceImpl implements IEspecialidadService {

    @Autowired
    private IEspecialiadRepository especialidadRepository;

    @Override
    public Especialidad buscarPorEspecialidadPorNombre(String nombre) {
        return especialidadRepository.findByNombre(nombre);
    }

    @Override
    public List<String> buscarNombresEspecialidades() {

        List<EspecialidadNombreDTO> especialidadesDTO = especialidadRepository.findAllDto();
        return especialidadesDTO.stream()
                             .map(EspecialidadNombreDTO::getNombre)
                             .collect(Collectors.toList());}

}
