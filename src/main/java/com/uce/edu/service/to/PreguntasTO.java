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
 * PreguntasTO, objeto para traer informacion para las preguntas desde su respectivo formulario
 */
public class PreguntasTO {

    private String preguntaUno;
    private String preguntaDos;
    private String preguntaTres;

}
