package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SistemaDTO {


    private Integer idSistema;
    private String codigo;
    private String nombre;
    public String version;
    private String logoMain;
    private String logoHead;
    private String url;
}
