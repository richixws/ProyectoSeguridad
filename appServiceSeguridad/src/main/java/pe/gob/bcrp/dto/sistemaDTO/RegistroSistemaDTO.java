package pe.gob.bcrp.dto.sistemaDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Column;
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
public class RegistroSistemaDTO {

    @JsonView(Views.Update.class)
    private Integer idSystem;
    //@NotEmpty(message = "codigo no puede ser vacio")
    //@Column(unique = true, nullable = false)
    //private String codigo;
    //@JsonProperty("name")
    @NotEmpty(message = "nombre no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "nombre no puede tener más de 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zA-ZñÑ0-9_]+(\\s[a-zA-ZñÑ0-9_]+)*$", message = "nombre con formato incorrecto, corrija.", groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String name;

    @NotEmpty(message = "version no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "version con formato incorrecto.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "version no puede tener más de 50 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    public String version;


    @NotNull(message = "usuario responsable no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    //@Min(value = 1, message = "usuario responsable debe ser mayor o igual a 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    //@Max(value = 1, message = "usuario responsable no debe ser mayor o igual a 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private  Integer idUserResponsible;

    @NotNull(message = "usuario responsable alterno no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    //@Min(value = 1, message = "usuario responsable alterno debe ser mayor o igual a 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    //@Max(value = 1, message = "usuario Responsable alterno  no debe ser mayor o igual a 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private  Integer idUserResponsibleAlternate;

    @NotEmpty(message = "url no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 150, message = "url no puede tener más de 150 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String url;

    @NotEmpty(message = "url externo no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 150, message = "url externo no puede tener más de 150 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String urlExternal;

    @NotNull(message = "estado critico no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Min(value = 1, message = "estado crítico debe ser al menos 1.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Max(value = 4, message = "estado critico no puede ser mayor 4.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private Integer idStateCritical;

    @NotEmpty(message = "unidad organizacional no puede ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zA-ZñÑ]+(\\s[a-zA-ZñÑ]+)*$", message = "unidad organizacional con formato incorrecto, corrija.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "unidad organizacional no puede tener más de 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String unitOrganizational;


    @Min(value = 0, message = "estado sólo admite el número 0 o 1.",groups = ValidationGroups.OnUpdate.class)
    @Max(value = 1, message = "estado sólo admite el número 0 o 1.",groups = ValidationGroups.OnUpdate.class)
    @NotNull(message = "estado es obligatorio.", groups = ValidationGroups.OnUpdate.class)
    @JsonView({Views.Update.class})
   // @Builder.Default
    private Integer estate;
}
