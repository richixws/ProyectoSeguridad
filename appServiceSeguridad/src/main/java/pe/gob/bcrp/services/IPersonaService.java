package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.personaDTO.PersonaDTO;
import pe.gob.bcrp.dto.response.PersonaResponse;

public interface IPersonaService {

    //public List<DocumentoIdentidadDTO> getAllDocumentoIdentifiacion();
    public PersonaResponse getAllPersonas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre);
    public PersonaDTO addPersona(PersonaDTO personaDTO);
    public PersonaDTO updatePersona(Integer id, PersonaDTO personaDTO);
    public boolean deletePersona(Integer idPersona);

}
