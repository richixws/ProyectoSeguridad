package pe.gob.bcrp.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SistemaDTO {


    @NotNull( message = "id sistema no puede ser nulo")
    private Integer idSistema;

    @NotEmpty(message = "codigo no puede ser vacio")
    private String codigo;

    @NotEmpty(message = "nombre no puede ser vacio")
    private String nombre;

    @NotEmpty(message = "versus no puede ser vacio")
    public String version;

    @NotEmpty(message = "loginMain no puede ser vacio")
    private String logoMain;

    @NotEmpty(message = "logoHead no puede ser vacio")
    private String logoHead;

    @NotEmpty(message = "url no puede ser vacio")
    private String url;


}
