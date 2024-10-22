package pe.gob.bcrp.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
//@Builder
public class SistemaDTO {


    @NotNull( message = "id sistema no puede ser nulo")
    private Integer idSistema;

   // @NotEmpty(message = "codigo no puede ser vacio")
   // private String codigo;

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

   // private boolean isDeleted=false;
    private List<UsuarioResponsableDTO> usuarioResponsable;





}
