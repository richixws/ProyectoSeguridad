package pe.gob.bcrp.dto.usuarioDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UsuarioFormDTO implements Serializable {


    @JsonProperty("userId")
    private Integer idUsuario;

    @JsonProperty("documentId")
    private Integer idDocumento;

    @JsonProperty("documentNumber")
    private String  numeroDocumento;

    @JsonProperty("names")
    private String  nombres;

    @JsonProperty("scope")
    private String  ambito;
 //   private String  idSistema;

    @JsonProperty("documentType")
    private String  tipoDocumento;

    @JsonProperty("state")
    private String  estado;

    @JsonProperty("fatherSurname")
    private String  apellidoPaterno;

    @JsonProperty("motherSurname")
    private String  apellidoMaterno;

    @JsonProperty("email")
    private String  correoElectronico;
}
