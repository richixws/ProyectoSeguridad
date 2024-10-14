package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import pe.gob.bcrp.entities.Persona;

import java.util.Date;

@Data
public class UsuarioDTO {


    private Integer idUsuario;
    private String correoInstitucional;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date fechaCreacion;
    private String docSustento;
    private String ambito;
    private String usuario;
    private String password;
    private Integer idPersona;
    private Persona persona;

}
