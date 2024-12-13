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
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.SistemaResponse;
import pe.gob.bcrp.dto.sistemaDTO.SistemaDTO;
import pe.gob.bcrp.dto.sistemaDTO.SistemaFormDTO;
import pe.gob.bcrp.enumerador.EstadoCritico;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IFilesRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import pe.gob.bcrp.services.ISistemaService;
import pe.gob.bcrp.services.IUploadFileService;
import pe.gob.bcrp.entities.Files;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.util.Util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SistemaServiceImpl implements ISistemaService {

    private  ISistemaRepository sistemaRepository;

    private ModelMapper modelMapper;

    private IUploadFileService uploadFileService;

    private IFilesRepository  filesRepository;

    private IUsuarioRepository iUsuarioRepository;

    private Util util;


    @Override
    @Cacheable(value = "usuariosResponsables")
    public List<UsuarioResponsableDTO> listarUsuariosResponsable() {
        try {
            List<UsuarioResponsableDTO> listUserResp = new ArrayList<>();
            UsuarioResponsableDTO usuarioResponsableDTO = new UsuarioResponsableDTO();
            log.info("INI - Service listarUsuariosResponsable ");
            Usuario usuario=util.getUsuario();
            if(usuario !=null && usuario.getPersona()!=null){
                Persona persona=usuario.getPersona();
                String nombreCompleto= persona.getNombres().concat(" "+persona.getApellidoPaterno());
                usuarioResponsableDTO.setCodigo(1);
                usuarioResponsableDTO.setUsuario(nombreCompleto);
                listUserResp.add(usuarioResponsableDTO);
            }

            return listUserResp;

        } catch (Exception e) {
            log.error(" ERROR - Service listarUsuariosResponsable {}", e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    @Cacheable(value = "estadosCriticos")
    public List<EstadoCriticoDto> listarEstadosCriticos() {
        log.info("INI - Service listarEstadosCriticos() ");
        try {
            List<EstadoCriticoDto> listEstados= new ArrayList<>();

            for( EstadoCritico estadoCritico: EstadoCritico.values()){
                EstadoCriticoDto estadoCriticoDto = new EstadoCriticoDto();
                estadoCriticoDto.setCodigo(estadoCritico.getCodigo());
                estadoCriticoDto.setEstCritico(estadoCritico.getDescripcion());
                listEstados.add(estadoCriticoDto);
            }

            return listEstados;

        } catch (Exception e) {
            log.error(" ERROR - Service listarEstadosCriticos()  "+e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @Override
    public List<SistemaDTO> getSistemaCarousel() {
        try {
            log.info("INI -getSistemas ");
            List<Sistema> listSistemas = sistemaRepository.findByIsDeletedFalse();
            return listSistemas.stream()
                    .map(sistema -> {
                        SistemaDTO sistemaDTO=new SistemaDTO();
                      //  sistemaDTO.setIdSistema(sistema.getIdSistema());
                        //sistemaDTO.setLogoMain(sistema.getLogoMain());
                        sistemaDTO.setNombre(sistema.getNombre());
                        return sistemaDTO;
                    })
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }


    @Override
    @Cacheable(value = "sistemas", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #nombre, #version}")
    public SistemaResponse getAllSistemas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre, String version) {

        log.info("INI Service() - getAllSistemas()");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Sistema> pageEntidades = null;

            if( nombre!=null ||  version!=null ){
                String nombreLowerCase = nombre != null ? nombre.toLowerCase() : null;
                String versionLowerCase = version != null ? version.toLowerCase() : null;
                pageEntidades = sistemaRepository.findByFilters( nombreLowerCase, versionLowerCase, pageDetails);
            }else{

               pageEntidades = sistemaRepository.findAll(pageDetails);

            }

            List<Sistema> sistemas = pageEntidades.getContent();
            var sistemaDtos = sistemas.stream()
                    .map( s-> {
                        SistemaDTO sistemaDTO=new SistemaDTO();
                        sistemaDTO.setIdSistema(s.getIdSistema());
                        sistemaDTO.setNombre(s.getNombre());
                        sistemaDTO.setVersion(s.getVersion());
                        sistemaDTO.setLogoMain(s.getLogoMain());
                        sistemaDTO.setLogoHead(s.getLogoHead());
                        sistemaDTO.setUrl(s.getUrl());

                        sistemaDTO.setUsuarioResponsable(s.getUsuarioResponsable());
                        sistemaDTO.setUsuarioResponsableAlterno(s.getUsuarioResponsableAlterno());

                        sistemaDTO.setIdUsuarioResponsable(s.getIdUsuarioResponsable());
                        sistemaDTO.setIdUsuarioResponsableAlterno(s.getIdUsuarioResponsableAlterno());

                        sistemaDTO.setUrlExterno(s.getUrlExterno());
                        sistemaDTO.setIdEstadoCritico(s.getEstadoCritico());
                        sistemaDTO.setUnidadOrganizacional(s.getUnidadOrganizacional());
                        sistemaDTO.setEstado(s.getEstado());
                        sistemaDTO.setDeleted(s.isDeleted());
                        return sistemaDTO;
                    }).toList();

            SistemaResponse sistemaResponse = new SistemaResponse();
            sistemaResponse.setContent(sistemaDtos);
            sistemaResponse.setPageNumber(pageEntidades.getNumber());
            sistemaResponse.setPageSize(pageEntidades.getSize());
            sistemaResponse.setTotalElements(pageEntidades.getTotalElements());
            sistemaResponse.setTotalPages(pageEntidades.getTotalPages());
            sistemaResponse.setLastPage(pageEntidades.isLast());
            return sistemaResponse;

        } catch (Exception e) {
            log.error( "ERROR - getAllSistemas() "+e.getMessage() );
            throw new RuntimeException(e);

        }
    }


    @Override
    @CacheEvict(value = "sistemas", allEntries = true)
    public boolean deleteSistema(Integer idSistema) {
        log.info("INI -deleteSistema() ");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Sistema sistema=sistemaRepository.findById(idSistema).orElseThrow(()-> new ResourceNotFoundException(" Sistema  no encontrado a eliminar"));
            List<Files> listFiles=filesRepository.findAllByIdIdentidad(idSistema);

            if(sistema!=null && !listFiles.isEmpty()){
                if(sistema.isDeleted()){
                    throw new ResourceNotFoundException("El sistema no existe, ya se encuentra eliminado");
                }
                for(Files file:listFiles ){
                    String filePath=file.getFilename();
                    uploadFileService.delete(filePath);
                }

                filesRepository.deleteAll(listFiles);

                sistema.setDeleted(true);
                sistema.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                sistema.setUsuarioEliminacion(usuario.getUsuario());
                sistemaRepository.save(sistema);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteSistemas()"+e.getMessage());
            e.printStackTrace();
            estado=false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return estado;
    }


    @Override
    @CacheEvict(value = "sistemas", allEntries = true)
    public SistemaFormDTO guardarSistema(
                                         String nombre,
                                         String version,
                                         MultipartFile multiLogoMain,
                                         MultipartFile multiLogoHead,
                                         String url,
                                         Integer idUsuarioResponsable,
                                         Integer idUsuarioResponsableAlt,
                                         String urlExterno,
                                         Integer idestadoCritico,
                                         String unidOrganizacional,
                                         Integer estado) throws IOException {
        log.info("INI - guardarSistema() ");
        try {
            Optional<Sistema> sistemaExistente = sistemaRepository.findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre);
            if (sistemaExistente.isPresent()){
                throw new IllegalArgumentException("El nombre del sistema se encuentra en uso, por favor ingrese un nuevo sistema.");
            }
            Usuario usuarioAutenticado =util.getUsuario();

            UUID codigoUuid=UUID.randomUUID();

            Sistema sistema=new Sistema();
           // sistema.setCodigo(codigo);
            sistema.setCodigo(codigoUuid.toString());
            sistema.setNombre(nombre);
            sistema.setVersion(version);
            sistema.setUrl(url);

            if (!usuarioAutenticado.getIdUsuario().equals(idUsuarioResponsable)) {
                throw new IllegalArgumentException("El id del usuario responsable no existe.");
            }
            if (!usuarioAutenticado.getIdUsuario().equals(idUsuarioResponsableAlt)) {
                throw new IllegalArgumentException("El id del usuario responsable alterno no existe.");
            }

            sistema.setIdUsuarioResponsable(idUsuarioResponsable);
            sistema.setUsuarioResponsable(usuarioAutenticado.getPersona().getNombres()+ " " +usuarioAutenticado.getPersona().getApellidoPaterno());

            sistema.setIdUsuarioResponsableAlterno(idUsuarioResponsableAlt);
            sistema.setUsuarioResponsableAlterno(usuarioAutenticado.getPersona().getNombres()+ " " +usuarioAutenticado.getPersona().getApellidoPaterno());


            sistema.setUrlExterno(urlExterno);
            sistema.setEstadoCritico(String.valueOf(idestadoCritico));
            sistema.setUnidadOrganizacional(unidOrganizacional);

            if(estado==null){
                estado=1;
            }
            sistema.setEstado(estado);

            if(multiLogoMain != null){
                //sistema.setLogoMain(multiLogoMain.getOriginalFilename());
                String nombreLogoMain=uploadFileService.upload(multiLogoMain);
                sistema.setLogoMain(nombreLogoMain);
            }
            if(multiLogoHead != null){
               // sistema.setLogoHead(multiLogoHead.getOriginalFilename());
                String nombreLogoHead=uploadFileService.upload(multiLogoHead);
                sistema.setLogoHead(nombreLogoHead);
            }

            sistema.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            sistema.setUsuarioCreacion(usuarioAutenticado.getUsuario());
            Sistema sistemaNew=sistemaRepository.save(sistema);

            //almacenarDatosDeArchivo;
            uploadFileService.almacenarDatosFile(multiLogoHead,sistemaNew.getIdSistema(),"Modulo Sistema");
            uploadFileService.almacenarDatosFile(multiLogoMain,sistemaNew.getIdSistema(),"Modulo Sistema");

            SistemaFormDTO SistemaFormDTO=modelMapper.map(sistemaNew,SistemaFormDTO.class);

            return SistemaFormDTO;
        } catch (IllegalArgumentException e){
        log.error("ERROR - Service savePersona() " + e.getMessage());
        throw new IllegalArgumentException(e.getMessage());

       } catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "sistemas", allEntries = true)
    public SistemaFormDTO actualizarSistema(Integer idSistema,
                                            String nombre,
                                            String version,
                                            MultipartFile logoMain,
                                            MultipartFile logoHead,
                                            String url,
                                            Integer idUsuarioResponsable,
                                            Integer idUsuarioResponsableAlt,
                                            String urlExterno,
                                            Integer idestadoCritico,
                                            String unidOrganizacional,
                                            Integer estado
                                            ) throws IOException {
        log.info("INI - actualizarSistema() ");
        try {
            Usuario usuarioAutenticado=util.getUsuario();//obtener usuario del sistema

            Sistema sistemaExistente = sistemaRepository.findById(idSistema).orElseThrow(() -> new ResourceNotFoundException("El Id del sistema no existe"));
            // Optional<Sistema> sistemaExistente = sistemaRepository.findByNombreContainingIgnoreCaseAndIsDeletedFalse(nombre);
            boolean existeNombredeSistema=sistemaRepository.existsByNombreIgnoreCaseAndIdSistemaNot(nombre,idSistema);
            if (existeNombredeSistema) {
                throw new IllegalArgumentException("El Nombre del sistema ya está registrado en otro sistema.");
            }


            if(sistemaExistente!=null) {

                // 2. Actualizar campos del sistema
                sistemaExistente.setNombre(nombre);
                sistemaExistente.setVersion(version);
                sistemaExistente.setUrl(url);

                if (!usuarioAutenticado.getIdUsuario().equals(idUsuarioResponsable)) {
                    throw new IllegalArgumentException("El id del usuario responsable no existe.");
                }
                if (!usuarioAutenticado.getIdUsuario().equals(idUsuarioResponsableAlt)) {
                    throw new IllegalArgumentException("El id del usuario responsable alterno no existe.");
                }

                sistemaExistente.setIdUsuarioResponsable(idUsuarioResponsable);
                sistemaExistente.setUsuarioResponsable(usuarioAutenticado.getPersona().getNombres()+ " " +usuarioAutenticado.getPersona().getApellidoPaterno());

                sistemaExistente.setIdUsuarioResponsableAlterno(idUsuarioResponsableAlt);
                sistemaExistente.setUsuarioResponsableAlterno(usuarioAutenticado.getPersona().getNombres()+ " " +usuarioAutenticado.getPersona().getApellidoPaterno());

                sistemaExistente.setIdUsuarioResponsable(idUsuarioResponsable);
                sistemaExistente.setIdUsuarioResponsableAlterno(idUsuarioResponsableAlt);

                sistemaExistente.setUrlExterno(urlExterno);
                sistemaExistente.setEstadoCritico(String.valueOf(idestadoCritico));
                sistemaExistente.setUnidadOrganizacional(unidOrganizacional);
                sistemaExistente.setEstado(estado);



                // 3. Actualizar logos si hay archivos nuevos
                if (logoMain != null && !logoMain.isEmpty()) {
                    // String logoMainFilename = UUID.randomUUID().toString() + "_" + multiLogoMain.getOriginalFilename();
                    String filenameMain=sistemaExistente.getLogoMain();
                    uploadFileService.delete(sistemaExistente.getLogoMain());

                    sistemaExistente.setLogoMain(logoMain.getOriginalFilename());
                    Files fileLogoMain=filesRepository.findFileByIdIdentidadAndFilename(idSistema,filenameMain);
                    if(fileLogoMain!=null){
                        uploadFileService.updateDatosFile(logoMain,fileLogoMain);
                    }
                }

                if (logoHead != null && !logoHead.isEmpty()) {
                    //String logoHeadFilename = UUID.randomUUID().toString() + "_" + multiLogoHead.getOriginalFilename();
                    String filenameHead=sistemaExistente.getLogoHead();
                    uploadFileService.delete(sistemaExistente.getLogoHead());

                    sistemaExistente.setLogoHead(logoHead.getOriginalFilename());
                    Files fileLogoHead=filesRepository.findFileByIdIdentidadAndFilename(idSistema,filenameHead);
                    if(fileLogoHead!=null){
                        uploadFileService.updateDatosFile(logoHead,fileLogoHead);
                    }
                }

                // 4. Guardar el sistema actualizado en la base de datos

                sistemaExistente.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                sistemaExistente.setUsuarioActualizacion(usuarioAutenticado.getUsuario());
                Sistema sistemaActualizado = sistemaRepository.save(sistemaExistente);
                // 5. Mapear el sistema actualizado a un DTO
                SistemaFormDTO sistemaFormDTO = modelMapper.map(sistemaActualizado, SistemaFormDTO.class);

                return sistemaFormDTO;
            }

        }catch (ResourceNotFoundException e){
            log.error("ERROR - actualizarSistema()"+e.getMessage());
            throw e;
        }

        return null;
    }





}
