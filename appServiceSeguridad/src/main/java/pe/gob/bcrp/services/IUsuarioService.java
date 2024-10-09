package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

  public List<UsuarioDTO> getUsuarios();
  public UsuarioDTO save(UsuarioDTO usuarioDTO);
  public UsuarioDTO updateUsuario(Integer id, UsuarioDTO usuarioDTO);
  public void  delete(Integer id);

}
