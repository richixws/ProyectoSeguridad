package pe.gob.bcrp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolFormDTO {


    private Integer idRol;
    // private Sistema sistema;
    @NotNull(message = "Sistema no debe ser vacio")
    private Integer  idSistema;

    @NotEmpty(message = "Rol no debe ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑ ]+$", message = "Nombre rol solo puede contener letras.")
    @Size(max = 100, message = "rol no debe superar los 100 caracteres.")
    private String  nombreRol;

    @NotNull(message = "Estado no puede ser vacío.")
    @Min(value = 0, message = "Estadp sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estadp sólo admite el número 0 o 1.")
    private Integer estado;

}
