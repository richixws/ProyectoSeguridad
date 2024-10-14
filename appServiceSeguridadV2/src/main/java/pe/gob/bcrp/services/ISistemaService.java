package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.dto.UsuarioDTO;

import java.util.List;

public interface ISistemaService {

    public List<SistemaDTO> getSistemas();
    public SistemaDTO saveSistemas(SistemaDTO usuarioDTO);
    public SistemaDTO updateSistemas(Integer id, SistemaDTO sistemaDTO);
    public void  deleteSistemas(Integer id);
}
