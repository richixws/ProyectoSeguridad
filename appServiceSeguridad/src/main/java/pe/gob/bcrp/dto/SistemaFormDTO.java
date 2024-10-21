package pe.gob.bcrp.dto;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
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

    SistemaFormDTO(String nombre, String version, String logoMain, String logoHead, String url) {

     this.nombre = nombre;
      this.version = version;
   this.logoMain = logoMain;
     this.logoHead = logoHead;
     this.url = url;

    }
}
