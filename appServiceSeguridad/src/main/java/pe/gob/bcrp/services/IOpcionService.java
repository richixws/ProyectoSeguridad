package pe.gob.bcrp.services;
import pe.gob.bcrp.dto.opcionDTO.OpcionDTO;
import pe.gob.bcrp.dto.response.OpcionResponse;

public interface IOpcionService {


    public OpcionResponse getAllOpciones(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
                                         Integer idSistema,Integer idModulo, String name);
    public OpcionDTO saveOpcion(OpcionDTO opcion);
    public OpcionDTO updateOpcion(OpcionDTO opcion, Integer idOpcion);
    public boolean deleteOpcion(Integer idOpcion);
}
