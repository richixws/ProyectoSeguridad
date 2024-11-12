package pe.gob.bcrp.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PerfilDTO {


    //@NotEmpty(message = "perfil no debe ser vacio")
    private Integer idPerfil;

    @NotNull(message = "Sistema no debe ser vacio")
    private Integer IdSistema;

    @NotNull(message = "Rol no debe ser vacio")
    private Integer idRol;

    @NotNull(message = "Entidad no debe ser vacio")
    private Integer idEntidad;

    @NotEmpty(message = "Nombre del perfil no debe ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+([\\-'\\s]?[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+)*$",
            message = "Formato no válido para el nombre del perfil")
    private String nombrePerfil;



}
