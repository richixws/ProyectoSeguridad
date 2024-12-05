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
import pe.gob.bcrp.dto.areaDTO.AreaDTO;
import pe.gob.bcrp.dto.areaDTO.AreaFormDTO;
import pe.gob.bcrp.dto.response.AreaResponse;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IAreaRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IAreaService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class AreaServiceImpl implements IAreaService {

    private final ModelMapper modelMapper;

    private IAreaRepository areaRepository;

    private ISistemaRepository isistemaRepository;

    private Util util;

    @Override
    @Cacheable(value = "areas", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #nombre}")
    public AreaResponse getAllAreas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre) {
        log.info("INI Service() - getAllAreas");
        try {
            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Area> pageAreas=null;

            if(nombre != null){
                pageAreas = areaRepository.findByFilters(nombre, pageDetails);
            }else{
                pageAreas = areaRepository.findAll(pageDetails);
            }

            List<Area> areas = pageAreas.getContent();
            List<AreaFormDTO> entidadDTOS = areas.stream().map(enti -> {
                Sistema sistema =  enti.getSistema();
                AreaFormDTO area =  modelMapper.map(enti, AreaFormDTO.class);
                area.setIdSistema(sistema.getIdSistema());
                return area;
            }).toList();

            AreaResponse areaResponse = new AreaResponse();
            areaResponse.setContent(entidadDTOS);
            areaResponse.setPageNumber(pageAreas.getNumber());
            areaResponse.setPageSize(pageAreas.getSize());
            areaResponse.setTotalElements(pageAreas.getTotalElements());
            areaResponse.setTotalPages(pageAreas.getTotalPages());
            areaResponse.setLastPage(pageAreas.isLast());
            return areaResponse;

        } catch (Exception e) {
            log.error( "ERROR - getAllAreas() "+e.getMessage() );
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "areas", allEntries = true)
    public AreaDTO saveArea(AreaDTO areaDto) {
        try {
            log.info("INI - saveModulo()");
            Usuario usuario = util.getUsuario();

            Optional<Area> exist = areaRepository.findByNombreAreaContainingIgnoreCase(areaDto.getNombreArea());
            if (exist.isPresent()){
                throw new IllegalArgumentException("El nombre del area se encuentra en uso, por favor ingrese un nuevo area.");
            }

            Area area = modelMapper.map(areaDto, Area.class);
            area.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            area.setUsuarioCreacion(usuario.getUsuario());

            Area areaNew = areaRepository.save(area);
            return modelMapper.map(areaNew, AreaDTO.class);

        } catch (IllegalArgumentException e) {
            log.error("ERROR - Service saveArea() {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());

        } catch (Exception e){
            log.error("ERROR -Service saveArea() {}", e.getMessage());
            throw new RuntimeException("Error al guardar el area" + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "areas", allEntries = true)
    public AreaDTO updateArea(Integer id, AreaDTO areaDto) {
        log.info("INI - Service updateArea()");
        try {
            Usuario usuario = util.getUsuario();

            Area area = areaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Modulo a actualizar no encontrado: " + id));

            boolean exist = areaRepository.existsByNombreAreaIgnoreCaseAndIdAreaNot(areaDto.getNombreArea(), id);
            if (exist) {
                throw new IllegalArgumentException("El Nombre del area ya está registrado en otro Sistema.");
            }

            Sistema sistema = isistemaRepository.findById(areaDto.getIdSistema()).orElseThrow(()->new ResourceNotFoundException(" Sistema a actualizar no encontrado."));

            area.setSistema(sistema);
            area.setNombreArea(areaDto.getNombreArea());
            area.setEstado(areaDto.getEstado());
            area.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            area.setUsuarioActualizacion(usuario.getUsuario());

            Area areaNew = areaRepository.save(area);
            return modelMapper.map(areaNew, AreaDTO.class);

        } catch (IllegalArgumentException e) {
            log.error("ERROR - updateArea() - {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (ResourceNotFoundException e){
            log.error("ERROR - updateArea {}", e.getMessage());
            throw e;
        }
        catch (Exception e){
            log.error("ERROR - updateArea(){}", e.getMessage());
            throw new RuntimeException("Error al actualizar el area", e);
        }
    }

    @Override
    @CacheEvict(value = "areas", allEntries = true)
    public boolean deleteArea(Integer idArea) {
        log.info("INI - deleteArea()");
        boolean estado = false;
        try {
            Usuario usuario = util.getUsuario();

            Area area = areaRepository.findById(idArea).orElseThrow(() -> new ResourceNotFoundException("Area no encontrado"));
            if(area!=null){
                if(area.isDeleted()){
                    throw new ResourceNotFoundException("El area no existe, ya se encuentra eliminado");
                }

                area.setDeleted(true);
                area.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                area.setUsuarioEliminacion(usuario.getUsuario());

                areaRepository.save(area);
                estado = true;
            }
        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteArea() "+e.getMessage());
            e.printStackTrace();
        }
        return estado;
    }

}
