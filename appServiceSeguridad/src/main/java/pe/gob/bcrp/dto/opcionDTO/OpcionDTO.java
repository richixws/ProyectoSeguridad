package pe.gob.bcrp.dto.opcionDTO;

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
public class OpcionDTO {

    //@NotEmpty(message = "Opcion no debe ser vacio")
    @JsonProperty("optionId")
    private Integer idOpcion;

    @JsonProperty("moduleId")
    @NotNull(message = "Modulo no debe ser vacio")
    private Integer idModulo;

    @JsonProperty("systemId")
    @NotNull(message = "Sistema no debe ser vacio")
    private Integer idSistema;

    @JsonProperty("optionName")
    @NotEmpty(message = "Nombre de Opcion no debe ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ](\\s?[a-zA-ZñÑáéíóúÁÉÍÓÚ])*$", message = "Nombre de opción con formato incorrecto")
    @Size(max = 100, message = "nombre opcion no debe superar los 100 caracteres.")
    private String nombreOpcion;

    @NotEmpty(message = "Link de Opcion no debe ser vacio")
    @Size(max = 200, message = "url no debe superar los 200 caracteres.")
    private String url;

    @JsonProperty("state")
    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
