package pe.gob.bcrp.dto;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+([\\-'\\s]?[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+)*$", message = "Nombre perfil solo contiene letras.")
    @Size(max = 100, message = "perfil no debe superar los 100 caracteres.")
    private String nombrePerfil;

    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
