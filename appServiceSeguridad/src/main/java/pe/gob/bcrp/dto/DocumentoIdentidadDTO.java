package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class DocumentoIdentidadDTO {

    private Integer idDocumentoIdentidad;
    private String tipoDocumentoIdentidad;
   // private Integer grupoDocumento;
}
