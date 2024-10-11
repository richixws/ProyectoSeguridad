package pe.gob.bcrp.dto;

import lombok.Data;

@Data
public class EntidadDTO {


    private Integer idEntidad;
    private String ruc;
    private String nombre;
    private String sigla;
    private String codExterno;
}
