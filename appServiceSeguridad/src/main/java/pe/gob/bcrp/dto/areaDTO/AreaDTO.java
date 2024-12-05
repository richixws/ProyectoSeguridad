package pe.gob.bcrp.dto.areaDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AreaDTO {

    private Integer idArea;
    @NotNull(message = "Sistema no puede ser vacio")
    private Integer idSistema;

    @NotEmpty(message = "nombre no puede ser vacio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    private String nombreArea;

    @NotEmpty(message = "descripción no puede ser vacio")
    @Size(max = 100, message = "La descripción no puede tener más de 300 caracteres.")
    private String descripcionArea;

    @Min(value = 0, message = "Estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
