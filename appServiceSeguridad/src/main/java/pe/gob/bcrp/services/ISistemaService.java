package pe.gob.bcrp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.EntidadResponse;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.dto.SistemaFormDTO;
import pe.gob.bcrp.dto.SistemaResponse;

import java.io.IOException;
import java.util.List;

public interface ISistemaService {

    public List<SistemaDTO> getSistemaCarousel();
    public List<SistemaDTO> listarSistemas();
    public SistemaResponse getAllSistemas(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String codigo, String nombre,String version);
    public Page<SistemaDTO> listarSistemasPaginated(Pageable pageable);
    public SistemaDTO findById(Integer id);
    public SistemaDTO findByNombre(String nombre);
    public SistemaFormDTO createSistema(SistemaFormDTO sistemaDTO);
    public SistemaFormDTO updateSistema(Integer id, SistemaFormDTO sistemaDTO);
   // public SistemaDTO guardarSistema(SistemaDTO sistemaDTO, MultipartFile logoMain);
    public SistemaFormDTO guardarSistemaPorParametro(String codigo,
                                                     String nombre,
                                                     String version,
                                                     MultipartFile multiLogoMain,
                                                     MultipartFile multiLogoHead,
                                                     String url) throws IOException;
    public SistemaFormDTO actualizarSistemaPorParametro(Integer id,
                                                        String codigo,
                                                        String nombre,
                                                        String version,
                                                        MultipartFile logoMain,
                                                        MultipartFile logoHead,
                                                        String url)throws IOException;
    public boolean  deleteSistemas(Integer id);
}
