package pe.gob.bcrp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Pattern(regexp = "^[a-zñA-ZÑ](\\s?[a-zñA-ZÑ])*$",message = "Nonbre de opcion con formato incorrecto")
    @Size(max = 100, message = "nombre opcion no debe superar los 100 caracteres.")
    private String nombreOpcion;

    @NotEmpty(message = "Link de Opcion no debe ser vacio")
    @Size(max = 200, message = "url no debe superar los 200 caracteres.")
    private String url;
}
