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
import pe.gob.bcrp.dto.OpcionDTO;
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
    @Cacheable(value = "opciones", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #idSistema,#idModulo}")
    public OpcionResponse getAllOpciones(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema,Integer idModulo) {
        log.info(" INI - Service  getAllOpciones");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Opcion> pageOpciones=null;

            if(idSistema!=null || idModulo!=null) {
                pageOpciones=opcionRepository.findByFilters(idSistema,idModulo,pageDetails);
            }else{
                pageOpciones = opcionRepository.findByIsDeletedFalse(pageDetails);
            }

            List<Opcion> opciones = pageOpciones.getContent();

            List<OpcionDTO> opcionDTOS = opciones.stream().map(opc -> {
                 OpcionDTO opcionDTO = modelMapper.map(opc, OpcionDTO.class);
                 if (opc.getModulo() != null) { // Asignar tipoDocumento a partir de DocumentoIdentidad
                    opcionDTO.setIdModulo(opc.getModulo().getIdModulo());
                    opcionDTO.setIdSistema(opc.getModulo().getSistema().getIdSistema());

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
           Usuario usuario = util.getUsuario();

           Opcion opcion = modelMapper.map(opcionDto, Opcion.class);
           opcion.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
           opcion.setUsuarioCreacion(usuario.getUsuario());

           Sistema sistema=sistemaRepository.findById(opcionDto.getIdSistema()).orElseThrow(()-> new ResourceNotFoundException("no encontrado sistema"));
           Modulo modulo = moduloRepository.findById(opcionDto.getIdModulo()).orElseThrow(()-> new ResourceNotFoundException("no encontrado modulo"));

           modulo.setSistema(sistema);
           opcion.setModulo(modulo);

           Opcion opcionSave= opcionRepository.save(opcion);
           OpcionDTO opcionDtoNew=modelMapper.map(opcionSave, OpcionDTO.class);
           return opcionDtoNew;

       }catch (ResourceNotFoundException e){
           log.error("ERROR - updateEntidad() "+e.getMessage());
           throw e;
       }catch (Exception e) {
           log.error( "ERROR - saveOpcion() "+e.getMessage() );
           throw  new RuntimeException("Error al guardar opcion"+e.getMessage());
       }
    }

    @Override
    @CacheEvict(value = "opciones", allEntries = true)
    public OpcionDTO updateOpcion(OpcionDTO opcionDto, Integer idOpcion) {
        log.info(" INI - Service  updateOpcion");
        try {
            Usuario usuario=util.getUsuario();

            Opcion opcion=opcionRepository.findById(idOpcion).orElseThrow(()-> new ResourceNotFoundException("no encontrado opcion "+idOpcion));

            Modulo modulo=moduloRepository.findById(opcionDto.getIdModulo())
                                          .orElseThrow(()-> new ResourceNotFoundException("no encontrado modulo a actualizar " + opcionDto.getIdModulo()));

            Sistema sistema=sistemaRepository.findById(opcionDto.getIdSistema()).orElseThrow(()-> new ResourceNotFoundException("no encontrado sistema a actualizar"+ opcionDto.getIdSistema()));

            modulo.setSistema(sistema);
            opcion.setModulo(modulo);
            opcion.setNombreOpcion(opcionDto.getNombreOpcion());
            opcion.setUrl(opcionDto.getUrl());

            opcion.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            opcion.setUsuarioActualizacion(usuario.getUsuario());

            Opcion opcionSave= opcionRepository.save(opcion);
            OpcionDTO opcionDtoUpd=modelMapper.map(opcionSave, OpcionDTO.class);
            return opcionDtoUpd;

        }catch (ResourceNotFoundException e){
            log.error("ERROR - updateEntidad() "+e.getMessage());
            throw e;
        }catch (Exception e) {
            log.error( "ERROR - updateOpcion() "+e.getMessage() );
            throw new RuntimeException("Error al actualizar opcion"+e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "opciones", allEntries = true)
    public boolean deleteOpcion(Integer idOpcion) {

        log.info("INI - deleteOpcion()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Opcion opcion=opcionRepository.findById(idOpcion).orElseThrow(() -> new ResourceNotFoundException("Opcion no encontrado con "+ idOpcion));
            if(opcion!=null){
                if(opcion.isDeleted()){
                    throw new ResourceNotFoundException("La Opción no existe, ya se encuentra eliminado");
                }
                opcion.setDeleted(true);
                opcion.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                opcion.setUsuarioEliminacion(usuario.getUsuario());

                opcionRepository.save(opcion);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteOpcion() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }


}
