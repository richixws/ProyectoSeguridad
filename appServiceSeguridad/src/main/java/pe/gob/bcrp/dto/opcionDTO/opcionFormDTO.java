package pe.gob.bcrp.dto.opcionDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class opcionFormDTO {

    @JsonProperty("optionId")
    private Integer idOpcion;

    @JsonProperty("moduleId")
    private Integer idModulo;

    @JsonProperty("systemId")
    private Integer idSistema;

    @JsonProperty("optionName")
    private String nombreOpcion;

    private String url;

    @JsonProperty("state")
    private Integer estado;

    private boolean isDeleted;
}
