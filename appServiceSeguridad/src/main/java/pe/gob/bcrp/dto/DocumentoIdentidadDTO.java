package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DocumentoIdentidadDTO {

    @JsonProperty("documentIdentityId")
    private Integer idDocumentoIdentidad;

    @JsonProperty("documentTypeId")
    private String  tipoDocumentoIdentidad;

    @JsonProperty("groupDocument")
    private Integer grupoDocumento;

    @JsonProperty("length")
    private String  longitud;
}
