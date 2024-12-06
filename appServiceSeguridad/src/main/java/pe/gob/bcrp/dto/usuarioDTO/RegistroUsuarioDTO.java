package pe.gob.bcrp.dto.usuarioDTO;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class RegistroUsuarioDTO {

    @NotNull(message = "Tipo de documento no debe ser nulo.")
    private Integer tipoDocumento;

    @NotEmpty(message = "Número de documento no debe estar vacío.")
    @Size(min = 8, max = 20, message = "Número de documento debe tener entre 8 y 20 caracteres.")
    private String  numeroDocumento;

    @NotEmpty(message = "Nombre no debe ser vacio")
    @Size(max = 50, message = "Nombre debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "El Nombre deve de ser texto.")
    private String  nombres;

    @NotEmpty(message = "Apellido Paterno no debe ser vacio")
    @Size(max = 50, message = "Apellido Paterno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Paterno debe de ser texto")
    private String  apePaterno;

    @NotEmpty(message = "Apellido Materno no debe ser vacio")
    @Size(max = 50, message = "Apellido Materno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Materno debe de ser texto.")
    private String  apeMaterno;

    @NotEmpty(message = "Correo no debe ser vacio.")
    @Size(max = 100, message = "Correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "Correo formato no válido.")
    @Email(message = "Correo formato no válido.")
    private String  correoElectronico;


    @NotEmpty(message = "Ambito no debe ser vacio.")
    @Size(max =50, message = "Ambito debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Ambito debe de ser texto.")
    private String  ambito;


    // private String  sustento;
}
