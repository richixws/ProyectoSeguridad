package pe.gob.bcrp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "^[a-zA-ZñÑáéíóúÁÉÍÓÚ]+([\\-'\\s]?[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+)*$",
            message = "Formato no válido para el nombre del perfil")
    private String nombreOpcion;

    @NotEmpty(message = "Link de Opcion no debe ser vacio")
    private String url;
}
