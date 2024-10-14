package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.mapper.UsuarioMapper;
import pe.gob.bcrp.repositories.IPersonaRepository;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import pe.gob.bcrp.services.IUsuarioService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {


   //@Autowired
    private IUsuarioRepository iUsuarioRepository;

    private IPersonaRepository iPersonaRepository;

    private ModelMapper mapper;


    @Override
    public List<UsuarioDTO> getUsuarios() {

        try {
            log.info("INI - getUsuarios");
            List<Usuario> listUsuarios = iUsuarioRepository.findAll();
            return listUsuarios.stream()
                    .map(usuario -> mapToDTO(usuario))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("ERROR - getUsuarios()" +e.getMessage());
        }
        return null;
    }

    @Override
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {

      //  Usuario usuarioNew = new Usuario();
        Persona persona = iPersonaRepository.findById(usuarioDTO.getIdPersona()) .orElseThrow(()-> new RuntimeException("Persona no encontrada")  );
        Usuario usuario=mapToEntity(usuarioDTO);
        usuario.setPersona(persona);
        Usuario newUsuario = iUsuarioRepository.save(usuario);

        UsuarioDTO usuarioResp=mapToDTO(newUsuario);
        return usuarioResp;

    }

    @Override
    public UsuarioDTO updateUsuario(Integer id, UsuarioDTO usuarioDTO) {


        Usuario usuarioUpdate=iUsuarioRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
        usuarioUpdate.setUsuario(usuarioDTO.getUsuario());
        usuarioUpdate.setPassword(usuarioDTO.getPassword());
        usuarioUpdate.setCorreoInstitucional(usuarioDTO.getCorreoInstitucional());
        usuarioUpdate.setAmbito(usuarioDTO.getAmbito());
        usuarioUpdate.setFechaCreacion(usuarioDTO.getFechaCreacion());
        usuarioUpdate.setDocSustento(usuarioDTO.getDocSustento());
     //   usuarioUpdate.setPersona(usuarioDTO.getIdPersona());

        UsuarioDTO newUsuario=UsuarioMapper.mapToUsuariosDto(usuarioUpdate,new UsuarioDTO());
        return newUsuario;
    }

    @Override
    public void delete(Integer id) {
          iUsuarioRepository.deleteById(id);

    }

    @Override
    public UsuarioDTO buscarPorUsuarioLogin(String usuario) {
        Optional<Usuario> usuarioLogin=iUsuarioRepository.findByUsuario(usuario);
        UsuarioDTO dto= mapToDTO(usuarioLogin.get());
        return dto;
    }


    // convert Entity into DTO
    private UsuarioDTO mapToDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = mapper.map(usuario, UsuarioDTO.class);
//        PostDto postDto = new PostDto();
//        postDto.setId(post.getId());
//        postDto.setTitle(post.getTitle());
//        postDto.setDescription(post.getDescription());
//        postDto.setContent(post.getContent());
        return usuarioDTO;
    }

    private Usuario mapToEntity(UsuarioDTO usuarioDTO){
        Usuario usuario = mapper.map(usuarioDTO, Usuario.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return usuario;
    }
}
