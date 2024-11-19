package pe.gob.bcrp.dto.sistemaDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistroSistemaDTO {

    private Integer idSistema;
    //@NotEmpty(message = "codigo no puede ser vacio")
    //@Column(unique = true, nullable = false)
    private String codigo;

    @NotEmpty(message = "nombre no puede ser vacio.")
    @Size(max = 200, message = "nombre sistema no puede tener más de 100 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "nombre sistema con formato incorrecto.")
    private String nombre;

    @NotEmpty(message = "versus no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Version con formato incorrecto")
    @Size(max = 50, message = "version no puede tener más de 50 caracteres.")
    public String version;

    @NotEmpty(message = "usuario responsable no puede ser vacio")
    @Size(max = 50, message = "version no puede tener más de 50 caracteres.")
    @Pattern(regexp = "^[a-zA-ZñÑ0-9áéíóúÁÉÍÓÚüÜ ]*$", message = "Solo se permiten letras, números y espacios.")
    private String usuarioResponsable;

    @NotEmpty(message = "usuario responsable alterno no puede ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑ0-9áéíóúÁÉÍÓÚüÜ ]*$", message = "Solo se permiten letras, números y espacios.")
    @Size(max = 50, message = "version no puede tener más de 50 caracteres.")
    private String usuarioResponsableAlt;

    @NotNull(message = "Usuario responsable no puede ser vacio")
    @Min(value = 1, message = "El valor debe ser mayor o igual a 1")
    private  Integer idUsuarioResponsable;

    @NotNull(message = "Usuario responsable Alterno no puede ser vacio")
    @Min(value = 1, message = "El valor debe ser mayor o igual a 1")
    private  Integer idUsuarioResponsableAlt;

    @NotEmpty(message = "url no puede ser vacio")
    @Size(max = 150, message = "url no puede tener más de 150 caracteres.")
    private String url;

    @NotEmpty(message = "Url externo no puede ser vacio")
    @Size(max = 150, message = "urlExterno no puede tener más de 150 caracteres.")
    private String urlExterno;

    @NotNull(message = "Estado critico no puede ser vacio")
    @Min(value = 1, message = "El estado crítico debe ser al menos 1.")
    @Max(value = 4, message = "El estado no puede ser mayor 4.")
    private Integer idEstadoCritico;

    @NotEmpty(message = "Unidad organizacional no puede ser vacio")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ]+(\\s[a-zA-ZáéíóúÁÉÍÓÚñÑ]+)*$", message = "unidad organizacional con formato incorrecto. Solo se permiten letras y espacios simples.")
    @Size(max = 200, message = "version no puede tener más de 200 caracteres.")
    private String unidOrganizacional;

    // @NotEmpty(message = "loginMain no puede ser vacio")
   // private String logoMain;

    //@NotEmpty(message = "logoHead no puede ser vacio")
    //private String logoHead;
}
