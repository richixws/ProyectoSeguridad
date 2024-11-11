package pe.gob.bcrp.dto;

import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "Sistema no debe ser vacio")
    private Integer  idSistema;
    @NotEmpty(message = "Rol no debe ser vacio")
    private String  nombreRol;
    private Integer estado;

}
