package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
}
