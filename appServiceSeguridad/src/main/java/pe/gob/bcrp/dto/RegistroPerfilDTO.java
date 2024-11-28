package pe.gob.bcrp.dto;

import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class RegistroPerfilDTO {

    private Integer idPerfil;

    private Integer IdSistema;

    private Integer idRol;

    private Integer idEntidad;

    private String nombrePerfil;

    private String nombreSistema;

    private Integer  estado;

    private Boolean isDeleted;
}
