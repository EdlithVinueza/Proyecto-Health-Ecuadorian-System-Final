package com.uce.edu.service.serviciosSistemaGeneral;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uce.edu.repository.interfacesSistemaGeneral.IConsultorioRepository;
import com.uce.edu.repository.modelo.dto.ConsultorioDTO;
import com.uce.edu.service.interfacesSistemaPrincipal.IConsultorioService;

@Service
public class ConsultorioServiceImpl  implements IConsultorioService{

    @Autowired
    private IConsultorioRepository consultorioRepository;

    @Override
    public List<String> buscarCodigoConsultorio() {
        List<ConsultorioDTO> codigos = consultorioRepository.findAllCodigos();
        
        return codigos.stream()
                          .map(ConsultorioDTO::getCodigo)
                          .collect(Collectors.toList()); }

}
