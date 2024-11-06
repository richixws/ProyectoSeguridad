package pe.gob.bcrp.services;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.RegistroUsuarioDTO;
import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.dto.UsuarioFormDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.dto.response.UsuarioResponse;
import pe.gob.bcrp.entities.Usuario;

import java.util.List;

public interface IUsuarioService {

  public UsuarioResponse getAllUsuarios(Integer pageNumber,
                                        Integer pageSize,
                                        String sortBy,
                                        String sortOrder,
                                        String nombre,
                                        Integer tipoDocumento,
                                        String numeroDocumento,
                                        Integer idSistema,
                                        String ambito);



  public List<UsuarioFormDTO> uploadUserCsv(MultipartFile file);

  public RegistroUsuarioDTO guardarUsuario(Integer tipoDocumento,
                                           String numeroDocumento,
                                           String nombres,
                                           String apePaterno,
                                           String apeMaterno,
                                           String correoElectronico,
                                           String ambito,
                                           MultipartFile sustento);

  public RegistroUsuarioDTO updateUsuario(Integer idUsuario, RegistroUsuarioDTO registroUsuarioDTO);
  public boolean  deleteUsuario(Integer idUsuario);

  // usado en le Login
  public UsuarioDTO buscarPorUsuarioLogin(String usuario );

}
