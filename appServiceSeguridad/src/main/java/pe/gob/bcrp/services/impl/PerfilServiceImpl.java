package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.PerfilDTO;
import pe.gob.bcrp.dto.RegistroPerfilDTO;
import pe.gob.bcrp.dto.response.PerfilResponse;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.repositories.IPerfilRepository;
import pe.gob.bcrp.repositories.IRolRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IPerfilService;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class PerfilServiceImpl implements IPerfilService {

    private final ModelMapper modelMapper;
    private final Util util;
    private IPerfilRepository perfilRepository;
    private ISistemaRepository sistemaRepository;
    private IRolRepository rolRepository;
    private IEntidadRepository entidadRepository;



    @Override
    public PerfilResponse getAllPerfiles(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema, Integer idPerfil) {

        log.info(" INI - Service  getAllPerfiles");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                                                : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Perfil> pagePerfiles=null;

            if(idSistema!=null || idPerfil!=null) {
                pagePerfiles=perfilRepository.findByFilters(idSistema,idPerfil,pageDetails);
            }else{
                pagePerfiles = perfilRepository.findByIsDeletedFalse(pageDetails);
            }

            List<Perfil> perfiles = pagePerfiles.getContent();

            List<RegistroPerfilDTO> perfilDTOS = perfiles.stream().map(p -> {
                RegistroPerfilDTO perfilDTO = modelMapper.map(p, RegistroPerfilDTO.class);
                if (p.getRol() != null) {
                    perfilDTO.setIdSistema(p.getRol().getSistema().getIdSistema());
                    perfilDTO.setNombrePerfil(p.getNombre());
                    perfilDTO.setNombreSistema(p.getRol().getSistema().getNombre());

                }
                return perfilDTO;
            }).toList();

            PerfilResponse perfilResponse = new PerfilResponse();
            perfilResponse.setContent(perfilDTOS);
            perfilResponse.setPageNumber(pagePerfiles.getNumber());
            perfilResponse.setPageSize(pagePerfiles.getSize());
            perfilResponse.setTotalElements(pagePerfiles.getTotalElements());
            perfilResponse.setTotalPages(pagePerfiles.getTotalPages());
            perfilResponse.setLastPage(pagePerfiles.isLast());
            return perfilResponse;

        }catch (Exception e) {
            log.error("ERROR - Service  getAllPerfiles() {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public PerfilDTO savePerfil(PerfilDTO perfilDTO) {

        log.info(" INI - Service  savePerfil");
        try {
            Usuario usuario = util.getUsuario();

            Perfil perfil = modelMapper.map(perfilDTO, Perfil.class);
            perfil.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            perfil.setUsuarioCreacion(usuario.getUsuario());

            //Sistema sistema=sistemaRepository.findById(opcionDto.getIdSistema()).orElseThrow(()-> new ResourceNotFoundException("no encontrado sistema"));
            //Modulo modulo = moduloRepository.findById(opcionDto.getIdModulo()).orElseThrow(()-> new ResourceNotFoundException("no encontrado modulo"));
           Rol rol= rolRepository.findById(perfilDTO.getIdRol()).orElseThrow(()-> new ResourceNotFoundException("Rol no encontrado"));
           Sistema sistema=sistemaRepository.findById(perfilDTO.getIdSistema()).orElseThrow(()-> new ResourceNotFoundException("Sistema no encontrado"));
           Entidad entidad=entidadRepository.findById(perfilDTO.getIdEntidad()).orElseThrow(()-> new ResourceNotFoundException("Entidad no encontrado"));
           rol.setSistema(sistema);
           perfil.setRol(rol);
           perfil.setEntidad(entidad);


           Perfil perfilSave= perfilRepository.save(perfil);
           PerfilDTO perfilDtoNew=modelMapper.map(perfilSave, PerfilDTO.class);
           return perfilDtoNew;

        }catch (ResourceNotFoundException e){
            log.error("ERROR -Service save Perfil() "+e.getMessage());
            throw e;
        }catch (Exception e) {
            log.error( "ERROR -Service savePerfil() "+e.getMessage() );
            throw  new RuntimeException("Error al guardar perfil"+e.getMessage());
        }


    }

    @Override
    public PerfilDTO updatePerfil(PerfilDTO perfilDTO, Integer idPerfil) {
        log.info(" INI - Service  updatePerfil");
        try {
            Usuario usuario=util.getUsuario();

            Perfil perfil=perfilRepository.findById(idPerfil).orElseThrow(()-> new ResourceNotFoundException("Perfil no encontrado"+idPerfil));

            //Modulo modulo=moduloRepository.findById(opcionDto.getIdModulo())
            //        .orElseThrow(()-> new ResourceNotFoundException("no encontrado modulo a actualizar " + opcionDto.getIdModulo()));

            Rol rol =rolRepository.findById(perfilDTO.getIdRol()).orElseThrow(()-> new ResourceNotFoundException("no encontrado rol "+perfilDTO.getIdRol()));
            Sistema sistema=sistemaRepository.findById(perfilDTO.getIdSistema()).orElseThrow(()-> new ResourceNotFoundException("no encontrado sistema a actualizar"+ perfilDTO.getIdSistema()));
            Entidad entidad=entidadRepository.findById(perfilDTO.getIdEntidad()).orElseThrow(()-> new ResourceNotFoundException("no encontrado entidad"));

            rol.setSistema(sistema);
            perfil.setRol(rol);
            perfil.setEntidad(entidad);
            perfil.setNombre(perfilDTO.getNombrePerfil());


            perfil.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            perfil.setUsuarioActualizacion(usuario.getUsuario());


            Perfil perfilSave= perfilRepository.save(perfil);
            PerfilDTO perfilDtoUpd=modelMapper.map(perfilSave, PerfilDTO.class);
            return perfilDtoUpd;

        }catch (ResourceNotFoundException e){
            log.error("ERROR -Service update Perfil() "+e.getMessage());
            throw e;
        }catch (Exception e) {
            log.error( "ERROR -Service updatePerfil() "+e.getMessage() );
            throw new RuntimeException("Error al actualizar perfil"+e.getMessage());
        }

    }

    @Override
    public boolean deletePerfil(Integer idPerfil) {

        log.info("INI - deleteOpcion()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Perfil perfil=perfilRepository.findById(idPerfil).orElseThrow(() -> new ResourceNotFoundException("Opcion no encontrado con "+ idPerfil));
            if(perfil!=null){

                perfil.setDeleted(true);
                perfil.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                perfil.setUsuarioEliminacion(usuario.getUsuario());

                perfilRepository.save(perfil);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - Service deletePerfil() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }
}
