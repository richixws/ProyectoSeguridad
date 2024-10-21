package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.dto.EntidadResponse;

import java.util.List;

public interface IEntidadService {

    public List<EntidadDTO> getEntidades();
    public EntidadResponse getAllEntidades(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String nombre);
    public EntidadDTO findEntidadByNombre(String nombre);
    public EntidadDTO saveEntidad(EntidadDTO entidadDto, String tipoDocumento);
    public EntidadDTO updateEntidad(Integer id, EntidadDTO entidadDto,String tipoDocumento);
    public boolean deleteEntidad(Integer id);
}
