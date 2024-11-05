package pe.gob.bcrp.dto;

import lombok.*;

import java.util.Set;

@Data
public class UsuarioFormDTO {

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
