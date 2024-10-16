package pe.gob.bcrp.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IEntidadService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
public class EntidadController {

    @Autowired
    private IEntidadService entidadService;


    @GetMapping("/entidades")
    public ResponseEntity<List<EntidadDTO>> listarEntidades(){
        log.info("INI - listarEntidades | requestURL=entidades");
        try {
            List<EntidadDTO> entidadDto=entidadService.getEntidades();
            return new ResponseEntity<>(entidadDto, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listarEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/entidad/{nombre}")
    public ResponseEntity<EntidadDTO> buscarEntidadPorNombre(@PathVariable String nombre){
        log.info("INI - listarEntidadesPorNombre | requestURL=nombre");
        EntidadDTO entidadDto=entidadService.findEntidadByNombre(nombre);
        return ResponseEntity.ok(entidadDto);
    }

    @PostMapping("/entidad")
    public  ResponseEntity<EntidadDTO> guardarEntidad(@Valid @RequestBody  EntidadDTO entidadDto){
        log.info("INI - guardarEntidad | requestURL=entidadDto");
        EntidadDTO entidadDTO=entidadService.saveEntidad(entidadDto);
        return ResponseEntity.ok(entidadDTO);
    }

    @PutMapping("/entidad/{id}")
    public ResponseEntity<EntidadDTO> updateEntidad(@PathVariable("id") Integer id, @Valid EntidadDTO entidadDTO){
        log.info("INI - updateEntidad | requestURL=entidadDto");
        EntidadDTO entidadDto=entidadService.updateEntidad(id, entidadDTO);
        return ResponseEntity.ok(entidadDto);
    }

    @DeleteMapping("/entidad/{id}")
    public ResponseEntity<ResponseDTO<EntidadDTO>> eliminarEntidad(@PathVariable("id") Integer id){
        ResponseDTO<EntidadDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarEntidad | requestURL=entidadDto");
        try {

           boolean eliminado= entidadService.deleteEntidad(id);
           if(!eliminado){
               throw new ResourceNotFoundException("La Entidad a eliminar con Id "+id+" no existe");
           }
            response.setStatus(1);
            response.setMessage("La Entidad ha sido eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error("ERROR - eliminarEntidad No encontrado "+e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR - eliminarEntidad() "+e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al eliminar Entidad: "+e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }









}
