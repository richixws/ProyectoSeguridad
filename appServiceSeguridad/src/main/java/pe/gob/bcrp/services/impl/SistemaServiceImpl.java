package pe.gob.bcrp.services.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.dto.SistemaFormDTO;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.ISistemaService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SistemaServiceImpl implements ISistemaService {

    private  ISistemaRepository sistemaRepository;

    private ModelMapper modelMapper;


    @Override
    public List<SistemaDTO> getSistemaCarousel() {
        try {
            log.info("INI -getSistemas ");
            List<Sistema> listSistemas = sistemaRepository.findAll();
            return listSistemas.stream()
                    .map(sistema -> {
                        SistemaDTO sistemaDTO=new SistemaDTO();
                      //  sistemaDTO.setIdSistema(sistema.getIdSistema());
                        //sistemaDTO.setLogoMain(sistema.getLogoMain());
                        sistemaDTO.setNombre(sistema.getNombre());
                        return sistemaDTO;
                    })
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    @Override
    public List<SistemaDTO> listarSistemas() {
        log.info("INI -listarSistemas ");
        try {

            List<Sistema> listSistemas = sistemaRepository.findAll();
            return listSistemas.stream()
                    .map( sistema -> modelMapper.map(sistema, SistemaDTO.class))
                    .collect(Collectors.toList());
        }catch (Exception e){
            log.error("ERROR - listarSistemas() "+ e.getMessage());
        }
        return null;
    }

    @Override
    public Page<SistemaDTO> listarSistemasPaginated(Pageable pageable) {
        log.info( "INI -listarSistemasPaginated ");
        try {
           Page<Sistema> pageSistema=   sistemaRepository.findAll(pageable);
           Page<SistemaDTO> pageSistemaDTO= modelMapper.map(pageSistema, Page.class);
           return pageSistemaDTO;

        }catch (Exception e){
            log.error("ERROR -  listarSistemas"+e.getMessage());
        }
        return null;
    }

    @Override
    public SistemaDTO findById(Integer id) {

        Sistema sistema=sistemaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("sitema no encontrado"));
        SistemaDTO sistemaDTO=new SistemaDTO();
        return sistemaDTO;
    }


    @Override
    public SistemaDTO findByNombre(String nombre) {
        log.info("INI - findByNombre ");
        try{
            Sistema sistema = sistemaRepository.findByNombre(nombre);
            SistemaDTO sistemaDTO= modelMapper.map(sistema, SistemaDTO.class);
            return  sistemaDTO;
        }catch (Exception e){
            log.error("ERROR - findByNombre "+e.getMessage());
        }
        return null;
    }

    @Override
    public SistemaFormDTO createSistema(SistemaFormDTO sistemaDTO) {

        log.info("INI -saveSistemas ");
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<SistemaFormDTO>();
        try {

            Sistema newSistema=modelMapper.map(sistemaDTO,Sistema.class);
            Sistema saveSistema=sistemaRepository.save(newSistema);
            SistemaFormDTO sistemaDto=modelMapper.map(saveSistema, SistemaFormDTO.class);
            return sistemaDTO;

        }catch (Exception e){
            log.error("ERROR - saveSistemas()"+e.getMessage());
        }
        return null;
    }


    @Override
    public SistemaFormDTO updateSistema(Integer id, SistemaFormDTO sistemaDTO ) {

        log.info("INI -updateSistemas() ");
        try {

            Sistema sistema = sistemaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sistema no encontrado con id :" + id));

            sistema.setNombre(sistemaDTO.getNombre());
            sistema.setUrl(sistemaDTO.getUrl());
            sistema.setLogoMain(sistemaDTO.getLogoMain());
            sistema.setVersion(sistemaDTO.getVersion());
            sistema.setCodigo(sistemaDTO.getCodigo());
            sistema.setLogoHead(sistemaDTO.getLogoHead());

            //Sistema newSistema=modelMapper.map(sistemaDTO,Sistema.class);
            Sistema updSistema = sistemaRepository.save(sistema);
            SistemaFormDTO updateSistema = modelMapper.map(updSistema, SistemaFormDTO.class);
            return updateSistema;
        } catch (ResourceNotFoundException e) {
            log.error("ERROR - eliminarSistema No encontrado| {}", e.getMessage());
            throw e;
        }catch (Exception e){
          log.error("ERROR - updateSistemas()"+e.getMessage());
          throw new RuntimeException("Error al actualizar el sistema", e);
        }

    }



    @Override
    public boolean deleteSistemas(Integer id) {
        log.info("INI -deleteSistemas() ");
        boolean estado=false;
        try {
            Sistema sistema=sistemaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Id de Sistema  no encontrado"));
            if(sistema!=null){
                sistemaRepository.deleteById(id);
                estado=true;
            }

        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteSistemas()"+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }
}
