package pe.gob.bcrp.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    private String  nombreRol;
    private Integer estado;

}
