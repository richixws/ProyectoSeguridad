package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import pe.gob.bcrp.entities.Sistema;

import java.io.Serializable;
import java.util.Date;

@Data
public class ModuloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer idModulo;

    private Integer idSistema;

    private String nombreModulo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date orderDate;
}
