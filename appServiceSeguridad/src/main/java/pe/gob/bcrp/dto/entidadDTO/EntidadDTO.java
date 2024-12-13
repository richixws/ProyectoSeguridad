package pe.gob.bcrp.dto.entidadDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.Views;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadDTO {

   // @JsonProperty("entityId")
   // private Integer idEntidad;
    //private Integer idEntidad;
    @JsonProperty("documentId")
    @NotNull(message = "documento no puede ser vacio.")
    private  Integer  idDocumento;

    @JsonProperty("documentNumber")
    @NotEmpty(message = "numero de documento no puede ser vacio.")
    @Pattern(regexp = "^[0-9]+(\\\\.[0-9]+)?$", message = "numero de documento sólo admite números.")
    @Size(min = 11, max = 20, message = "numero de documento debe tener entre 11 y 20 caracteres.")
    private String numeroDocumento;

    @JsonProperty("name")
    @NotEmpty(message = "nombre no puede ser vacio.")
    @Size(max = 100, message = "nombre no puede tener más de 100 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ](\\s?[a-zñA-ZÑ])*$",message = "nombre solo debe contener letras.")
    private String nombre;

    @JsonProperty("initials")
    @NotEmpty(message = "sigla no puede ser vacio.")
    @Size(max = 100, message = "sigla no puede tener más de 50 caracteres.")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9_](\\s?[a-zñA-ZÑ0-9_])*$", message = "sigla contiene caracteres no permitidos.")
    private String sigla;

    //@NotEmpty(message = "codigo externo no puede ser vacio")
    @JsonProperty("externalCode")
    @JsonView(Views.Update.class)
    private String codExterno;

    @JsonProperty("state")
    @Min(value = 0, message = "estado sólo admite el número 0 o 1.")
    @Max(value = 1, message = "estado sólo admite el número 0 o 1.")
    @Builder.Default
    private Integer estado = 1;
}
