package pe.gob.bcrp.dto.sistemaDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
public class RegistroSistemaDTO {

    private Integer idSistema;
    //@NotEmpty(message = "codigo no puede ser vacio")
    //@Column(unique = true, nullable = false)
    private String codigo;

    @NotEmpty(message = "nombre no puede ser vacio.")
    @Size(max = 100, message = "nombre sistema no puede tener más de 100 caracteres.")
    @Pattern(regexp = "^[a-zA-ZñÑ]+(\\s[a-zA-ZñÑ]+)*$", message = "nombre con formato incorrecto, corrija")
    private String nombre;

    @NotEmpty(message = "versus no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9\\_](\\s?[a-zñA-ZÑ0-9\\_])*$", message = "Version con formato incorrecto")
    @Size(max = 100, message = "version no puede tener más de 50 caracteres.")
    public String version;


    @NotNull(message = "usuario responsable no puede ser vacio")
    @Min(value = 1, message = "El Usuario Responsable debe ser mayor o igual a 1")
    @Max(value = 1, message = "El Usuario Responsable no debe ser mayor o igual a 1")
    private  Integer idUsuarioResponsable;

    @NotNull(message = "Usuario responsable Alterno no puede ser vacio")
    @Min(value = 1, message = "El Usuario Responsable Alterno debe ser mayor o igual a 1")
    @Max(value = 1, message = "El Usuario Responsable Alterno  no debe ser mayor o igual a 1")
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
    @Pattern(regexp = "^[a-zA-ZñÑ]+(\\s[a-zA-ZñÑ]+)*$", message = "unidad organizacional con formato incorrecto, corrija")
    @Size(max = 100, message = "unidad organizacional no puede tener más de 100 caracteres.")
    private String unidOrganizacional;

    @Min(value = 0, message = "Estadp sólo admite el número 0 o 1.")
    @Max(value = 1, message = "Estadp sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
