package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "documento no puede ser vacio")
    private  Integer  idDocumento;


    private String tipoDocumento;

    @NotEmpty(message = "numero de documento no puede ser vacio")
    @Size(min = 8, max = 20, message = "Número de documento debe tener entre 8 y 20 caracteres.")
    private String numeroDocumento;

    @NotEmpty(message = "nombre no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ](\\s?[a-zñA-ZÑ])*$",message = "El nombre solo debe contener letras")
    private String nombre;

    @NotEmpty(message = "sigla no puede ser vacio")
    @Pattern(regexp = "^[a-zñA-ZÑ0-9_](\\s?[a-zñA-ZÑ0-9_])*$", message = "Sigla contiene caracteres no permitidos")
    private String sigla;
    //@NotEmpty(message = "codigo externo no puede ser vacio")
    private String codExterno;
}
