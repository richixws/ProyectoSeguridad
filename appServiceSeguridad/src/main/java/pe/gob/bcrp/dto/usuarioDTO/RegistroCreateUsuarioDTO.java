package pe.gob.bcrp.dto.usuarioDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;



@Data
public class RegistroCreateUsuarioDTO {

    @JsonProperty("documentType")
    @NotNull(message = "Tipo de documento no debe ser nulo.")
    @Min(value = 1, message = "Tipo de documento no puede ser menor que 1.")
    private Integer tipoDocumento;

    @JsonProperty("documentNumber")
    @NotBlank(message = "Número de documento no debe estar vacío.")
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "Número de documento sólo admite números.")
    @Size(min = 8, max = 8, message = "Número de documento debe tener 8 caracteres.")
    private String  numeroDocumento;

    @JsonProperty("names")
    @NotBlank(message = "Nombre no debe ser vacio")
    @NotNull(message = "Nombre no debe ser nulo.")
    @Size(max = 50, message = "Nombre debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Nombre formato no válido.")
    private String  nombres;

    @JsonProperty("fatherSurname")
    @NotBlank(message = "Apellido Paterno no debe ser vacio")
    @NotNull(message = "Apellido Paterno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Paterno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Paterno formato no válido.")
    private String  apePaterno;

    @JsonProperty("motherSurname")
    @NotBlank(message = "Apellido Materno no debe ser vacio")
    @NotNull(message = "Apellido Materno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Materno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Materno formato no válido.")
    private String  apeMaterno;

    @JsonProperty("email")
    @NotBlank(message = "Correo no debe ser vacio.")
    @NotNull(message = "Correo no debe ser nulo.")
    @Size(max = 100, message = "Correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "Correo formato no válido.")
    @Email(message = "Correo formato no válido.")
    private String  correoElectronico;


    @JsonProperty("scope")
    @NotBlank(message = "Ambito no debe ser vacio.")
    @NotNull(message = "Ambito no debe ser nulo.")
    @Size(max =50, message = "Ambito debe tener max 50 caracteres.")
    private String  ambito;

    // @NotBlank(message = "Ambito no debe ser vacio.")
    //  @NotNull(message = "Ambito no debe ser nulo.")
    // @Nullable
   //  private String  sustento;
}
