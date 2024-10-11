package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.services.IEntidadService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class EntidadServiceImpl implements IEntidadService {


    private final ModelMapper modelMapper;

    private IEntidadRepository entidadRepository;

    @Override
    public List<EntidadDTO> getEntidades() {

        try {
            log.info("INI - getEntidades");
            List<Entidad> listEntidades=entidadRepository.findAll();
            return listEntidades.stream()
                    .map(entidad -> modelMapper.map(entidad, EntidadDTO.class))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("ERROR - getEntidades() "+e.getMessage());
        }
        return null;
    }

    @Override
    public List<EntidadDTO> getEntidadByNombre(String nombre) {
        try {
            log.info("INI - getEntidadByNombre()");
            List<Entidad> listEntidadPorNombre=entidadRepository.findByNombre(nombre);
            return listEntidadPorNombre.stream()
                    .map(entidad -> modelMapper.map(entidad, EntidadDTO.class))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("ERROR - getEntidadByNombre() "+e.getMessage());
        }
        return null;
    }

    @Override
    public EntidadDTO saveEntidad(EntidadDTO entidadDto) {
        try {
            Entidad entidad=modelMapper.map(entidadDto,Entidad.class);
            Entidad entidadNew=entidadRepository.save(entidad);
            EntidadDTO entidadDtoNew=modelMapper.map(entidadNew, EntidadDTO.class);
            return entidadDtoNew;

        }catch (Exception e){
            log.error("ERROR - saveEntidad() "+e.getMessage());
        }
        return null;
    }

    @Override
    public EntidadDTO updateUsuario(Integer id, EntidadDTO entidadDto) {
        return null;
    }

    @Override
    public void EntidadDelete(Integer id) {

    }
}
