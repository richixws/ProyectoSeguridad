package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.ModuloDTO;
import pe.gob.bcrp.dto.response.ModuloResponse;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IModuloRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IModuloService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class ModuloServiceImpl implements IModuloService {

    private final ModelMapper modelMapper;
    private final Util util;
    private IModuloRepository imoduloRepository;
    private ISistemaRepository isistemaRepository;


    @Override
    public ModuloResponse getAllModulos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema) { //, String nombre

        log.info(" INI - Service  getAllModulos");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                                                : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Modulo> pageModulos=null;

             if(idSistema!=null){
                 pageModulos=imoduloRepository.findByFilters(idSistema,pageDetails);
             }else{
                 pageModulos = imoduloRepository.findByIsDeletedFalse(pageDetails);
            }

            List<Modulo> modulo = pageModulos.getContent();
            List<ModuloDTO> moduloDTOS = modulo.stream()
                                                 .map(mod -> modelMapper.map(mod, ModuloDTO.class))
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
    public ModuloDTO saveModulo(ModuloDTO moduloDto) {

        try {
            log.info("INI - saveModulo()");
            Usuario usuario = util.getUsuario();

            Modulo modulo = modelMapper.map(moduloDto, Modulo.class);
            modulo.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            modulo.setUsuarioCreacion(usuario.getUsuario());

            modulo.setOrderDate(new Date());

            Modulo moduloNew = imoduloRepository.save(modulo);
            ModuloDTO moduloDtoNew = modelMapper.map(moduloNew, ModuloDTO.class);
            return moduloDtoNew;

        }catch (Exception e){
            log.error("ERROR - saveModulo() "+e.getMessage());
            throw new RuntimeException("Error al guardar el modulo" + e.getMessage());
        }
    }


    @Override
    public ModuloDTO updateModulo(ModuloDTO moduloDto, Integer idModulo) {

        log.info("INI - Service updateModulo()");
        try {

            Usuario usuario=util.getUsuario();

            Modulo modulo=imoduloRepository.findById(idModulo).orElseThrow(() -> new ResourceNotFoundException("Modulo a actualizar no encontrado :" + idModulo));

            Sistema sistema=isistemaRepository.findById(moduloDto.getIdSistema()).orElseThrow(()->new ResourceNotFoundException(" Sistema no encontrando "));

            modulo.setSistema(sistema);
            modulo.setNombreModulo(moduloDto.getNombreModulo());
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
            throw e;
        }
        catch (Exception e){
            log.error("ERROR - updateSistemas(){}", e.getMessage());
            throw new RuntimeException("Error al actualizar el sistema", e);
        }



    }

    @Override
    public boolean deleteModulo(Integer idModulo) {

        log.info("INI - delete Modulo()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Modulo modulo=imoduloRepository.findById(idModulo).orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
            if(modulo!=null){
                //entidadRepository.deleteById(id);
                modulo.setDeleted(true);
                modulo.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                modulo.setUsuarioEliminacion(usuario.getUsuario());

                imoduloRepository.save(modulo);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - delete Modulo() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;

    }
}
