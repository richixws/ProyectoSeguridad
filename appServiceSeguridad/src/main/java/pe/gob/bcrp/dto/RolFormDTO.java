package pe.gob.bcrp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolFormDTO {


    private Integer idRol;
    // private Sistema sistema;
    private Integer  idSistema;
    private String  nombreRol;
    private Integer estado;

}
