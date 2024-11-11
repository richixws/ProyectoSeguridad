package pe.gob.bcrp.dto.usuarioDTO;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UsuarioFormDTO implements Serializable {

    private Integer idUsuario;
    private Integer idDocumento;
    private String  numeroDocumento;
    private String  nombres;
    private String  ambito;
 //   private String  idSistema;
    private String  tipoDocumento;
    private String  estado;
    private String  apellidoPaterno;
    private String  apellidoMaterno;
    private String  correoElectronico;
}
