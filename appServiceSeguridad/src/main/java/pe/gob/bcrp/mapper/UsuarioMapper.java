package pe.gob.bcrp.mapper;

import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.entities.Usuario;

public class UsuarioMapper {

    public static  UsuarioDTO mapToUsuariosDto(Usuario usuario, UsuarioDTO usuarioDTO){

        usuarioDTO.setIdUsuario(usuario.getIdUsuario());

        usuarioDTO.setUsuario(usuario.getUsuario());
        usuarioDTO.setAmbito(usuario.getAmbito());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setDocSustento(usuario.getDocSustento());
        usuarioDTO.setFechaCreacion(usuario.getFechaCreacion());
        usuarioDTO.setCorreoInstitucional(usuario.getCorreoInstitucional());

        usuarioDTO.setIdPersona(usuario.getPersona().getIdPersona());

      //  usuarioDTO.setPersona();

        return usuarioDTO;
    }

    public static  Usuario mapToUsuario(UsuarioDTO usuarioDTO, Usuario usuario){

        usuario.setIdUsuario(usuarioDTO.getIdUsuario());

        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setAmbito(usuarioDTO.getAmbito());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setDocSustento(usuarioDTO.getDocSustento());
        usuario.setFechaCreacion(usuarioDTO.getFechaCreacion());
        usuario.setCorreoInstitucional(usuarioDTO.getCorreoInstitucional());
      //  usuario.setPersona(usuarioDTO.getUsuario());

        return usuario;


    }

}
