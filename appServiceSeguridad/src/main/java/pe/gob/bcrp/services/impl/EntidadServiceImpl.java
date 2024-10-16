package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
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
    public EntidadDTO findEntidadByNombre(String nombre) {
        try {
            log.info("INI - findEntidadByNombre()");
            Entidad entidad=entidadRepository.findByNombre(nombre);
            if(entidad==null){
                log.error("ERROR - findEntidadByNombre() "+nombre);
                return null;
            }

            EntidadDTO dto=modelMapper.map(entidad, EntidadDTO.class);
            return dto;
        }catch (Exception e){
            log.error("ERROR - getEntidadByNombre() "+e.getMessage());
        }
        return null;
    }

    @Override
    public EntidadDTO saveEntidad(EntidadDTO entidadDto) {
        try {
            log.info("INI - saveEntidad()");
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
    public EntidadDTO updateEntidad(Integer id, EntidadDTO entidadDto) {

        try {
            log.info("INI - updateUsuario()");
            Entidad entidad=entidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado con id :" + entidadDto.getIdEntidad()));
            EntidadDTO entidadDtoNew=modelMapper.map(entidadDto,EntidadDTO.class);

            entidad.setNombre(entidadDto.getNombre());
            entidad.setRuc(entidadDto.getRuc());
            entidad.setSigla(entidadDto.getSigla());
            entidad.setCodExterno(entidadDto.getCodExterno());

            EntidadDTO updateEntidad=modelMapper.map(entidad,EntidadDTO.class);
            return  updateEntidad;

        }catch (Exception e){
            log.error("ERROR - updateEntidad() "+e.getMessage());

        }
        return null;
    }

    @Override
    public boolean deleteEntidad(Integer id) {

        log.info("INI - deleteEntidad()");
        boolean estado=false;
        try {
            Entidad entidad=entidadRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entidad con no encontrado"));
            if(entidad!=null){
                entidadRepository.deleteById(id);
                estado=true;
            }


        }catch (Exception e){
            log.error("ERROR - deleteEntidad() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }
}
