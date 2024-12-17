package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.moduloDTO.ModuloDTO;
import pe.gob.bcrp.dto.response.ModuloResponse;

public interface IModuloService {

    public ModuloResponse getAllModulos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema, String name);
    public ModuloDTO saveModulo(ModuloDTO modulo);
    public ModuloDTO updateModulo(ModuloDTO modulo, Integer idModulo);
    public boolean deleteModulo(Integer idModulo);

}
