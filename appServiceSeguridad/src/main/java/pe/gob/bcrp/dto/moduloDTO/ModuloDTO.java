package pe.gob.bcrp.dto.moduloDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.Views;
import pe.gob.bcrp.dto.validacion.ValidationGroups;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("moduleId")
    @Schema(hidden = true)
    private Integer idModulo;

    @JsonProperty("systemId")
    @NotNull(message = "sistema es obligatorio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class, Views.Create.class})
    private Integer idSistema;

    @JsonProperty("moduleName")
    @NotEmpty(message = "nombre de modulo no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "nombre modulo tiene campo incorrecto.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class,Views.Create.class})
    private String nombreModulo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Schema(hidden = true)
    private Date orderDate;

    @JsonProperty("state")
    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.",groups = ValidationGroups.OnUpdate.class)
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.",groups = ValidationGroups.OnUpdate.class)
    @NotNull(message = "estado del modulo es obligatorio.",groups = ValidationGroups.OnUpdate.class)
    //@Schema(hidden = true)
    @JsonView({Views.Update.class})
    //@Builder.Default
    private Integer estado;
}
