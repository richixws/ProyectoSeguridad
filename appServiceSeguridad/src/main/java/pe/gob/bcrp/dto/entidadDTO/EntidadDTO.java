package pe.gob.bcrp.dto.entidadDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadDTO {


    private Integer idEntidad;

    @NotNull(message = "documento no puede ser vacio")
    private  Integer  idDocumento;

    @NotEmpty(message = "numero de documento no puede ser vacio")
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "Número de documento sólo admite números.")
    @Size(min = 11, max = 20, message = "Número de documento debe tener entre 11 y 20 caracteres.")
    private String numeroDocumento;

    @NotEmpty(message = "nombre no puede ser vacio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ](\\s?[a-zñA-ZÑ])*$",message = "El nombre solo debe contener letras")
    private String nombre;

    @NotEmpty(message = "sigla no puede ser vacio")
    @Size(max = 100, message = "La sigla no puede tener más de 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9_](\\s?[a-zñA-ZÑ0-9_])*$", message = "Sigla contiene caracteres no permitidos")
    private String sigla;
    //@NotEmpty(message = "codigo externo no puede ser vacio")
    private String codExterno;

    @Min(value = 0, message = "Estadp sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estadp sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
