package pe.gob.bcrp.dto;

import lombok.Data;
import pe.gob.bcrp.entities.Modulo;

@Data
public class OpcionDTO {

    private Integer idOpcion;
    //private Modulo modulo;
    private Integer idModulo;

    private Integer idSistema;

    private String nombreOpcion;

    private String url;
}
