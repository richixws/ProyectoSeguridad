package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class PersonaDTO {

    private Integer idPersona;
    private Integer tipoDocumento;
    private String  documentoIdentidad;
    private String  apellidoPaterno;
    private String  apellidoMaterno;
    private String  nombres;
}
