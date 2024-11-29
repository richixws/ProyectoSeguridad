package pe.gob.bcrp.dto.moduloDTO;

import lombok.Data;

import java.util.Date;

@Data
public class ModuloFormDTO {

    private Integer idModulo;

    private Integer idSistema;

    private String nombreModulo;

    private Date orderDate;

    private Integer estado;

    private boolean isDeleted;

}
