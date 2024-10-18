package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.dto.EntidadResponse;

import java.util.List;

public interface IEntidadService {

    public List<EntidadDTO> getEntidades();
    public EntidadResponse getAllEntidades(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    public EntidadDTO findEntidadByNombre(String nombre);
    public EntidadDTO saveEntidad(EntidadDTO entidadDto);
    public EntidadDTO updateEntidad(Integer id, EntidadDTO entidadDto);
    public boolean deleteEntidad(Integer id);
}
