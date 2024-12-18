package pe.gob.bcrp.dto.opcionDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.Views;
import pe.gob.bcrp.dto.validacion.ValidationGroups;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpcionDTO {

    //@NotEmpty(message = "Opcion no debe ser vacio")
    @JsonProperty("optionId")
    private Integer idOpcion;

    @JsonProperty("moduleId")
    @NotNull(message = "Modulo no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private Integer idModulo;

    @JsonProperty("systemId")
    @NotNull(message = "Sistema no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private Integer idSistema;

    @JsonProperty("optionName")
    @NotEmpty(message = "Nombre de Opcion no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ](\\s?[a-zA-ZñÑáéíóúÁÉÍÓÚ])*$", message = "Nombre de opción con formato incorrecto",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "nombre opcion no debe superar los 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String nombreOpcion;

    @NotEmpty(message = "Link de Opcion no debe ser vacio",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 200, message = "url no debe superar los 200 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String url;

    @JsonProperty("state")
    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "estado de la entidad es obligatorio.",groups = ValidationGroups.OnUpdate.class)
    @JsonView({Views.Update.class})
    private Integer estado;
}
