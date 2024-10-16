package pe.gob.bcrp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.dto.SistemaFormDTO;

import java.util.List;

public interface ISistemaService {

    public List<SistemaDTO> getSistemaCarousel();
    public List<SistemaDTO> listarSistemas();
    public Page<SistemaDTO> listarSistemasPaginated(Pageable pageable);
    public SistemaDTO findById(Integer id);
    public SistemaDTO findByNombre(String nombre);
    public SistemaFormDTO createSistema(SistemaFormDTO sistemaDTO);
    public SistemaFormDTO updateSistema(Integer id, SistemaFormDTO sistemaDTO);
   // public SistemaDTO guardarSistema(SistemaDTO sistemaDTO, MultipartFile logoMain);
   // public SistemaDTO saveSistemas(String codigo, String nombre, String version, MultipartFile logoMain,MultipartFile logoHead, String url);
   // public SistemaDTO updateSistemas(Integer id, String codigo, String nombre, String version, MultipartFile logoMain,MultipartFile logoHead, String url);
    public boolean  deleteSistemas(Integer id);
}
