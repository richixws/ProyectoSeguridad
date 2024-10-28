package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.ModuloDTO;
import pe.gob.bcrp.dto.RolDTO;
import pe.gob.bcrp.dto.response.ModuloResponse;
import pe.gob.bcrp.dto.response.RolResponse;

public interface IRolService {

    public RolResponse getAllRoles(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema, Integer idRol);//, String nombre
    public RolDTO saveRole(RolDTO rolDTO);
    public RolDTO updateRole(RolDTO rolDTO, Integer idRol);
    public boolean deleteRole(Integer idRol);

}
