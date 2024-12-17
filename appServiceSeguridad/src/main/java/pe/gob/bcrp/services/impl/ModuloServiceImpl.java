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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.moduloDTO.ModuloDTO;
import pe.gob.bcrp.dto.moduloDTO.ModuloFormDTO;
import pe.gob.bcrp.dto.response.ModuloResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IModuloRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IModuloService;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
@AllArgsConstructor
public class ModuloServiceImpl implements IModuloService {

    private final ModelMapper modelMapper;
    private final Util util;
    private IModuloRepository imoduloRepository;
    private ISistemaRepository isistemaRepository;


    @Override
    @Cacheable(value = "modulos", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #idSistema, #name}")
    public ModuloResponse getAllModulos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
                                        Integer idSistema, String name) {

        log.info(" INI - Service  getAllModulos");
        try {

            Map<String, String> sortByMapping = Map.of(
                    "moduleName", "nombreModulo",
                    "moduleId",   "idModulo",
                    "systemId",   "sistema.idSistema",
                    "state",      "estado"
            );

            String entitySortBy = sortByMapping.getOrDefault(sortBy, sortBy);

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(entitySortBy).ascending()
                                                                                : Sort.by(entitySortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Modulo> pageModulos=null;

             if(idSistema!=null || name != null){
                 String nombreLowerCase = name != null ? name.toLowerCase() : null;
                 pageModulos=imoduloRepository.findByFilters(idSistema, nombreLowerCase, pageDetails);
             }else{
                 pageModulos = imoduloRepository.findAll(pageDetails);
            }

            List<Modulo> modulo = pageModulos.getContent();
            List<ModuloFormDTO> moduloDTOS = modulo.stream()
                                                 .map(mod -> modelMapper.map(mod, ModuloFormDTO.class))
                                                 .toList();

            ModuloResponse moduloResponse = new ModuloResponse();
            moduloResponse.setContent(moduloDTOS);
            moduloResponse.setPageNumber(pageModulos.getNumber());
            moduloResponse.setPageSize(pageModulos.getSize());
            moduloResponse.setTotalElements(pageModulos.getTotalElements());
            moduloResponse.setTotalPages(pageModulos.getTotalPages());
            moduloResponse.setLastPage(pageModulos.isLast());
            return moduloResponse;

        }catch (Exception e) {
            log.error( "ERROR - getAllModulos() "+e.getMessage() );
            throw new RuntimeException(e);
        }
    }


    @Override
    @CacheEvict(value = "modulos", allEntries = true)
    public ModuloDTO saveModulo(ModuloDTO moduloDto) {

        try {
            log.info("INI - saveModulo()");
            Usuario usuario = util.getUsuario();

            Sistema sistema=isistemaRepository.findById(moduloDto.getIdSistema()).orElseThrow(()->new ResourceNotFoundException("El sistema del modulo no existe."));

          /** Optional<Modulo> moduloExistente = imoduloRepository.findByNombreModuloContainingIgnoreCase(moduloDto.getNombreModulo());
            if (moduloExistente.isPresent()){
                throw new IllegalArgumentException("El nombre del modulo se encuentra en uso, por favor ingrese un nuevo modulo.");
            }**/

            if(moduloDto.getEstado()==null){
                moduloDto.setEstado(1);
            }

            Modulo modulo = modelMapper.map(moduloDto, Modulo.class);
            modulo.setSistema(sistema);
            modulo.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            modulo.setUsuarioCreacion(usuario.getUsuario());

            //modulo.setOrderDate(new Date());
            modulo.setOrderDate(java.sql.Date.valueOf(LocalDate.now()));

            Modulo moduloNew = imoduloRepository.save(modulo);
            ModuloDTO moduloDtoNew = modelMapper.map(moduloNew, ModuloDTO.class);
            return moduloDtoNew;

        }catch (ResourceNotFoundException e){
            log.error("ERROR  Service saveModulo() {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("ERROR - Service saveModulo() {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e){
            log.error("ERROR -Service saveModulo() {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @CacheEvict(value = "modulos", allEntries = true)
    public ModuloDTO updateModulo(ModuloDTO moduloDto, Integer idModulo) {

        log.info("INI - Service updateModulo()");
        try {

            Usuario usuario=util.getUsuario();

            Modulo modulo=imoduloRepository.findById(idModulo).orElseThrow(() -> new ResourceNotFoundException("Modulo a actualizar no existe."));

            /**boolean existeNombredeModulo=imoduloRepository.existsByNombreModuloIgnoreCaseAndAndIdModuloNot(moduloDto.getNombreModulo(),idModulo);
            if (existeNombredeModulo) {
                throw new IllegalArgumentException("El Nombre del Modulo ya está registrado en otro Sistema.");
            }**/

            Sistema sistema=isistemaRepository.findById(moduloDto.getIdSistema()).orElseThrow(()->new ResourceNotFoundException("El sistema del modulo a actualizar no existe."));

            modulo.setSistema(sistema);
            modulo.setNombreModulo(moduloDto.getNombreModulo());
            modulo.setEstado(moduloDto.getEstado());
            modulo.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            modulo.setUsuarioActualizacion(usuario.getUsuario());

            Modulo moduloNew=imoduloRepository.save(modulo);
            ModuloDTO moduloUpdt=modelMapper.map(moduloNew,ModuloDTO.class);
            return  moduloUpdt;

        } catch (IllegalArgumentException e) {
            log.error("ERROR - updateModulo() - {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());

        }catch (ResourceNotFoundException e){
            log.error("ERROR - updateModulo {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }
        catch (Exception e){
            log.error("ERROR - updateSistemas(){}", e.getMessage());
            throw new RuntimeException("Error al actualizar el sistema", e);
        }

    }

    @Override
    @CacheEvict(value = "modulos", allEntries = true)
    public boolean deleteModulo(Integer idModulo) {

        log.info("INI - delete Modulo()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Modulo modulo=imoduloRepository.findById(idModulo).orElseThrow(() -> new ResourceNotFoundException("Modulo a eliminar no existe."));
            if(modulo!=null){
                if(modulo.isDeleted()){
                    throw new ResourceNotFoundException("El modulo no existe, ya se encuentra eliminado.");
                }
                //entidadRepository.deleteById(id);
                modulo.setDeleted(true);
                modulo.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                modulo.setUsuarioEliminacion(usuario.getUsuario());

                imoduloRepository.save(modulo);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - delete Modulo() "+e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
          //  estado=false;
        }
        return estado;

    }
}
