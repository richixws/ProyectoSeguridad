package pe.gob.bcrp.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EntidadDTO {


    private Integer idEntidad;
   // @NotEmpty(message = "tipo documento no puede ser vacio")
   // private String tipoDocumento;
   // @Column(name = "numero_documento", length = 25,nullable = false )
    private  DocumentoIdentidadDTO documentoIdentidad;

    @NotEmpty(message = "numero de documento no puede ser vacio")
    private String numeroDocumento;

    @NotEmpty(message = "nombre no puede ser vacio")
    private String nombre;
    @NotEmpty(message = "sigla no puede ser vacio")
    private String sigla;
    @NotEmpty(message = "codigo externo no puede ser vacio")
    private String codExterno;
}
