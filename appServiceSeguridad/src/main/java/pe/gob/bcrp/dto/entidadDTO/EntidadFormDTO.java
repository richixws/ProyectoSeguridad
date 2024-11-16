package pe.gob.bcrp.dto.entidadDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EntidadFormDTO {

    private Integer idEntidad;
    private Integer idDocumento;
    private String  tipoDocumento;
    private String  numeroDocumento;
    private String  nombre;
    private String  sigla;
    private String  codExterno;
}
