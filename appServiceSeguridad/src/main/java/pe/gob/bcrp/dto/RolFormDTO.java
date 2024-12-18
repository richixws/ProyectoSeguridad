package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class RolFormDTO {


    @JsonProperty("roleId")
    @Schema(hidden = true)
    private Integer idRol;

    // private Sistema sistema;
    @JsonProperty("systemId")
    @NotNull(message = "sistema no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private Integer  idSistema;

    @JsonProperty("roleName")
    @NotEmpty(message = "rol no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zA-ZñÑ ]+$", message = "nombre rol solo puede contener letras.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "rol no debe superar los 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private String  nombreRol;

    @JsonProperty("state")
    @Min(value = 0, message = "estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Max(value = 1, message = "estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "estado de la entidad es obligatorio.",groups = ValidationGroups.OnUpdate.class)
    @JsonView({Views.Update.class})
    private Integer estado;

}
