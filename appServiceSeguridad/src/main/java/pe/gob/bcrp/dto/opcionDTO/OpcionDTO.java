package pe.gob.bcrp.dto.opcionDTO;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
public class OpcionDTO {

    //@NotEmpty(message = "Opcion no debe ser vacio")
    private Integer idOpcion;

    @NotNull(message = "Modulo no debe ser vacio")
    private Integer idModulo;

    @NotNull(message = "Sistema no debe ser vacio")
    private Integer idSistema;

    @NotEmpty(message = "Nombre de Opcion no debe ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ](\\s?[a-zA-ZñÑáéíóúÁÉÍÓÚ])*$", message = "Nombre de opción con formato incorrecto")
    @Size(max = 100, message = "nombre opcion no debe superar los 100 caracteres.")
    private String nombreOpcion;

    @NotEmpty(message = "Link de Opcion no debe ser vacio")
    @Size(max = 200, message = "url no debe superar los 200 caracteres.")
    private String url;

    @Min(value = 0, message = "Estadp sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estadp sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
