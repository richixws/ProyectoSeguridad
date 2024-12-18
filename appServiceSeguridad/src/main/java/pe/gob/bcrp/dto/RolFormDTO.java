package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
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
    private Integer idRol;

    // private Sistema sistema;
    @JsonProperty("systemId")
    @NotNull(message = "Sistema no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private Integer  idSistema;

    @JsonProperty("roleName")
    @NotEmpty(message = "Rol no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zA-ZñÑ ]+$", message = "Nombre rol solo puede contener letras.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "rol no debe superar los 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String  nombreRol;

    @JsonProperty("state")
    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "estado de la entidad es obligatorio.",groups = ValidationGroups.OnUpdate.class)
    @JsonView({Views.Update.class})
    private Integer estado;

}
