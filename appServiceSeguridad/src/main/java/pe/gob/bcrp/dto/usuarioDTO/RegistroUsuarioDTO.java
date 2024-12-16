package pe.gob.bcrp.dto.usuarioDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroUsuarioDTO {

    @NotNull(message = "Tipo de documento no debe ser nulo.")
    @Min(value = 1, message = "Tipo de documento no puede ser menor que 1.")
    private Integer documentType;

    @NotBlank(message = "Número de documento no debe estar vacío.")
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "Número de documento sólo admite números.")
    @Size(min = 8, max = 8, message = "Número de documento debe tener 8 caracteres.")
    private String  documentNumber;

    @NotBlank(message = "Nombre no debe ser vacio")
    @NotNull(message = "Nombre no debe ser nulo.")
    @Size(max = 50, message = "Nombre debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Nombre formato no válido.")
    private String  names;

    @NotBlank(message = "Apellido Paterno no debe ser vacio")
    @NotNull(message = "Apellido Paterno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Paterno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Paterno formato no válido.")
    private String  fatherSurname;

    @NotBlank(message = "Apellido Materno no debe ser vacio")
    @NotNull(message = "Apellido Materno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Materno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Materno formato no válido.")
    private String  motherSurname;

    @NotBlank(message = "Correo no debe ser vacio.")
    @NotNull(message = "Correo no debe ser nulo.")
    @Size(max = 100, message = "Correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "Correo formato no válido.")
    @Email(message = "Correo formato no válido.")
    private String  email;

    @NotBlank(message = "Ambito no debe ser vacio.")
    @NotNull(message = "Ambito no debe ser nulo.")
    @Size(max =50, message = "Ambito debe tener max 50 caracteres.")
    private String  scope;

    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer state = 1;


    // private String  sustento;
}
