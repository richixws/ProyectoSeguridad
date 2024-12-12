package pe.gob.bcrp.dto.entidadDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EntidadFormDTO {
    @JsonProperty("entityId")
    private Integer idEntidad;
    @JsonProperty("documentId")
    private Integer idDocumento;
    @JsonProperty("documentType")
    private String  tipoDocumento;
    @JsonProperty("documentNumber")
    private String  numeroDocumento;
    @JsonProperty("name")
    private String  nombre;
    @JsonProperty("initials")
    private String  sigla;
    @JsonProperty("externalCode")
    private String  codExterno;
    @JsonProperty("state")
    private Integer  estado;
    private boolean isDeleted;
}
