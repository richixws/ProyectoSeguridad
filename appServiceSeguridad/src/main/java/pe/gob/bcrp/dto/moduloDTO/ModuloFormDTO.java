package pe.gob.bcrp.dto.moduloDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;

    @JsonProperty("state")
    private Integer estado;

    @JsonProperty("deleted")
    private boolean isDeleted;

}
