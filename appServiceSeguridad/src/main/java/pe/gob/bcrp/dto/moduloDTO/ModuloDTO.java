package pe.gob.bcrp.dto.moduloDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("moduleId")
    private Integer idModulo;

    @JsonProperty("systemId")
    @NotNull(message = "Sistema no debe ser vacio")
    private Integer idSistema;

    @JsonProperty("moduleName")
    @NotEmpty(message = "Nombre de Modulo no debe ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Nombre modulo tiene campo incorrecto.")
    private String nombreModulo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;

    @JsonProperty("state")
    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
