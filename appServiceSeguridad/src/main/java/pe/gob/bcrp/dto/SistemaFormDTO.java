package pe.gob.bcrp.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SistemaFormDTO {

    @NotEmpty(message = "codigo no puede ser vacio")
    @Column(unique = true, nullable = false)
    private String codigo;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Pattern(regexp = "^[a-zA-ZñÑ]+(\\s[a-zA-ZñÑ]+)*$", message = "formato de nombre incorrecto, corrija")
    @Size(max = 100, message = "El nombre debe tener como máximo 100 caracteres.")
    private String nombre;

    @NotEmpty(message = "versus no puede ser vacio")
    public String version;

    @NotEmpty(message = "loginMain no puede ser vacio")
    private String logoMain;

    @NotEmpty(message = "logoHead no puede ser vacio")
    private String logoHead;

    @NotEmpty(message = "url no puede ser vacio")
    private String url;

    @NotEmpty(message = "usuario responsable no puede ser vacio")
    private String usuarioResponsable;

    @NotEmpty(message = "usuario responsable alterno no puede ser vacio")
    private String usuarioResponsableAlterno;


}
