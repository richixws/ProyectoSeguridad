package pe.gob.bcrp.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditoriaDto {


    private LocalDateTime horaCreacion;

    private LocalDateTime horaDeEliminacion;

    private LocalDateTime horaActualizacion;

    private String usuarioCreacion;

    private String usuarioEliminacion;

    private String usuarioActualizacion;


}
