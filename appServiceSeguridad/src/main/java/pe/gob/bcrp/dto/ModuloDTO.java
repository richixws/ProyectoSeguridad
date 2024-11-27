package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ModuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Integer idModulo;

    @NotNull(message = "Sistema no debe ser vacio")
    private Integer idSistema;

    @NotEmpty(message = "Nombre de Modulo no debe ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Nombre modulo tiene campo incorrecto.")
    private String nombreModulo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;

    @NotNull(message = "Estado no puede ser vacío.")
    @Min(value = 0, message = "Estadp sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estadp sólo admite el número 0 o 1.")
    private Integer estado;
}
