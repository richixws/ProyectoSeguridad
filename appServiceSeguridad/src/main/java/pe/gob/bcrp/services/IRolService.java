package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.RolFormDTO;
import pe.gob.bcrp.dto.response.RolResponse;

public interface IRolService {

    public RolResponse getAllRoles(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema, Integer idRol);//, String nombre
    public RolFormDTO saveRole(RolFormDTO rolDTO);
    public RolFormDTO updateRole(RolFormDTO rolDTO, Integer idRol);
    public boolean deleteRole(Integer idRol);

}
