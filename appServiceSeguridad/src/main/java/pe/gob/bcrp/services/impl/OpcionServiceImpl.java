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
import pe.gob.bcrp.dto.opcionDTO.OpcionDTO;
import pe.gob.bcrp.dto.opcionDTO.opcionFormDTO;
import pe.gob.bcrp.dto.response.OpcionResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IModuloRepository;
import pe.gob.bcrp.repositories.IOpcionRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IOpcionService;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.entities.Opcion;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
@AllArgsConstructor
public class OpcionServiceImpl  implements IOpcionService {

    private final ModelMapper modelMapper;
    private final Util util;
    private final IOpcionRepository opcionRepository;
    private final IModuloRepository moduloRepository;
    private final ISistemaRepository sistemaRepository;


    @Override
    @Cacheable(value = "opciones", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #idSistema,#idModulo, #name}")
    public OpcionResponse getAllOpciones(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
                                         Integer idSistema,Integer idModulo, String name) {
        log.info(" INI - Service  getAllOpciones");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Opcion> pageOpciones=null;

            if(idSistema!=null || idModulo!=null || name != null) {
                String nombreLowerCase = name != null ? name.toLowerCase() : null;
                pageOpciones=opcionRepository.findByFilters(idSistema,idModulo, nombreLowerCase, pageDetails);
            }else{
                pageOpciones = opcionRepository.findAll(pageDetails);
            }

            List<Opcion> opciones = pageOpciones.getContent();

            List<opcionFormDTO> opcionDTOS = opciones.stream().map(opc -> {
                opcionFormDTO opcionDTO = modelMapper.map(opc, opcionFormDTO.class);
                 if (opc.getModulo() != null) { // Asignar tipoDocumento a partir de DocumentoIdentidad
                    opcionDTO.setIdModulo(opc.getModulo().getIdModulo());
                    opcionDTO.setIdSistema(opc.getModulo().getSistema().getIdSystem());

                 }
                 return opcionDTO;
             }).toList();

            OpcionResponse opcionResponse = new OpcionResponse();
            opcionResponse.setContent(opcionDTOS);
            opcionResponse.setPageNumber(pageOpciones.getNumber());
            opcionResponse.setPageSize(pageOpciones.getSize());
            opcionResponse.setTotalElements(pageOpciones.getTotalElements());
            opcionResponse.setTotalPages(pageOpciones.getTotalPages());
            opcionResponse.setLastPage(pageOpciones.isLast());
            return opcionResponse;

        }catch (Exception e) {
            log.error( "ERROR - getAllModulos() "+e.getMessage() );
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "opciones", allEntries = true)
    public OpcionDTO saveOpcion(OpcionDTO opcionDto) {
       log.info(" INI - Service  saveOpcion");
       try {

           Optional<Opcion> exist = opcionRepository.findFirstByNombreOpcionContainingIgnoreCase(
                   opcionDto.getNombreOpcion());
           if(exist.isPresent()) {
               throw new ResourceNotFoundException("El nombre de la opción ya esta registrado en otra opcion.");
           }

           if(opcionDto.getEstado()==null){
               opcionDto.setEstado(1);
           }

           Usuario usuario = util.getUsuario();

           Opcion opcion = modelMapper.map(opcionDto, Opcion.class);
           opcion.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
           opcion.setUsuarioCreacion(usuario.getUsuario());

           Sistema sistema=sistemaRepository.findById(opcionDto.getIdSistema()).orElseThrow(()-> new IllegalArgumentException("El sistema a guardar de la opcion no existe."));
           Modulo modulo = moduloRepository.findById(opcionDto.getIdModulo()).orElseThrow(()-> new IllegalArgumentException("El modulo a guardar de la opcion no existe."));

           modulo.setSistema(sistema);
           opcion.setModulo(modulo);

           Opcion opcionSave= opcionRepository.save(opcion);
           OpcionDTO opcionDtoNew=modelMapper.map(opcionSave, OpcionDTO.class);
           return opcionDtoNew;

       }catch (ResourceNotFoundException e){
           log.error("ERROR - save Opcion() {}", e.getMessage());
           throw new ResourceNotFoundException(e.getMessage());
       }  catch (IllegalArgumentException e) {
           log.error("ERROR save update () - {}", e.getMessage());
           throw new IllegalArgumentException(e.getMessage());
       }catch (Exception e) {
           log.error("ERROR - saveOpcion() {}", e.getMessage());
           throw  new RuntimeException(e.getMessage());
       }
    }

    @Override
    @CacheEvict(value = "opciones", allEntries = true)
    public OpcionDTO updateOpcion(OpcionDTO opcionDto, Integer idOpcion) {
        log.info(" INI - Service  updateOpcion");
        try {
            Usuario usuario=util.getUsuario();

            Opcion opcion=opcionRepository.findById(idOpcion).orElseThrow(()-> new ResourceNotFoundException("Opción a actualizar no existe."));

            boolean existeNombredeModulo=opcionRepository.existsByNombreOpcionIgnoreCaseAndAndIdOpcionNot(opcionDto.getNombreOpcion(),idOpcion);
            if (existeNombredeModulo) {
                throw new ResourceNotFoundException("El nombre de opcion ya está registrado en otra opcion.");
            }

            Modulo modulo=moduloRepository.findById(opcionDto.getIdModulo())
                                          .orElseThrow(()-> new IllegalArgumentException("El modulo a actualizar de la opcion no existe."));

            Sistema sistema=sistemaRepository.findById(opcionDto.getIdSistema()).orElseThrow(()-> new IllegalArgumentException("El sistema a actualizar de la opcion no existe."));

            modulo.setSistema(sistema);
            opcion.setModulo(modulo);
            opcion.setNombreOpcion(opcionDto.getNombreOpcion());
            opcion.setUrl(opcionDto.getUrl());
            opcion.setEstado(opcionDto.getEstado());

            opcion.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            opcion.setUsuarioActualizacion(usuario.getUsuario());

            Opcion opcionSave= opcionRepository.save(opcion);
            OpcionDTO opcionDtoUpd=modelMapper.map(opcionSave, OpcionDTO.class);
            return opcionDtoUpd;

        }  catch (IllegalArgumentException e) {
            log.error("ERROR service update Opcion() - {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (ResourceNotFoundException e){
            log.error("ERROR - update Opcion() {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }catch (Exception e) {
            log.error("ERROR - updateOpcion() {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "opciones", allEntries = true)
    public boolean deleteOpcion(Integer idOpcion) {

        log.info("INI - deleteOpcion()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Opcion opcion=opcionRepository.findById(idOpcion).orElseThrow(() -> new ResourceNotFoundException("Opcion a eliminar no existe."));
            if(opcion!=null){
                if(opcion.isDeleted()){
                    throw new ResourceNotFoundException("La opción no existe, ya se encuentra eliminado.");
                }
                opcion.setDeleted(true);;
                opcion.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                opcion.setUsuarioEliminacion(usuario.getUsuario());

                opcionRepository.save(opcion);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteOpcion() {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
            //estado=false;
        }
        return estado;
    }


}
