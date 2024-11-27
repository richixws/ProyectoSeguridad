package pe.gob.bcrp.services.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.dto.personaDTO.ValidateDni;
import pe.gob.bcrp.dto.personaDTO.ValidatePasaporte;
import pe.gob.bcrp.dto.personaDTO.ValidateRuc;
import pe.gob.bcrp.dto.response.PersonaResponse;
import pe.gob.bcrp.entities.DocumentoIdentidad;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IDocumentoIdentidadRepository;
import pe.gob.bcrp.repositories.IPersonaRepository;
import pe.gob.bcrp.services.IPersonaService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PersonaServiceImpl  implements IPersonaService {


    private IPersonaRepository  iPersonaRepository ;
    private IDocumentoIdentidadRepository documentoIdentidadRepository;
    private ModelMapper modelMapper;
    private Util util;



    @Override
    @Cacheable(value = "personas", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #nombre}")
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
    @CacheEvict(value = "personas", allEntries = true)
    public PersonaDTO addPersona(PersonaDTO personaDTO) {
        log.info("INFO - Service AddPersona() ");

      //  DocumentoIdentidad doc=documentoIdentidadRepository.findById(personaDTO.getTipoDocumento()).orElseThrow(()-> new ResourceNotFoundException("Documento de identidad no encontrado"));
        DocumentoIdentidad doc = documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(personaDTO.getTipoDocumento(), 1)
                .orElseThrow(() -> new ResourceNotFoundException("Documento de identidad no encontrado "));

        Set<ConstraintViolation<PersonaDTO>> violations;
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            if(doc != null && doc.getGrupoDocumento().equals(1)){
                if(Objects.equals(doc.getIdDocumentoIdentidad(), 1)) {
                    violations = validator.validate(personaDTO, ValidateDni.class);
                } else {
                    violations = validator.validate(personaDTO, ValidatePasaporte.class);
                }
            } else {
                if(Objects.equals(personaDTO.getTipoDocumento(), 1)) {
                    violations = validator.validate(personaDTO, ValidateDni.class);
                } else {
                    violations = validator.validate(personaDTO, ValidatePasaporte.class);
                }
            }

            if(!violations.isEmpty()) {
                var obj = violations.stream().findFirst().get();
                throw new IllegalArgumentException(obj.getMessage());
            }

            Usuario usuario = util.getUsuario();

            boolean existeNumeroDocumento = iPersonaRepository.existsByNumeroDocumento(personaDTO.getNumeroDocumento());
            if (existeNumeroDocumento) {
                throw new IllegalArgumentException("El numero de documento de identidad ya existe en el sistema.");
            }

            if (iPersonaRepository.existsByCorreo(personaDTO.getCorreo())) {
                throw new IllegalArgumentException("El correo electrónico ya existe.");
            }

            DocumentoIdentidad documentoIdentidad=documentoIdentidadRepository.findById(personaDTO.getTipoDocumento()).orElseThrow(()->new ResourceNotFoundException("Documento no encontrada"));
            Persona persona=modelMapper.map(personaDTO,Persona.class);
            persona.setTipoDocumento(documentoIdentidad);
            persona.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            persona.setUsuarioCreacion(usuario.getUsuario());
            Persona newPersona=iPersonaRepository.save(persona);
            PersonaDTO newPersonaDTO=modelMapper.map(newPersona,PersonaDTO.class);
            return newPersonaDTO;


        } catch (IllegalArgumentException e) {
            log.error("ERROR - Service savePersona() " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (Exception e) {
            throw new RuntimeException("ERROR Service - save Persona() "+e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "personas", allEntries = true)
    public PersonaDTO updatePersona(Integer idPersona, PersonaDTO personaDTO) {

        log.info("INFO - Service UpdatePersona() ");

            DocumentoIdentidad doc = documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(personaDTO.getTipoDocumento(), 1)
                    .orElseThrow(() -> new ResourceNotFoundException("Documento de identidad no encontrado "));

            Set<ConstraintViolation<PersonaDTO>> violations;
            try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = factory.getValidator();
                if(doc != null && doc.getGrupoDocumento().equals(1)){
                    if(Objects.equals(doc.getIdDocumentoIdentidad(), 1)) {
                        violations = validator.validate(personaDTO, ValidateDni.class);
                    } else {
                        violations = validator.validate(personaDTO, ValidatePasaporte.class);
                    }
                } else {
                    if(Objects.equals(personaDTO.getTipoDocumento(), 1)) {
                        violations = validator.validate(personaDTO, ValidateDni.class);
                    } else {
                        violations = validator.validate(personaDTO, ValidatePasaporte.class);
                    }
                }

                if(!violations.isEmpty()) {
                    var obj = violations.stream().findFirst().get();
                    throw new IllegalArgumentException(obj.getMessage());
                }


            Usuario usuario = util.getUsuario();
            boolean existeDocumentoIdentidad = iPersonaRepository.existsByNumeroDocumentoAndIdPersonaNot(personaDTO.getNumeroDocumento(), idPersona);
            if (existeDocumentoIdentidad) {
                throw new IllegalArgumentException("El número de documento identidad ya está registrado en otra Persona");
            }

            boolean existeCorreo = iPersonaRepository.existsByCorreoAndIdPersonaNot(personaDTO.getCorreo(), idPersona);
            if (existeCorreo) {
                throw new IllegalArgumentException("El correo electrónico ya existe.");
            }

            Persona persona=iPersonaRepository.findById(idPersona).orElseThrow( ()-> new RuntimeException("Persona no encontrada") );
            persona.setApellidoMaterno(personaDTO.getApellidoMaterno());
            persona.setNombres(personaDTO.getNombres());
            persona.setApellidoPaterno(personaDTO.getApellidoPaterno());
            //persona.setDocuIdentidad(personaDTO.getTipoDocumento());
            persona.setNumeroDocumento(personaDTO.getNumeroDocumento());
            persona.setCorreo(personaDTO.getCorreo());

            persona.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            persona.setUsuarioActualizacion(usuario.getUsuario());
            Persona personaUpd=iPersonaRepository.save(persona);
            PersonaDTO newPersonaDTO=modelMapper.map(personaUpd,PersonaDTO.class);
            return newPersonaDTO;

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("ERROR - Service updatePersona() - " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (Exception e ){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "personas", allEntries = true)
    public boolean deletePersona(Integer idPersona) {
        log.info("INFO - Service DeletePersona() ");
        var estado=false;
        try {
            Usuario usuario = util.getUsuario();
            var persona=iPersonaRepository.findById(idPersona).orElseThrow(()->new  ResourceNotFoundException("Persona no encontrado"));
            if(persona!=null){
                if(persona.isDeleted()){
                    throw new ResourceNotFoundException("La Persona no existe, ya se encuentra eliminado");
                }
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
