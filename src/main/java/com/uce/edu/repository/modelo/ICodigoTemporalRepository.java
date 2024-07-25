package com.uce.edu.repository.modelo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICodigoTemporalRepository extends JpaRepository<CodigoTemporal, Integer> {
    
    CodigoTemporal findByDoctor(Doctor doctor);

    CodigoTemporal findByUsuario(Usuario usuario);

    CodigoTemporal findByPaciente(Paciente paciente);

}
