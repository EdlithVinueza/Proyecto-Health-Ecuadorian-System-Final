package com.uce.edu.service.to;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
/**
 * AgendarCitaTO, objeto para traer informacion para agendar una cita desde su respectivo formulario
 
 */
public class AgendarCitaTO {

    private String cedulaPaciente;
    private String cedulaAuditor;

   

}
