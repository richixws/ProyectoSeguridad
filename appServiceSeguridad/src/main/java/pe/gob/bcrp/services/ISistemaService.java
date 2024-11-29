package pe.gob.bcrp.services;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.SistemaResponse;
import pe.gob.bcrp.dto.sistemaDTO.SistemaDTO;
import pe.gob.bcrp.dto.sistemaDTO.SistemaFormDTO;

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
                                         String         unidOrganizacional,
                                         Integer        esatdo) throws IOException;

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
                                            String        unidOrganizacional,
                                            Integer       estado)throws IOException;
    public boolean  deleteSistema(Integer idSistema);
}
