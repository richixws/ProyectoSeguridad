package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.repositories.IPersonaRepository;
import pe.gob.bcrp.services.IPersonaService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonaServiceImpl  implements IPersonaService {

    private IPersonaRepository  iPersonaRepository ;

    private ModelMapper modelMapper;

    @Override
    public List<PersonaDTO> getPersonas() {

        List<Persona> listPersonas=iPersonaRepository.findAll();
        return listPersonas.stream()
                .map(persona -> modelMapper.map(persona,PersonaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PersonaDTO save(PersonaDTO personaDTO) {

        Persona persona=modelMapper.map(personaDTO,Persona.class);
        Persona newPersona=iPersonaRepository.save(persona);
        PersonaDTO newPersonaDTO=modelMapper.map(newPersona,PersonaDTO.class);
        return newPersonaDTO;
    }

    @Override
    public PersonaDTO updatePersona(Integer id, PersonaDTO personaDTO) {

        Persona persona=iPersonaRepository.findById(id).orElseThrow( ()-> new RuntimeException("Persona no encontrada") );
        persona.setApellidoMaterno(personaDTO.getApellidoMaterno());
        persona.setNombres(personaDTO.getNombres());
        persona.setApellidoPaterno(personaDTO.getApellidoPaterno());
        persona.setTipoDocumento(personaDTO.getTipoDocumento());
        persona.setDocumentoIdentidad(personaDTO.getDocumentoIdentidad());
        PersonaDTO newPersonaDTO=modelMapper.map(persona,PersonaDTO.class);
        return newPersonaDTO;
    }

    @Override
    public void delete(Integer id) {
         iPersonaRepository.deleteById(id);
    }
}
