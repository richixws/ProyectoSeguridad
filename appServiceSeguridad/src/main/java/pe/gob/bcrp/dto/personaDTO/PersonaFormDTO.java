package pe.gob.bcrp.dto.personaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonaFormDTO {

    @JsonProperty("personId")
    private Integer idPersona;

    @JsonProperty("documentType")
    private Integer tipoDocumento;

    @JsonProperty("documentNumber")
    private String  numeroDocumento;

    @JsonProperty("fatherSurname")
    private String  apellidoPaterno;

    @JsonProperty("motherSurname")
    private String  apellidoMaterno;

    @JsonProperty("names")
    private String  nombres;

    @JsonProperty("email")
    private String correo;

    @JsonProperty("state")
    private Integer estado;

    private boolean isDeleted;

}
