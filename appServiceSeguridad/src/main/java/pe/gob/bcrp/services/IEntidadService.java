package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.EntidadDTO;

import java.util.List;

public interface IEntidadService {

    public List<EntidadDTO> getEntidades();
    public List<EntidadDTO> getEntidadByNombre(String nombre);
    public EntidadDTO saveEntidad(EntidadDTO entidadDto);
    public EntidadDTO updateUsuario(Integer id, EntidadDTO entidadDto);
    public void EntidadDelete(Integer id);
}
