package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntidadDTO {


    private Integer idEntidad;
   // @NotEmpty(message = "tipo documento no puede ser vacio")
   // private String tipoDocumento;
   // @Column(name = "numero_documento", length = 25,nullable = false )
    //private  DocumentoIdentidadDTO documentoIdentidad;

    private  Integer  idDocumento;

    private String tipoDocumento;

    @NotEmpty(message = "numero de documento no puede ser vacio")
    private String numeroDocumento;

    @NotEmpty(message = "nombre no puede ser vacio")
    @Pattern(regexp = "^[a-zA-ZñÑ\\s]+$", message = "El nombre solo debe contener letras y espacios")
    private String nombre;

    @NotEmpty(message = "sigla no puede ser vacio")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "La sigla solo puede contener letras y números")
    private String sigla;
    //@NotEmpty(message = "codigo externo no puede ser vacio")
    private String codExterno;
}
