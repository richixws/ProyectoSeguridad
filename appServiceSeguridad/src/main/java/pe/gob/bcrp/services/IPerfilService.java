package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.PerfilDTO;
import pe.gob.bcrp.dto.response.PerfilResponse;

public interface IPerfilService {

    public PerfilResponse getAllPerfiles(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
                                         Integer idSistema, Integer idPerfil, String name);//, String nombre
    public PerfilDTO savePerfil(PerfilDTO perfilDTO);
    public PerfilDTO updatePerfil(PerfilDTO perfilDTO, Integer idPerfil);
    public boolean deletePerfil(Integer idPerfil);

}
