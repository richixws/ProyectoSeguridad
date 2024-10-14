package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.ISistemaService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SistemaServiceImpl implements ISistemaService {

    private  ISistemaRepository sistemaRepository;

    private ModelMapper modelMapper;


    @Override
    public List<SistemaDTO> getSistemas() {
        try {
            log.info("INI -getSistemas ");
            List<Sistema> listSistemas = sistemaRepository.findAll();
            return listSistemas.stream()
                    .map(sistema -> modelMapper.map(sistema,SistemaDTO.class))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public SistemaDTO saveSistemas(SistemaDTO usuarioDTO) {
        return null;
    }

    @Override
    public SistemaDTO updateSistemas(Integer id, SistemaDTO usuarioDTO) {
        return null;
    }

    @Override
    public void deleteSistemas(Integer id) {

    }
}
