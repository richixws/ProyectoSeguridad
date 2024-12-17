package pe.gob.bcrp.dto.personaDTO;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDTO {


    @JsonProperty("personId")
    @Schema(hidden = true)
    private Integer idPersona;

    @JsonProperty("documentType")
    @NotNull(message = "tipo de documento no debe ser nulo.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private Integer tipoDocumento;

    @JsonProperty("documentNumber")
    @NotBlank(message = "número de documento no debe estar vacío.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "número de documento sólo admite números.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(min = 8, max = 8, message = "Número de documento dni debe tener 8 caracteres.",groups = {ValidateDni.class})
    @Size(min = 12, max = 12, message = "Número de documento pasaporte debe tener 12 caracteres.",groups = {ValidatePasaporte.class})
    private String  numeroDocumento;

    @JsonProperty("fatherSurname")
    @NotBlank(message = "apellido paterno no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "apellido paterno no debe ser nulo.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "apellido paterno formato no válido.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "apellido paterno no debe superar los 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String  apellidoPaterno;

    @JsonProperty("motherSurname")
    @NotBlank(message = "apellido materno no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "apellido materno no debe ser nulo.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "apellido materno formato no válido.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "apellido materno no debe superar los 100 caracteres.")
    private String  apellidoMaterno;

    @JsonProperty("names")
    @NotBlank(message = "nombre no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "nombre no debe ser nulo.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "nombre formato no válido.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "nombres no debe superar los 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String  nombres;

    @JsonProperty("email")
    @NotBlank(message = "correo no debe ser vacio.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @NotNull(message = "correo no debe ser nulo.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Size(max = 100, message = "correo debe tener max 100 caracteres.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Pattern(regexp = ".+@.+\\..+", message = "correo formato no válido.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    @Email(message = "correo formato no válido.",groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class})
    private String correo;

    @JsonProperty("state")
    @Min(value = 0, message = "estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnUpdate.class})
    @Max(value = 1, message = "estado sólo admite el número 0 o 1.", groups = {ValidationGroups.OnUpdate.class})
    @NotNull(message = "estado es obligatorio.", groups = {ValidationGroups.OnUpdate.class})
    @JsonView({Views.Update.class})
   // @Builder.Default
    private Integer estado;
}
