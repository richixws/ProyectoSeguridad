package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.dto.response.PersonaResponse;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IPersonaRepository;
import pe.gob.bcrp.services.IPersonaService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PersonaServiceImpl  implements IPersonaService {


    private IPersonaRepository  iPersonaRepository ;
    private ModelMapper modelMapper;
    private final Util util;



    @Override
    public PersonaResponse getAllPersonas( Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre) {

        log.info("INI - Service GetAllPersonas() ");
        try {
            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Persona> pagePersona=null;

            if(nombre!=null){
                 pagePersona=iPersonaRepository.findByFilters(nombre,pageDetails);
            }else {
                  pagePersona=iPersonaRepository.findByIsDeletedFalse(pageDetails);
            }
            var personas=pagePersona.getContent();
            var lisPersonasDto= personas.stream()
                                        .map(persona -> modelMapper.map(persona, PersonaDTO.class))
                                        .collect(Collectors.toList());

            PersonaResponse personaResponse = new PersonaResponse();
            personaResponse.setContent(lisPersonasDto);
            personaResponse.setPageNumber(pagePersona.getNumber());
            personaResponse.setPageSize(pagePersona.getSize());
            personaResponse.setTotalPages(pagePersona.getTotalPages());
            personaResponse.setTotalElements(pagePersona.getTotalElements());
            personaResponse.setLastPage(pagePersona.isLast());
            return personaResponse;

        }catch (Exception e) {
            log.error("ERROR Service - GetAllPersonas() "+e.getMessage());
            throw new RuntimeException("ERROR Service - GetAllPersonas() "+e.getMessage());
        }
    }

    @Override
    public PersonaDTO addPersona(PersonaDTO personaDTO) {
        log.info("INFO - Service AddPersona() ");
        try {
            Usuario usuario = util.getUsuario();

            boolean existeNumeroDocumento = iPersonaRepository.existsByNumeroDocumento(personaDTO.getDocumentoIdentidad());
            if (existeNumeroDocumento) {
                throw new IllegalArgumentException("El numero de documento de identidad ya existe en el sistema.");
            }

            Persona persona=modelMapper.map(personaDTO,Persona.class);
            persona.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            persona.setUsuarioCreacion(usuario.getUsuario());
            Persona newPersona=iPersonaRepository.save(persona);
            PersonaDTO newPersonaDTO=modelMapper.map(newPersona,PersonaDTO.class);
            return newPersonaDTO;


        }catch (IllegalArgumentException e){
            throw  new IllegalArgumentException("El numero de documento de identidad ya existe en el sistema.");

        }catch (Exception e) {
            throw new RuntimeException("ERROR Service - GetAllPersonas() "+e.getMessage());
        }
    }

    @Override
    public PersonaDTO updatePersona(Integer idPersona, PersonaDTO personaDTO) {

        log.info("INFO - Service UpdatePersona() ");
        try {
            Usuario usuario = util.getUsuario();
            boolean existeDocumentoIdentidad = iPersonaRepository.existsByNumeroDocumentoAndIdPersonaNot(personaDTO.getDocumentoIdentidad(), idPersona);
            if (existeDocumentoIdentidad) {
                throw new IllegalArgumentException("El número de documento identidad ya está registrado en otra Persona");
            }



            Persona persona=iPersonaRepository.findById(idPersona).orElseThrow( ()-> new RuntimeException("Persona no encontrada") );
            persona.setApellidoMaterno(personaDTO.getApellidoMaterno());
            persona.setNombres(personaDTO.getNombres());
            persona.setApellidoPaterno(personaDTO.getApellidoPaterno());
            //persona.setDocuIdentidad(personaDTO.getTipoDocumento());
            persona.setNumeroDocumento(personaDTO.getDocumentoIdentidad());
            persona.setCorreo(personaDTO.getCorreo());

            persona.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            persona.setUsuarioActualizacion(usuario.getUsuario());
            PersonaDTO newPersonaDTO=modelMapper.map(persona,PersonaDTO.class);
            return newPersonaDTO;

        } catch (IllegalArgumentException e) {
            log.error("ERROR - Service updatePersona() - " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (Exception e ){
            throw new RuntimeException("ERROR Service - updatePersona() "+e.getMessage());
        }
    }

    @Override
    public boolean deletePersona(Integer idPersona) {
        log.info("INFO - Service DeletePersona() ");
        var estado=false;
        try {
            Usuario usuario = util.getUsuario();
            var persona=iPersonaRepository.findById(idPersona).orElseThrow(()->new  ResourceNotFoundException("Persona no encontrado"));
            if(persona!=null){
               persona.setDeleted(true);
               persona.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
               persona.setUsuarioActualizacion(usuario.getUsuario());
               iPersonaRepository.save(persona);
               estado=true;
            }

        } catch (ResourceNotFoundException e) {
            log.error("ERROR Service - DeletePersona() "+e.getMessage());
            throw new RuntimeException("ERROR Service - DeletePersona() "+e.getMessage());
        }
        return estado;
    }
}
