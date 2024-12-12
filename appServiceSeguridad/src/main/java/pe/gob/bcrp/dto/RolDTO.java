package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RolDTO {

    @JsonProperty("roleId")
    private Integer  idRol;

    @JsonProperty("systemId")
    private Integer  idSistema;

    @JsonProperty("systemName")
    private String   nombreSistema;

    @JsonProperty("roleName")
    private String   nombreRol;

    @JsonProperty("state")
    private Integer  estado;

    private boolean isDeleted;
   //private Integer ultLin;

}
