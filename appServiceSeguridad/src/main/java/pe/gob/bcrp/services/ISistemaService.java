package pe.gob.bcrp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;

import java.io.IOException;
import java.util.List;

public interface ISistemaService {

    public List<UsuarioResponsableDTO> listarUsuariosResponsable();
    public List<EstadoCriticoDto> listarEstadosCriticos();


    public List<SistemaDTO> getSistemaCarousel();

    public SistemaResponse getAllSistemas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre,String version);
    public Page<SistemaDTO> listarSistemasPaginated(Pageable pageable);
    public SistemaDTO findById(Integer id);
    public SistemaFormDTO createSistema(SistemaFormDTO sistemaDTO);
    public SistemaFormDTO updateSistema(Integer id, SistemaFormDTO sistemaDTO);
   // public SistemaDTO guardarSistema(SistemaDTO sistemaDTO, MultipartFile logoMain);
    public SistemaFormDTO guardarSistemaPorParametro(
                                                     String nombre,
                                                     String version,
                                                     MultipartFile multiLogoMain,
                                                     MultipartFile multiLogoHead,
                                                     String url,
                                                     String usuarioResponsable,
                                                     String usuarioResponsableAlt,
                                                     Integer idUsuarioResponsable,
                                                     Integer idUsuarioResponsableAlt,
                                                     String urlExterno,
                                                     Integer idEstadoCritico,
                                                     String unidOrganizacional) throws IOException;

    public SistemaFormDTO actualizarSistemaPorParametro(Integer idSistema,
                                                        String nombre,
                                                        String version,
                                                        MultipartFile logoMain,
                                                        MultipartFile logoHead,
                                                        String url,
                                                        String usuarioResponsable,
                                                        String usuarioResponsableAlt,
                                                        Integer idUsuarioResponsable,
                                                        Integer idUsuarioResponsableAlt,
                                                        String urlExterno,
                                                        Integer idEstadoCritico,
                                                        String unidOrganizacional)throws IOException;
    public boolean  deleteSistemas(Integer idSistema);
}
