package pe.gob.bcrp.dto.moduloDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ModuloFormDTO {

    @JsonProperty("moduleId")
    private Integer idModulo;

    @JsonProperty("systemId")
    private Integer idSistema;

    @JsonProperty("moduleName")
    private String nombreModulo;

    private Date orderDate;

    @JsonProperty("state")
    private Integer estado;

    private boolean isDeleted;

}
