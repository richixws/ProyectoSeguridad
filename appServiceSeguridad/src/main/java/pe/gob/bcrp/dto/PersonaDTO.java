package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PersonaDTO {


    private Integer idPersona;
    @NotNull(message = "Tipo de documento no debe ser nulo.")
    private Integer tipoDocumento;

    @NotBlank(message = "Número de documento no debe estar vacío.")
    @Size(min = 8, max = 20, message = "Número de documento debe tener entre 8 y 20 caracteres.")
    private String  numeroDocumento;

    @NotBlank(message = "Apellido Paterno no debe ser vacio")
    @NotNull(message = "Apellido Paterno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Paterno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Paterno formato no válido.")
    private String  apellidoPaterno;

    @NotBlank(message = "Apellido Materno no debe ser vacio")
    @NotNull(message = "Apellido Materno no debe ser nulo.")
    @Size(max = 50, message = "Apellido Materno debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Materno formato no válido.")
    private String  apellidoMaterno;

    @NotBlank(message = "Nombre no debe ser vacio")
    @NotNull(message = "Nombre no debe ser nulo.")
    @Size(max = 50, message = "Nombre debe tener max 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Nombre formato no válido.")
    private String  nombres;

    @NotBlank(message = "Correo no debe ser vacio.")
    @NotNull(message = "Correo no debe ser nulo.")
    @Size(max = 100, message = "Correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "Correo formato no válido.")
    @Email(message = "Correo formato no válido.")
    private String correo;
}
