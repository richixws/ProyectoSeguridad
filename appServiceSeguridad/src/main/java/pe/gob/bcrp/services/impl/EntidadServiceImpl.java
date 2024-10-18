package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.dto.EntidadResponse;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.services.IEntidadService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class EntidadServiceImpl implements IEntidadService {


    private final ModelMapper modelMapper;

    private IEntidadRepository entidadRepository;

    private Util util;

    @Override
    public List<EntidadDTO> getEntidades() {

        try {
            log.info("INI - getEntidades");
            List<Entidad> listEntidades=entidadRepository.findByIsDeletedFalse();
            return listEntidades.stream()
                    .map(entidad -> modelMapper.map(entidad, EntidadDTO.class))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("ERROR - getEntidades() "+e.getMessage());
        }
        return null;
    }

    @Override
    public EntidadResponse getAllEntidades(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        log.info("INI Service() - getAllProducts");

        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                                                                                ? Sort.by(sortBy).ascending()
                                                                                : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Entidad> pageEntidades = entidadRepository.findByIsDeletedFalse(pageDetails);

            List<Entidad> entidades = pageEntidades.getContent();

            List<EntidadDTO> entidadDTOS = entidades.stream()
                                                    .map(enti -> modelMapper.map(enti, EntidadDTO.class))
                                                    .toList();

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
    public EntidadDTO findEntidadByNombre(String nombre) {
        try {
            log.info("INI - findEntidadByNombre()");
           Entidad entidad=entidadRepository.findByNombre(nombre).orElseThrow(()-> new ResourceNotFoundException("Entidad no encontrado con nombre"+nombre));
            EntidadDTO dto=modelMapper.map(entidad, EntidadDTO.class);
            return dto;
        }catch (ResourceNotFoundException e){
            log.error("ERROR - getEntidadByNombre() "+e.getMessage());
        }
        return null;
    }

    @Override
    public EntidadDTO saveEntidad(EntidadDTO entidadDto) {
        try {
            log.info("INI - saveEntidad()");
            Usuario usuario=util.getUsuario();

            Entidad entidad=modelMapper.map(entidadDto,Entidad.class);
            entidad.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            entidad.setUsuarioCreacion(usuario.getUsuario());
            Entidad entidadNew=entidadRepository.save(entidad);
            EntidadDTO entidadDtoNew=modelMapper.map(entidadNew, EntidadDTO.class);
            return entidadDtoNew;

        }catch (Exception e){
            log.error("ERROR - saveEntidad() "+e.getMessage());
        }
        return null;
    }

    @Override
    public EntidadDTO updateEntidad(Integer id, EntidadDTO entidadDto) {

        try {
            log.info("INI - updateUsuario()");
            Usuario usuario=util.getUsuario();

            Entidad entidad=entidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado con id :" + entidadDto.getIdEntidad()));
            entidad.setNombre(entidadDto.getNombre());
            entidad.setTipoDocumento(entidadDto.getTipoDocumento());
            entidad.setNumeroDocumento(entidadDto.getNumeroDocumento());
            entidad.setSigla(entidadDto.getSigla());
            entidad.setCodExterno(entidadDto.getCodExterno());

            entidad.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            entidad.setUsuarioActualizacion(usuario.getUsuario());
            Entidad entidadNew=entidadRepository.save(entidad);
            EntidadDTO updateEntidad=modelMapper.map(entidadNew,EntidadDTO.class);
            return  updateEntidad;

        }catch (ResourceNotFoundException e){
            log.error("ERROR - updateEntidad() "+e.getMessage());
            throw e;
        }
        catch (Exception e){
            log.error("ERROR - updateSistemas()"+e.getMessage());
            throw new RuntimeException("Error al actualizar el sistema", e);
        }

    }

    @Override
    public boolean deleteEntidad(Integer id) {

        log.info("INI - deleteEntidad()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Entidad entidad=entidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entidad con no encontrado"));
            if(entidad!=null){
                //entidadRepository.deleteById(id);
                entidad.setDeleted(true);
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
