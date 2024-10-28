package pe.gob.bcrp.services;
import pe.gob.bcrp.dto.OpcionDTO;
import pe.gob.bcrp.dto.response.OpcionResponse;

public interface IOpcionService {


    public OpcionResponse getAllOpciones(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema,Integer idModulo);//, String nombre
    public OpcionDTO saveOpcion(OpcionDTO opcion);
    public OpcionDTO updateOpcion(OpcionDTO opcion, Integer idOpcion);
    public boolean deleteOpcion(Integer idOpcion);
}
