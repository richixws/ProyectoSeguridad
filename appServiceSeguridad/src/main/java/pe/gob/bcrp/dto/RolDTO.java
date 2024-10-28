package pe.gob.bcrp.dto;

import jakarta.persistence.*;
import lombok.Data;
import pe.gob.bcrp.entities.Sistema;

@Data
public class RolDTO {

    private Integer idRol;
   // private Sistema sistema;
    private Integer  idSistema;
    private String  nombre;
    private Integer estado;
    private Integer ultLin;

}
