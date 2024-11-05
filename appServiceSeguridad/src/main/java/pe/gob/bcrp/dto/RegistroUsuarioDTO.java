package pe.gob.bcrp.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class RegistroUsuarioDTO {

    private Integer tipoDocumento;
    private String documentoIdentidad;
    private String nombres;
    private String apPat;
    private String apMat;
    private String correo;
    private String ambito;
    private String sustento;
}
