package pe.gob.bcrp.dto;

import lombok.Data;

@Data
public class RolDTO {

    private Integer  idRol;
    private Integer  idSistema;
    private String   nombreSistema;
    private String   nombreRol;
    private Integer  estado;
    private Boolean isDeleted;
   //private Integer ultLin;

}
