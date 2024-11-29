package pe.gob.bcrp.dto.personaDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonaFormDTO {

    private Integer idPersona;

    private Integer tipoDocumento;

    private String  numeroDocumento;

    private String  apellidoPaterno;

    private String  apellidoMaterno;

    private String  nombres;

    private String correo;

    private Integer estado;

    private boolean isDeleted;

}
