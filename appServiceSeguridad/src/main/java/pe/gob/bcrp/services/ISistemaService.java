package pe.gob.bcrp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.SistemaResponse;

import java.io.IOException;
import java.util.List;

public interface ISistemaService {

    public List<UsuarioResponsableDTO> listarUsuariosResponsable();
    public List<EstadoCriticoDto> listarEstadosCriticos();
    public List<SistemaDTO> getSistemaCarousel();
    public SistemaResponse getAllSistemas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombre, String version);
    public SistemaFormDTO guardarSistema(
                                         String         nombre,
                                         String         version,
                                         MultipartFile  multiLogoMain,
                                         MultipartFile  multiLogoHead,
                                         String         url,
                                         Integer        idUsuarioResponsable,
                                         Integer        idUsuarioResponsableAlt,
                                         String         urlExterno,
                                         Integer        idEstadoCritico,
                                         String         unidOrganizacional) throws IOException;

    public SistemaFormDTO actualizarSistema(Integer       idSistema,
                                            String        nombre,
                                            String        version,
                                            MultipartFile logoMain,
                                            MultipartFile logoHead,
                                            String        url,
                                            Integer       idUsuarioResponsable,
                                            Integer       idUsuarioResponsableAlt,
                                            String        urlExterno,
                                            Integer       idEstadoCritico,
                                            String        unidOrganizacional)throws IOException;
    public boolean  deleteSistema(Integer idSistema);
}
