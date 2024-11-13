package pe.gob.bcrp.dto.sistemaDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistroSistemaDTO {

    private Integer idSistema;
    //@NotEmpty(message = "codigo no puede ser vacio")
    //@Column(unique = true, nullable = false)
    private String codigo;

    @NotEmpty(message = "nombre no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Nombre con formato incorrecto")
    @Size(max = 200, message = "Nombre debe tener max 200 caracteres.")
    private String nombre;

    @NotEmpty(message = "versus no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Version con formato incorrecto")
    public String version;

    @NotEmpty(message = "usuario responsable no puede ser vacio")
    private String usuarioResponsable;

    @NotEmpty(message = "usuario responsable alterno no puede ser vacio")
    private String usuarioResponsableAlt;

    @NotNull(message = "Usuario responsable no puede ser vacio")
    private  Integer idUsuarioResponsable;

    @NotNull(message = "Usuario responsable Alterno no puede ser vacio")
    private  Integer idUsuarioResponsableAlt;

    @NotEmpty(message = "url no puede ser vacio")
    private String url;

    @NotEmpty(message = "Url externo no puede ser vacio")
    private String urlExterno;

    @NotNull(message = "Estado critico no puede ser vacio")
    private Integer idEstadoCritico;

    @NotEmpty(message = "Unidad organizacional no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Unidad oraganizacional con formato incorrecto")
    private String unidOrganizacional;

    // @NotEmpty(message = "loginMain no puede ser vacio")
   // private String logoMain;

    //@NotEmpty(message = "logoHead no puede ser vacio")
    //private String logoHead;
}
