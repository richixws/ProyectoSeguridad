package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IFilesRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import pe.gob.bcrp.services.ISistemaService;
import pe.gob.bcrp.services.IUploadFileService;
import pe.gob.bcrp.util.Util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    public List<UsuarioResponsableDTO> listarUsuariosResponsable() {
        try {
            List<UsuarioResponsableDTO> listUserResp = new ArrayList<>();
            UsuarioResponsableDTO usuarioResponsableDTO = new UsuarioResponsableDTO();
            log.info("INI - Service listarUsuariosResponsable ");
            Usuario usuario=util.getUsuario();
            if(usuario !=null && usuario.getPersona()!=null){
                Persona persona=usuario.getPersona();
                String nombreCompleto= persona.getNombres().concat(" "+persona.getApellidoPaterno());
                usuarioResponsableDTO.setCodigo(persona.getIdPersona());
                usuarioResponsableDTO.setUsuario(nombreCompleto);
                listUserResp.add(usuarioResponsableDTO);
            }

            return listUserResp;

        } catch (Exception e) {
            log.error(" ERROR - Service listarUsuariosResponsable "+e.getMessage());
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
    public List<SistemaDTO> listarSistemas() {
        log.info("INI -listarSistemas ");
        try {

            List<Sistema> listSistemas = sistemaRepository.findByIsDeletedFalse();
            return listSistemas.stream()
                    .map( sistema -> modelMapper.map(sistema, SistemaDTO.class))
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("ERROR - listarSistemas() "+ e.getMessage());
        }
        return null;
    }

    @Override
    public SistemaResponse getAllSistemas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre,String version) {

        log.info("INI Service() - getAllSistemas()");
        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Sistema> pageEntidades = null;

            if( nombre!=null ||  version!=null ){

                pageEntidades = sistemaRepository.findByFilters( nombre, version, pageDetails);
            }else{

               pageEntidades = sistemaRepository.findByIsDeletedFalse(pageDetails);

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
                        return sistemaDTO;
                    })

                    .toList();


                   // .map(s -> modelMapper.map(s, SistemaDTO.class))
                   // .filter( p -> p.getCodigo() )
                   // .toList();

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
    public Page<SistemaDTO> listarSistemasPaginated(Pageable pageable) {
        log.info( "INI -listarSistemasPaginated ");
        try {
           Page<Sistema> pageSistema=   sistemaRepository.findAll(pageable);
           Page<SistemaDTO> pageSistemaDTO= modelMapper.map(pageSistema, Page.class);
           return pageSistemaDTO;

        }catch (Exception e){
            log.error("ERROR -  listarSistemas"+e.getMessage());
        }
        return null;
    }

    @Override
    public SistemaDTO findById(Integer id) {

        Sistema sistema=sistemaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("sitema no encontrado"));
        SistemaDTO sistemaDTO=new SistemaDTO();
        return sistemaDTO;
    }



    @Override
    public SistemaFormDTO createSistema(SistemaFormDTO sistemaDTO) {

        log.info("INI -saveSistemas ");
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<SistemaFormDTO>();
        try {

            Sistema newSistema=modelMapper.map(sistemaDTO,Sistema.class);
            Sistema saveSistema=sistemaRepository.save(newSistema);
            SistemaFormDTO sistemaDto=modelMapper.map(saveSistema, SistemaFormDTO.class);
            return sistemaDTO;

        }catch (Exception e){
            log.error("ERROR - saveSistemas()"+e.getMessage());
        }
        return null;
    }


    @Override
    public SistemaFormDTO updateSistema(Integer id, SistemaFormDTO sistemaDTO ) {

        log.info("INI -updateSistemas() ");
        try {

            Sistema sistema = sistemaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con id :" + id));

            sistema.setNombre(sistemaDTO.getNombre());
            sistema.setUrl(sistemaDTO.getUrl());
            sistema.setLogoMain(sistemaDTO.getLogoMain());
            sistema.setVersion(sistemaDTO.getVersion());
            sistema.setCodigo(sistemaDTO.getCodigo());
            sistema.setLogoHead(sistemaDTO.getLogoHead());

            //Sistema newSistema=modelMapper.map(sistemaDTO,Sistema.class);
            Sistema updSistema = sistemaRepository.save(sistema);
            SistemaFormDTO updateSistema = modelMapper.map(updSistema, SistemaFormDTO.class);
            return updateSistema;
        } catch (ResourceNotFoundException e) {
            log.error("ERROR - eliminarSistema No encontrado| {}", e.getMessage());
            throw e;
        }catch (Exception e){
          log.error("ERROR - updateSistemas()"+e.getMessage());
          throw new RuntimeException("Error al actualizar el sistema", e);
        }

    }



    @Override
    public boolean deleteSistemas(Integer idSistema) {
        log.info("INI -deleteSistemas() ");
        boolean estado=false;
        try {
            Usuario usuario=util.getUsuario();

            Sistema sistema=sistemaRepository.findById(idSistema).orElseThrow(()-> new ResourceNotFoundException("Id de Sistema  no encontrado"));
            List<Files> listFiles=filesRepository.findAllByIdIdentidad(idSistema);

            if(sistema!=null && !listFiles.isEmpty()){

                for(Files file:listFiles ){
                    String filePath=file.getFilename();
                    uploadFileService.delete(filePath);
                }

                filesRepository.deleteAll(listFiles);

                sistema.setDeleted(true);
                sistema.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                sistema.setUsuarioEliminacion(usuario.getUsuario());
                sistemaRepository.save(sistema);
                //sistemaRepository.deleteById(id);
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
    public SistemaFormDTO guardarSistemaPorParametro(
                                                     String nombre,
                                                     String version,
                                                     MultipartFile multiLogoMain,
                                                     MultipartFile multiLogoHead,
                                                     String url,
                                                     String usuarioResponsable,
                                                     String usuarioResponsableAlt) throws IOException {
        try {

            Usuario usuario=util.getUsuario();

            UUID codigoUuid=UUID.randomUUID();
            System.out.println(codigoUuid.toString());

            Sistema sistema=new Sistema();
           // sistema.setCodigo(codigo);
            sistema.setCodigo(codigoUuid.toString());
            sistema.setNombre(nombre);
            sistema.setVersion(version);
            sistema.setUrl(url);
            sistema.setUsuarioResponsable(usuarioResponsable);
            sistema.setUsuarioResponsableAlterno(usuarioResponsableAlt);

            if(multiLogoMain != null){
                sistema.setLogoMain(multiLogoMain.getOriginalFilename());
                uploadFileService.upload(multiLogoMain);
            }
            if(multiLogoHead != null){
                sistema.setLogoHead(multiLogoHead.getOriginalFilename());
                uploadFileService.upload(multiLogoHead);
            }

            sistema.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            sistema.setUsuarioCreacion(usuario.getUsuario());
            Sistema sistemaNew=sistemaRepository.save(sistema);

            //almacenarDatosDeArchivo(sistemaNew);
            uploadFileService.almacenarDatosFile(multiLogoHead,sistemaNew.getIdSistema());
            uploadFileService.almacenarDatosFile(multiLogoMain,sistemaNew.getIdSistema());

            SistemaFormDTO SistemaFormDTO=modelMapper.map(sistemaNew,SistemaFormDTO.class);

            return SistemaFormDTO;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public SistemaFormDTO actualizarSistemaPorParametro(Integer idSistema,
                                                        String nombre,
                                                        String version,
                                                        MultipartFile logoMain,
                                                        MultipartFile logoHead,
                                                        String url,
                                                        String usuarioResponsable,
                                                        String usuarioResponsableAlt) throws IOException {
        try {
            Usuario usuario=util.getUsuario();//obtener usuario del sistema

            Sistema sistemaExistente = sistemaRepository.findById(idSistema).orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con el id: " + idSistema));

            if(sistemaExistente!=null) {

                // 2. Actualizar campos del sistema
                //sistemaExistente.setCodigo(codigo);
                sistemaExistente.setNombre(nombre);
                sistemaExistente.setVersion(version);
                sistemaExistente.setUrl(url);
                sistemaExistente.setUsuarioResponsable(usuarioResponsable);
                sistemaExistente.setUsuarioResponsableAlterno(usuarioResponsableAlt);



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
                sistemaExistente.setUsuarioActualizacion(usuario.getUsuario());
                Sistema sistemaActualizado = sistemaRepository.save(sistemaExistente);
                // 5. Mapear el sistema actualizado a un DTO
                SistemaFormDTO sistemaFormDTO = modelMapper.map(sistemaActualizado, SistemaFormDTO.class);

                return sistemaFormDTO;
            }

        }catch (ResourceNotFoundException e){
            log.error("ERROR - actualizarSistemaPorParametro()"+e.getMessage());
            throw e;
        }

        return null;
    }





}
