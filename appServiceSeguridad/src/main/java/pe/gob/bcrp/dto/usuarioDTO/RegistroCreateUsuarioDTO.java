package pe.gob.bcrp.dto.usuarioDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import javax.annotation.Nullable;

@Data
public class RegistroCreateUsuarioDTO {

    @NotNull(message = "Tipo de documento no debe ser nulo.")
    @Min(value = 1, message = "Tipo de documento no puede ser menor que 1.")
    private Integer tipoDocumento;

    @NotBlank(message = "Número de documento no debe estar vacío.")
    @Size(min = 8, max = 20, message = "Número de documento debe tener entre 8 y 20 caracteres.")
    private String  numeroDocumento;

    @NotBlank(message = "Nombre no debe ser vacio")
    @NotNull(message = "Nombre no debe ser nulo.")
    @Size(max = 50, message = "Nombre debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Nombre formato no válido.")
    private String  nombres;

    @NotBlank(message = "Apellido Paterno no debe ser vacio")
    @NotNull(message = "Apellido Paterno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Paterno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Paterno formato no válido.")
    private String  apePaterno;

    @NotBlank(message = "Apellido Materno no debe ser vacio")
    @NotNull(message = "Apellido Materno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Materno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Materno formato no válido.")
    private String  apeMaterno;

    @NotBlank(message = "Correo no debe ser vacio.")
    @NotNull(message = "Correo no debe ser nulo.")
    @Size(max = 100, message = "Correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "Correo formato no válido.")
    @Email(message = "Correo formato no válido.")
    private String  correoElectronico;


    @NotBlank(message = "Ambito no debe ser vacio.")
    @NotNull(message = "Ambito no debe ser nulo.")
    @Size(max =50, message = "Ambito debe tener max 50 caracteres.")
    private String  ambito;

    // @NotBlank(message = "Ambito no debe ser vacio.")
    //  @NotNull(message = "Ambito no debe ser nulo.")
    // @Nullable
   //  private String  sustento;
}
