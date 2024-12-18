package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.validacion.ValidationGroups;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilDTO {


    //@NotEmpty(message = "perfil no debe ser vacio")
    @JsonProperty("profileId")
    @Schema(hidden = true)
    private Integer idPerfil;

    @JsonProperty("systemId")
    @NotNull(message = "Sistema no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private Integer IdSistema;

    @JsonProperty("roleId")
    @NotNull(message = "Rol no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private Integer idRol;

    @JsonProperty("entityId")
    @NotNull(message = "Entidad no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private Integer idEntidad;

    @JsonProperty("profileName")
    @NotEmpty(message = "Nombre del perfil no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+([\\-'\\s]?[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+)*$", message = "Nombre perfil solo contiene letras.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "perfil no debe superar los 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private String nombrePerfil;

    @JsonProperty("state")
    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnUpdate.class})
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnUpdate.class})
    @NotNull(message = "estado de la entidad es obligatorio.",groups = ValidationGroups.OnUpdate.class)
    @JsonView({Views.Update.class})
    private Integer estado;
}
