package pe.gob.bcrp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class RegistroUsuarioDTO {

    private Integer tipoDocumento;
    private String  numeroDocumento;
    private String  nombres;
    private String  apPaterno;
    private String  apMaterno;
    private String  correoElectronico;
    private String  ambito;
    private String  sustento;
}
