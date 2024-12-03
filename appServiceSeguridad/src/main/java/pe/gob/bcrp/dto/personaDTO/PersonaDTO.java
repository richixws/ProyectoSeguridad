package pe.gob.bcrp.dto.personaDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaDTO {


    private Integer idPersona;
    @NotNull(message = "Tipo de documento no debe ser nulo.")
    private Integer tipoDocumento;

    @NotBlank(message = "Número de documento no debe estar vacío.")
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "Número de documento sólo admite números.")
    @Size(min = 8, max = 8, message = "Número de documento dni debe tener 8 caracteres.", groups = ValidateDni.class)
    @Size(min = 9, max = 9, message = "Número de documento pasaporte debe tener 9 caracteres.", groups = ValidatePasaporte.class)
    private String  numeroDocumento;

    @NotBlank(message = "Apellido Paterno no debe ser vacio")
    @NotNull(message = "Apellido Paterno no debe ser nulo.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Paterno formato no válido.")
    @Size(max = 100, message = "apellido paterno no debe superar los 100 caracteres.")
    private String  apellidoPaterno;

    @NotBlank(message = "Apellido Materno no debe ser vacio")
    @NotNull(message = "Apellido Materno no debe ser nulo.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Apellido Materno formato no válido.")
    @Size(max = 100, message = "apellido materno no debe superar los 100 caracteres.")
    private String  apellidoMaterno;

    @NotBlank(message = "Nombre no debe ser vacio")
    @NotNull(message = "Nombre no debe ser nulo.")
    @Pattern(regexp = "^[a-zñA-ZÑ]+[\\-'\\s]?[a-zñA-ZÑ ]+$", message = "Nombre formato no válido.")
    @Size(max = 100, message = "nombres no debe superar los 100 caracteres.")
    private String  nombres;

    @NotBlank(message = "Correo no debe ser vacio.")
    @NotNull(message = "Correo no debe ser nulo.")
    @Size(max = 100, message = "Correo debe tener max 100 caracteres.")
    @Pattern(regexp = ".+@.+\\..+", message = "Correo formato no válido.")
    @Email(message = "Correo formato no válido.")
    private String correo;

    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
