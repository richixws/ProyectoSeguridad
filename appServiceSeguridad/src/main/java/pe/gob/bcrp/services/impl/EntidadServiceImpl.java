package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.DocumentoIdentidadDTO;
import pe.gob.bcrp.dto.entidadDTO.EntidadDTO;
import pe.gob.bcrp.dto.entidadDTO.EntidadFormDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.entities.DocumentoIdentidad;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IDocumentoIdentidadRepository;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.services.IEntidadService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class EntidadServiceImpl implements IEntidadService {


    private final ModelMapper modelMapper;

    private IEntidadRepository entidadRepository;

    private Util util;

    private IDocumentoIdentidadRepository documentoIdentidadRepository;



    @Override
    //@Cacheable(value = "documentosIdentidad")
    public List<DocumentoIdentidadDTO> getAllDocumentos() {

        try {
            log.info("INI - getAllDocumentos");
            List<DocumentoIdentidad> listDocumentos=documentoIdentidadRepository.findByGrupoDocumento(2);;
           // List<DocumentoIdentidad> listDocumentos=documentoIdentidadRepository.findAll();
            return listDocumentos.stream()
                    .map(documento -> modelMapper.map(documento, DocumentoIdentidadDTO.class))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("ERROR - getAllDocumentos() "+e.getMessage());
        }
        return null;

    }

    @Override
    @Cacheable(value = "entidades", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #nombre,#tipoDocumento,#numeroDocumento}")
    public EntidadResponse getAllEntidades(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String nombre,Integer tipoDocumento, String numeroDocumento) {

        log.info("INI Service() - getAllEntidades");

        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                                                                                ? Sort.by(sortBy).ascending()
                                                                                : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Entidad> pageEntidades=null;

            if(nombre!=null || tipoDocumento !=null || numeroDocumento!=null  ){
                pageEntidades=entidadRepository.findByFilters(nombre,tipoDocumento,numeroDocumento,pageDetails);
            }else{
                pageEntidades = entidadRepository.findByIsDeletedFalse(pageDetails);
            }

            List<Entidad> entidades = pageEntidades.getContent();
            /**List<EntidadDTO> entidadDTOS = entidades.stream()
                                                    .map(enti -> modelMapper.map(enti, EntidadDTO.class))
                                                    .toList();
             **/
            List<EntidadFormDTO> entidadDTOS = entidades.stream().map(enti -> {
                EntidadFormDTO entidadDTO = modelMapper.map(enti, EntidadFormDTO.class);
                if (enti.getDocumentoIdentidad() != null) { // Asignar tipoDocumento a partir de DocumentoIdentidad
                    entidadDTO.setTipoDocumento(enti.getDocumentoIdentidad().getTipoDocumentoIdentidad());
                }
                return entidadDTO;
            }).toList();

            EntidadResponse entidadResponse = new EntidadResponse();
            entidadResponse.setContent(entidadDTOS);
            entidadResponse.setPageNumber(pageEntidades.getNumber());
            entidadResponse.setPageSize(pageEntidades.getSize());
            entidadResponse.setTotalElements(pageEntidades.getTotalElements());
            entidadResponse.setTotalPages(pageEntidades.getTotalPages());
            entidadResponse.setLastPage(pageEntidades.isLast());
            return entidadResponse;

        } catch (Exception e) {
            log.error( "ERROR - getAllEntidades() "+e.getMessage() );
            throw new RuntimeException(e);

        }
    }



    @Override
    @CacheEvict(value = "entidades", allEntries = true)
    public EntidadDTO saveEntidad(EntidadDTO entidadDto) {
        log.info("INI - Service() saveEntidad()");
        try {

            DocumentoIdentidad doc = documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(entidadDto.getIdDocumento(), 2)
                    .orElseThrow(() -> new ResourceNotFoundException("Documento de identidad no encontrado"));

            Usuario usuario=util.getUsuario();
            String uuidCodExt = UUID.randomUUID().toString();

            boolean existeNumeroDocumento = entidadRepository.existsByNumeroDocumento(entidadDto.getNumeroDocumento());
            if (existeNumeroDocumento) {
                throw new IllegalArgumentException("El número de documento ya existe en el sistema.");
            }

            Entidad entidad=modelMapper.map(entidadDto,Entidad.class);
            entidad.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            entidad.setUsuarioCreacion(usuario.getUsuario());
            entidad.setCodExterno(uuidCodExt);

            DocumentoIdentidad identidad=new DocumentoIdentidad();
            identidad.setTipoDocumentoIdentidad(doc.getTipoDocumentoIdentidad());
            identidad.setIdDocumentoIdentidad(doc.getIdDocumentoIdentidad());
            entidad.setDocumentoIdentidad(identidad);

            Entidad entidadNew=entidadRepository.save(entidad);
            EntidadDTO entidadDtoNew=modelMapper.map(entidadNew, EntidadDTO.class);
            return entidadDtoNew;

        }catch (ResourceNotFoundException e){
            log.error("ERROR -Service saveEntidad() {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }catch (IllegalArgumentException e){
           throw  new IllegalArgumentException(e.getMessage());
        }catch (Exception e){
            log.error("ERROR - saveEntidad() "+e.getMessage());
            throw new RuntimeException("Error al guardar el sistema " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "entidades", allEntries = true)
    public EntidadDTO updateEntidad(Integer idEntidad, EntidadDTO entidadDto) {
        log.info("INI - Service updateEntidad()");
        try {

            Usuario usuario=util.getUsuario();

            Entidad entidad=entidadRepository.findById(idEntidad).orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));

            DocumentoIdentidad doc = documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(entidadDto.getIdDocumento(), 2)
                    .orElseThrow(() -> new ResourceNotFoundException("Documento de identidad no encontrado "));

            boolean existeNumeroDocumento = entidadRepository.existsByNumeroDocumentoAndIdEntidadNot( entidadDto.getNumeroDocumento(), idEntidad);
            if (existeNumeroDocumento) {
                throw new IllegalArgumentException("El número de documento ya está registrado en otra entidad.");
            }

            if(entidadDto.getCodExterno()==null || entidadDto.getCodExterno().isBlank()){
                throw new IllegalArgumentException("Codigo externo no puede estar vacio.");
            }
            if (!entidadDto.getCodExterno().matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")) {
                throw new IllegalArgumentException("Código debe tener un formato UUID válido");
            }


           // DocumentoIdentidad doc=documentoIdentidadRepository.findById(entidadDto.getIdDocumento()).orElseThrow(()-> new ResourceNotFoundException("Documento de identidad no encontrado"));

            DocumentoIdentidad identidad=new DocumentoIdentidad();
            identidad.setIdDocumentoIdentidad(doc.getIdDocumentoIdentidad());
            identidad.setTipoDocumentoIdentidad(doc.getTipoDocumentoIdentidad());

            entidad.setDocumentoIdentidad(identidad);
            entidad.setNumeroDocumento(entidadDto.getNumeroDocumento());
            entidad.setNombre(entidadDto.getNombre());
            entidad.setSigla(entidadDto.getSigla());
            entidad.setCodExterno(entidadDto.getCodExterno());
            entidad.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            entidad.setUsuarioActualizacion(usuario.getUsuario());

            Entidad entidadNew=entidadRepository.save(entidad);
            EntidadDTO updateEntidad=modelMapper.map(entidadNew,EntidadDTO.class);
            return  updateEntidad;

        } catch (IllegalArgumentException e) {
            log.error("ERROR -Service update Entidad() " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (ResourceNotFoundException e){
            log.error("ERROR -Service updateEntidad() "+e.getMessage());
            throw e;
        }
        catch (Exception e){
            log.error("ERROR - updateSistemas()"+e.getMessage());
            throw new RuntimeException("Error al actualizar el sistema", e);
        }

    }

    @Override
    @CacheEvict(value = "entidades", allEntries = true)
    public boolean deleteEntidad(Integer idEntidad) {

        log.info("INI - deleteEntidad()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Entidad entidad=entidadRepository.findById(idEntidad).orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
            if(entidad!=null){
                if(entidad.isDeleted()){
                    throw new ResourceNotFoundException("La entidad no existe, ya se encuentra eliminado");
                }
                //entidad.setDeleted(true);
                entidad.setEstado(1);
                entidad.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                entidad.setUsuarioEliminacion(usuario.getUsuario());

                entidadRepository.save(entidad);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteEntidad() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }
}
