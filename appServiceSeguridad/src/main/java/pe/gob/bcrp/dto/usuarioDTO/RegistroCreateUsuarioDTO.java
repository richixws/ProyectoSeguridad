package pe.gob.bcrp.dto.usuarioDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.Views;
import pe.gob.bcrp.dto.personaDTO.ValidateDni;
import pe.gob.bcrp.dto.personaDTO.ValidatePasaporte;
import pe.gob.bcrp.dto.validacion.ValidationGroups;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroCreateUsuarioDTO {

    @NotNull(message = "tipo de documento no debe ser nulo.",groups = {ValidationGroups.OnCreate.class})
    @Min(value = 1, message = "tipo de documento no puede ser menor que 1.",groups = {ValidationGroups.OnCreate.class})
    private Integer documentType;

    @NotBlank(message = "número de documento no debe estar vacío.",groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "número de documento sólo admite números.",groups = {ValidationGroups.OnCreate.class})
    @Size(min = 8, max = 8, message = "Número de documento dni debe tener 8 caracteres.",groups = {ValidateDni.class})
    @Size(min = 12, max = 12, message = "Número de documento pasaporte debe tener 12 caracteres.",groups = {ValidatePasaporte.class})
    private String  documentNumber;

    @NotBlank(message = "nombre no debe ser vacio",groups = {ValidationGroups.OnCreate.class})
    @NotNull(message = "nombre no debe ser nulo.",groups = {ValidationGroups.OnCreate.class})
    @Size(max = 50, message = "nombre debe tener max 50 caracteres.",groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Nombre formato no válido.",groups = {ValidationGroups.OnCreate.class})
    private String  names;

    @NotBlank(message = "apellido paterno no debe ser vacio",groups = {ValidationGroups.OnCreate.class})
    @NotNull(message = "apellido paterno no debe ser nulo.",groups = {ValidationGroups.OnCreate.class})
    @Size(max = 50, message = "apellido paterno debe tener max 50 caracteres.",groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "apellido paterno formato no válido.",groups = {ValidationGroups.OnCreate.class})
    private String  fatherSurname;

    @NotBlank(message = "apellido materno no debe ser vacio",groups = {ValidationGroups.OnCreate.class})
    @NotNull(message = "apellido materno no debe ser nulo.",groups = {ValidationGroups.OnCreate.class})
    @Size(max = 50, message = "apellido materno debe tener max 50 caracteres.",groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "apellido materno formato no válido.",groups = {ValidationGroups.OnCreate.class})
    private String  motherSurname;

    @NotBlank(message = "correo no debe ser vacio.",groups = {ValidationGroups.OnCreate.class})
    @NotNull(message = "correo no debe ser nulo.",groups = {ValidationGroups.OnCreate.class})
    @Size(max = 100, message = "correo debe tener max 100 caracteres.",groups = {ValidationGroups.OnCreate.class})
    @Pattern(regexp = ".+@.+\\..+", message = "correo formato no válido.",groups = {ValidationGroups.OnCreate.class})
    @Email(message = "correo formato no válido.",groups = {ValidationGroups.OnCreate.class})
    private String  email;

    @NotBlank(message = "ambito no debe ser vacio.",groups = {ValidationGroups.OnCreate.class})
    @NotNull(message = "ambito no debe ser nulo.",groups = {ValidationGroups.OnCreate.class})
    @Size(max =50, message = "ambito debe tener max 50 caracteres.",groups = {ValidationGroups.OnCreate.class})
    private String  scope;

    @Min(value = 0, message = "estado sólo admite el número 0 o 1.",groups = {ValidationGroups.OnUpdate.class})
    @Max(value = 1, message = "estado sólo admite el número 0 o 1.",groups = ValidationGroups.OnUpdate.class)
    @JsonView({Views.Update.class})
    //@Builder.Default
    private Integer state;

    // @NotBlank(message = "Ambito no debe ser vacio.")
    // @NotNull(message = "Ambito no debe ser nulo.")
    // @Nullable
   //  private String  sustento;
}
