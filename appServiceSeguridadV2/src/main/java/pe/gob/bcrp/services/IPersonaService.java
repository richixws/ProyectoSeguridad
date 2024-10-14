package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.dto.UsuarioDTO;

import java.util.List;

public interface IPersonaService {

    public List<PersonaDTO> getPersonas();
    public PersonaDTO save(PersonaDTO personaDTO);
    public PersonaDTO updatePersona(Integer id, PersonaDTO personaDTO);
    public void  delete(Integer id);

}
