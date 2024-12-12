package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class RegistroPerfilDTO {

    @JsonProperty("profileId")
    private Integer idPerfil;

    @JsonProperty("systemId")
    private Integer IdSistema;

    @JsonProperty("roleId")
    private Integer idRol;

    @JsonProperty("identityId")
    private Integer idEntidad;

    @JsonProperty("profileName")
    private String nombrePerfil;

    @JsonProperty("systemName")
    private String nombreSistema;

    @JsonProperty("state")
    private Integer  estado;

    private boolean isDeleted;
}
