package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.dto.response.UsuarioResponse;
import pe.gob.bcrp.entities.DocumentoIdentidad;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.mapper.UsuarioMapper;
import pe.gob.bcrp.repositories.IDocumentoIdentidadRepository;
import pe.gob.bcrp.repositories.IPersonaRepository;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import pe.gob.bcrp.services.IUploadFileService;
import pe.gob.bcrp.services.IUsuarioService;
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
public class UsuarioServiceImpl implements IUsuarioService {


   //@Autowired
    private IUsuarioRepository usuarioRepository;

    private IPersonaRepository personaRepository;

    private IDocumentoIdentidadRepository documentoIdentidadRepository;

    private ModelMapper modelMapper;

    private Util util;

    private IUploadFileService uploadFileService;


    @Override
    public UsuarioResponse getAllUsuarios(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombres, Integer tipoDocumento, String numeroDocumento, Integer idSistema, String ambito) {
        log.info("INI Service() - getAllUsuarios");

        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Usuario> pageUsuarios = null;

            if (nombres != null || tipoDocumento != null || numeroDocumento != null || ambito != null) {
                pageUsuarios = usuarioRepository.findByFilters(nombres, tipoDocumento, numeroDocumento,ambito, pageDetails);//,
            }else if(idSistema != null){
                pageUsuarios = usuarioRepository.findBySistemaId(idSistema, pageDetails);
            }else{
                 pageUsuarios = usuarioRepository.findByIsDeletedFalse(pageDetails);
            }

            List<Usuario> usuarios = pageUsuarios.getContent();

            List<UsuarioFormDTO> usuariosDTOS = usuarios.stream().map(user -> {
                UsuarioFormDTO usuarioDTO = modelMapper.map(user, UsuarioFormDTO.class);
                  if(user.getPersona() !=null){
                      usuarioDTO.setIdDocumento(user.getPersona().getDocuIdentidad().getIdDocumentoIdentidad());
                      usuarioDTO.setTipoDocumento(user.getPersona().getDocuIdentidad().getTipoDocumentoIdentidad());
                      usuarioDTO.setNumeroDocumento(user.getPersona().getNumeroDocumento());
                      usuarioDTO.setNombres(user.getPersona().getNombres());
                      usuarioDTO.setApellidoPaterno(user.getPersona().getApellidoPaterno());
                      usuarioDTO.setApellidoMaterno(user.getPersona().getApellidoMaterno());
                      usuarioDTO.setCorreoElectronico(user.getPersona().getCorreo());
                  }
                return usuarioDTO;
            }).toList();

            UsuarioResponse usuarioResponse = new UsuarioResponse();
            usuarioResponse.setContent(usuariosDTOS);
            usuarioResponse.setPageNumber(pageUsuarios.getNumber());
            usuarioResponse.setPageSize(pageUsuarios.getSize());
            usuarioResponse.setTotalElements(pageUsuarios.getTotalElements());
            usuarioResponse.setTotalPages(pageUsuarios.getTotalPages());
            usuarioResponse.setLastPage(pageUsuarios.isLast());
            return usuarioResponse;

        } catch (Exception e) {
            log.error( "ERROR - getAllEntidades() "+e.getMessage() );
            throw new RuntimeException(e);
        }
    }

    @Override
     public UsuarioFormDTO saveUsuario(UsuarioFormDTO usuarioFormDTO) {
        log.info("INI Service() - saveUsuario");
        try {
            Usuario usuario=util.getUsuario();

            Persona persona=new Persona();
            Usuario usuario1=new Usuario();
          //  persona.setTipoDocumento(usuarioFormDTO.getIdDocumento());
            persona.setNombres(usuarioFormDTO.getNombres());
            persona.setApellidoPaterno(usuarioFormDTO.getApellidoPaterno());
            persona.setApellidoMaterno(usuarioFormDTO.getApellidoMaterno());
            persona.setCorreo(usuarioFormDTO.getCorreoElectronico());

            Usuario usuario2=modelMapper.map(usuarioFormDTO, Usuario.class);





            Usuario usuarioNew=usuarioRepository.save(usuario);

        }catch (Exception e) {

        }
       return null;
    }

    @Override
    public RegistroUsuarioDTO guardarUsuario(Integer tipoDocumento,
                                             String numeroDocumento,
                                             String nombres,
                                             String apePaterno,
                                             String apeMaterno,
                                             String correoElectronico,
                                             String ambito, MultipartFile sustento) {

        try {
            Usuario usuarioSistema=util.getUsuario();

            DocumentoIdentidad doc=documentoIdentidadRepository.findById(tipoDocumento).orElseThrow(()-> new ResourceNotFoundException("documento no encontrado"));

            Usuario usuario=new Usuario();
            Persona persona=new Persona();
           // usuarioNew.set
            persona.setDocuIdentidad(doc);
            persona.setNumeroDocumento(numeroDocumento);
            persona.setNombres(nombres);
            persona.setApellidoPaterno(apePaterno);
            persona.setApellidoMaterno(apeMaterno);
            persona.setCorreo(correoElectronico);

            persona.setUsuarioCreacion(usuario.getUsuario());
            persona.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));


            if(ambito.equalsIgnoreCase("interno")){
                usuario.setAmbito(ambito);
                if(sustento !=null){
                    usuario.setDocSustento(sustento.getOriginalFilename());
                    uploadFileService.upload(sustento);
                }

                usuario.setFechaCreacion(new Date());
                usuario.setEstado("ACTIVO");

                usuario.setUsuarioCreacion(usuarioSistema.getUsuario());
                usuario.setHoraCreacion(usuario.getHoraCreacion());
            }


            Persona personaNew=personaRepository.save(persona);
            usuario.setPersona(personaNew);
            //usuarioNew.setEstado("Activo");
            Usuario usuarioNew=usuarioRepository.save(usuario);

            uploadFileService.almacenarDatosFile(sustento,usuarioNew.getIdUsuario(),"Modulo Usuario");

            RegistroUsuarioDTO regUsuarioNew=modelMapper.map(usuarioNew, RegistroUsuarioDTO.class);
            return regUsuarioNew;

        }catch (Exception e) {
          log.error(e.getMessage());
          throw new RuntimeException(e);
        }
    }

    @Override
    public RegistroUsuarioDTO updateUsuario(Integer idUsuario, RegistroUsuarioDTO registroUsuarioDTO) {

        log.info("INI Service() - updateUsuario");
        try {
            Usuario usuarioReg = util.getUsuario();

            Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Persona persona = personaRepository.findById(usuario.getPersona().getIdPersona()).orElseThrow(() -> new RuntimeException("Persona no encontrado"));
            DocumentoIdentidad docuIde = documentoIdentidadRepository.findById(registroUsuarioDTO.getTipoDocumento()).orElseThrow(() -> new ResourceNotFoundException("documento no encontrado"));

            persona.setDocuIdentidad(docuIde);
            persona.setNumeroDocumento(registroUsuarioDTO.getDocumentoIdentidad());
            persona.setNombres(registroUsuarioDTO.getNombres());
            persona.setApellidoPaterno(registroUsuarioDTO.getApPat());
            persona.setApellidoMaterno(registroUsuarioDTO.getApMat());
            persona.setCorreo(registroUsuarioDTO.getCorreo());

            persona.setUsuarioActualizacion(usuarioReg.getUsuario());
            persona.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));


            // usuarioUpdate.setCorreoInstitucional(registroUsuarioDTO.getCorreo());
            usuario.setAmbito(registroUsuarioDTO.getAmbito());

            usuario.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            usuario.setUsuarioActualizacion(usuario.getUsuario());
            usuario.setDocSustento(usuario.getDocSustento());
            //   usuarioUpdate.setPersona(usuarioDTO.getIdPersona());**/

            Persona personaUpd=personaRepository.save(persona);
            usuario.setPersona(personaUpd);
            Usuario usuarioUpd=usuarioRepository.save(usuario);

            RegistroUsuarioDTO newUsuario = modelMapper.map(usuarioUpd, RegistroUsuarioDTO.class);
            return newUsuario;

        }catch (ResourceNotFoundException e){
            log.error("ERROR - updateUsuario() "+e.getMessage());
            throw e;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteUsuario(Integer idUsuario) {

        log.info("INI - InhabilitarUsuario()");
        boolean estado=false;
        try {
            Usuario usuarioReg=util.getUsuario();

            Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            if(usuario!=null){
                //entidadRepository.deleteById(id);
                usuario.setDeleted(true);
                usuario.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                usuario.setUsuarioEliminacion(usuarioReg.getUsuario());
                usuario.setEstado("INHABILITADO");

                usuarioRepository.save(usuario);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteEntidad() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }


    @Override
    public UsuarioDTO buscarPorUsuarioLogin(String usuario) {
        Optional<Usuario> usuarioLogin=usuarioRepository.findByUsuario(usuario);
        UsuarioDTO dto= mapToDTO(usuarioLogin.get());
       // UsuarioDTO dto=modelMapper.map(usuarioLogin,UsuarioDTO.class);
        return dto;
    }


    // convert Entity into DTO
    private UsuarioDTO mapToDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return usuarioDTO;
    }

    private Usuario mapToEntity(UsuarioDTO usuarioDTO){
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return usuario;
    }
}
