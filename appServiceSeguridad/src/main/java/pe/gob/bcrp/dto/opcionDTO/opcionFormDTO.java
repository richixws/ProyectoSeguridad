package pe.gob.bcrp.dto.opcionDTO;

import lombok.Data;

@Data
public class opcionFormDTO {

    private Integer idOpcion;

    private Integer idModulo;

    private Integer idSistema;

    private String nombreOpcion;

    private String url;

    private Integer estado;

    private boolean isDeleted;
}
