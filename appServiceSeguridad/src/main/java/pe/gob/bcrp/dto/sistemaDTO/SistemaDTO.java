package pe.gob.bcrp.dto.sistemaDTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@Builder
public class SistemaDTO {



    @JsonProperty("idSystem")
    private Integer idSistema;

   // @NotEmpty(message = "codigo no puede ser vacio")
   // private String codigo;

    @JsonProperty("name")
    private String nombre;

    @JsonProperty("version")
    public String version;

    @JsonProperty("logoMain")
    private String logoMain;

    @JsonProperty("logoHead")
    private String logoHead;

    @JsonProperty("url")
    private String url;

    @JsonProperty("userResponsible")
    private String usuarioResponsable;

    @JsonProperty("idUserResponsible")
    private Integer idUsuarioResponsable;

    @JsonProperty("userResponsibleAlternate")
    private String usuarioResponsableAlterno;

    @JsonProperty("idUserResponsibleAlternate")
    private Integer idUsuarioResponsableAlterno;

    @JsonProperty("urlExternal")
    private String urlExterno;

    @JsonProperty("idStateCritical")
    private String idEstadoCritico;

    @JsonProperty("unitOrganizational")
    private String unidadOrganizacional;

    @JsonProperty("estate")
    private Integer  estado;

    @JsonProperty("delete")
    private boolean isDeleted;

}
