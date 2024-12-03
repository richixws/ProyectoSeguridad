package pe.gob.bcrp.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolFormDTO {


    private Integer idRol;
    // private Sistema sistema;
    @NotNull(message = "Sistema no debe ser vacio")
    private Integer  idSistema;

    @NotEmpty(message = "Rol no debe ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑ ]+$", message = "Nombre rol solo puede contener letras.")
    @Size(max = 100, message = "rol no debe superar los 100 caracteres.")
    private String  nombreRol;

    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;

}
