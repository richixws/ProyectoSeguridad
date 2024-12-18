package pe.gob.bcrp.dto.usuarioDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.personaDTO.ValidateDni;
import pe.gob.bcrp.dto.personaDTO.ValidatePasaporte;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {

    @NotNull(message = "tipo de documento no debe ser nulo.")
    @Min(value = 1, message = "tipo de documento no puede ser menor que 1.")
    private Integer documentType;

    @NotBlank(message = "número de documento no debe estar vacío.")
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "número de documento sólo admite números.")
    @Size(min = 8, max = 8, message = "Número de documento dni debe tener 8 caracteres.",groups = {ValidateDni.class})
    @Size(min = 12, max = 12, message = "Número de documento pasaporte debe tener 12 caracteres.",groups = {ValidatePasaporte.class})
    private String  documentNumber;

    @NotBlank(message = "nombre no debe ser vacio.")
    @NotNull(message = "nombre no debe ser nulo.")
    @Size(max = 50, message = "nombre debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "nombre formato no válido.")
    private String  names;

    @NotBlank(message = "apellido paterno no debe ser vacio.")
    @NotNull(message = "apellido paterno no debe ser nulo.")
    @Size(max = 50, message = "apellido paterno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "apellido paterno formato no válido.")
    private String  fatherSurname;

    @NotBlank(message = "apellido materno no debe ser vacio")
    @NotNull(message = "apellido materno no debe ser nulo.")
    @Size(max = 50, message = "apellido materno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "apellido materno formato no válido.")
    private String  motherSurname;

    @NotBlank(message = "correo no debe ser vacio.")
    @NotNull(message = "correo no debe ser nulo.")
    @Size(max = 100, message = "correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "correo formato no válido.")
    @Email(message = "correo formato no válido.")
    private String  email;

    @NotBlank(message = "ambito no debe ser vacio.")
    @NotNull(message = "ambito no debe ser nulo.")
    @Size(max =50, message = "Ambito debe tener max 50 caracteres.")
    private String  scope;

    @Min(value = 0, message = "estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer state = 1;


    // private String  sustento;
}
