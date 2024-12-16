package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.RolDTO;
import pe.gob.bcrp.dto.RolFormDTO;
import pe.gob.bcrp.dto.response.RolResponse;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.entities.Rol;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IRolRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IRolService;
import pe.gob.bcrp.util.Util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class RolServiceImpl implements IRolService {

    private final ModelMapper modelMapper;
    private final Util util;
    private IRolRepository rolRepository;
    private ISistemaRepository sistemaRepository;

     @Override
     @Cacheable(value = "roles", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #idSistema, #idRol, #name}")
     public RolResponse getAllRoles(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,
                                    Integer idSistema, Integer idRol, String name) {

        log.info(" INI - Service  getAllRoles");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                                                : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Rol> pageRol=null;

            if(idSistema!=null || name != null) {
                String nombreLowerCase = name != null ? name.toLowerCase() : null;
                pageRol=rolRepository.findByFilters(idSistema,idRol, nombreLowerCase, pageDetails);
            }else{
                pageRol = rolRepository.findAll(pageDetails);
            }

            List<Rol> roles = pageRol.getContent();

            /** List<OpcionDTO> opcionDTOS = opciones.stream()
             .map(opc -> modelMapper.map(opc, OpcionDTO.class))
             .toList();**/

            List<RolDTO> opcionDTOS = roles.stream().map(rol -> {
                RolDTO rolDTO = modelMapper.map(rol, RolDTO.class);
                if (rol.getSistema() != null) { // Asignar tipoDocumento a partir de DocumentoIdentidad
                    rolDTO.setIdSistema(rol.getSistema().getIdSistema());
                    rolDTO.setNombreSistema(rol.getSistema().getNombre());

                }
                return rolDTO;
            }).toList();

            RolResponse rolResponse = new RolResponse();
            rolResponse.setContent(opcionDTOS);
            rolResponse.setPageNumber(pageRol.getNumber());
            rolResponse.setPageSize(pageRol.getSize());
            rolResponse.setTotalElements(pageRol.getTotalElements());
            rolResponse.setTotalPages(pageRol.getTotalPages());
            rolResponse.setLastPage(pageRol.isLast());
            return rolResponse;

        }catch (Exception e) {
            log.error( "ERROR - getAllModulos() "+e.getMessage() );
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RolFormDTO saveRole(RolFormDTO rolDTO) {
        log.info(" INI - Service  saveRole");
        try {
            Usuario usuario = util.getUsuario();

            Optional<Rol> moduloExistente = rolRepository.findFirstByNombreContainingIgnoreCase(rolDTO.getNombreRol());
            if (moduloExistente.isPresent()){
                throw new IllegalArgumentException("El nombre del rol se encuentra en uso, por favor ingrese un nuevo rol.");
            }

            Rol rol = modelMapper.map(rolDTO, Rol.class);
            rol.setEstado(1);
            rol.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            rol.setUsuarioCreacion(usuario.getUsuario());

            Sistema sistema=sistemaRepository.findById(rolDTO.getIdSistema()).orElseThrow(()-> new ResourceNotFoundException("Sistema no encontrado"));
            rol.setSistema(sistema);

            Rol rolSave=rolRepository.save(rol);
            RolFormDTO rolFormDTO = modelMapper.map(rol, RolFormDTO.class);
            return rolFormDTO;

        }catch (IllegalArgumentException e) {
            log.error("ERROR - service saveRole {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (ResourceNotFoundException e) {
            log.error("ERROR - service save Role {}", e.getMessage());
             throw e;
        }catch (Exception e) {
            log.error( "ERROR -service saveRole "+e.getMessage() );
            throw new RuntimeException("Error al guardar rol "+e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RolFormDTO updateRole(RolFormDTO rolDto, Integer idRol) {
        log.info(" INI - Service  updateRole");
        try {
            Usuario usuario = util.getUsuario();
            Rol rol = rolRepository.findById(idRol).orElseThrow(() -> new ResourceNotFoundException(" Rol a actualizar no encontrado"));

            boolean existeNombredeModulo=rolRepository.existsByNombreIgnoreCaseAndAndIdRolNot(rolDto.getNombreRol(),idRol);
            if (existeNombredeModulo) {
                throw new IllegalArgumentException("El Nombre del rol ya se encuentra registrado en otro rol.");
            }

            Sistema sistema = sistemaRepository.findById(rolDto.getIdSistema()).orElseThrow(() -> new ResourceNotFoundException(" Sistema a actualizar no encontrado"));
            rol.setSistema(sistema);
            rol.setNombre(rolDto.getNombreRol());
            rol.setEstado(rolDto.getEstado());
           // rol.setUltLin(rolDto.getUltLin());

            rol.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            rol.setUsuarioActualizacion(usuario.getUsuario());

            Rol rolSave = rolRepository.save(rol);
            RolFormDTO rolDTOUpd = modelMapper.map(rol, RolFormDTO.class);
            return rolDTOUpd;

        }catch (IllegalArgumentException e) {
            log.error("ERROR - service update Role {}", e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }catch (ResourceNotFoundException e){
            log.error("ERROR - Service  updateRole() "+e.getMessage());
            throw e;
        }
        catch (Exception e) {
            log.error( "ERROR - Service updateRole() "+e.getMessage() );
            throw new RuntimeException("Error al actualizar Role "+e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public boolean deleteRole(Integer idRol) {
        log.info("INI - deleteRole()");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Rol rol=rolRepository.findById(idRol).orElseThrow(() -> new ResourceNotFoundException(" Rol no encontrado "));
            if(rol!=null){
                if(rol.isDeleted()){
                    throw new ResourceNotFoundException("El Rol no existe, ya se encuentra eliminado");
                }
                rol.setDeleted(true);
                //rol.setEstado(0);
                rol.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                rol.setUsuarioEliminacion(usuario.getUsuario());

                rolRepository.save(rol);
                estado=true;
            }
        }catch (ResourceNotFoundException e){
            log.error("ERROR - delete Role "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }catch (Exception e) {
            log.error( "ERROR - Service deleteRole() "+e.getMessage() );
            throw new RuntimeException("Error al Eliminar Role "+e.getMessage());
        }
        return estado;
    }

}
