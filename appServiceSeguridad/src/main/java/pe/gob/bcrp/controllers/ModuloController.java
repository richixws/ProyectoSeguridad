package pe.gob.bcrp.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.ModuloResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IModuloService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
public class ModuloController {

    @Autowired
    private IModuloService moduloService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modulos")
    public ResponseEntity<ModuloResponse> getAllModulos(
            @RequestParam(name = "pageNumber", defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "nombreModulo", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(name = "idSistema", required = false) Integer idSistema
    ){

        log.info("INI - getAllModulos | requestURL=modulos");
        try {

            ModuloResponse moduloResponse=moduloService.getAllModulos(pageNumber, pageSize, sortBy, sortOrder,idSistema);//,nombre
            return new ResponseEntity<>(moduloResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - getAllModulos | requestURL=modulos"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modulo")
    public  ResponseEntity<ResponseDTO<ModuloDTO>> saveModulo(@Valid @RequestBody  ModuloDTO moduloDTO){

        log.info("INI - guardarEntidad | requestURL=entidadDto");
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        try {
            ModuloDTO moduloDto=moduloService.saveModulo(moduloDTO);
            response.setStatus(1);
            response.setMessage("El Modulo fue guardado de manera exitosa");
            // response.setBody(entidadDTO);

        }catch (Exception e){
            log.error("ERROR - guardarEntidad | requestURL=entidadDto");
            response.setStatus(0);
            response.setMessage("Error al guardar el Modulo "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modulo/{idModulo}")
    public  ResponseEntity<ResponseDTO<ModuloDTO>> updateModulo(@Valid @RequestBody  ModuloDTO moduloDTO,
                                                                 @PathVariable("idModulo") Integer idModulo){

        log.info("INI - updateModulo | requestURL=modulo/idModulo");
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        try {
            ModuloDTO moduloDto=moduloService.updateModulo(moduloDTO,idModulo);
            response.setStatus(1);
            response.setMessage("El Modulo fue actualizado de manera exitosa");
            // response.setBody(entidadDTO);

        }catch ( ResourceNotFoundException e) {
                log.error("ERROR - update Modulo No encontrado " + e.getMessage());
                response.setStatus(0);
                response.setMessage(e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("ERROR -  update Modulo | requestURL=modulo");
            response.setStatus(0);
            response.setMessage("Error al guardar el Modulo "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/modulo/{idModulo}")
    public ResponseEntity<ResponseDTO<ModuloDTO>> deleteModulo(@PathVariable("idModulo") Integer idModulo){
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarModulo | requestURL=moduloDto");
        try {

            boolean eliminado= moduloService.deleteModulo(idModulo);
            if(!eliminado){
                throw new ResourceNotFoundException("El Modulo a eliminar con Id "+idModulo+" no existe");
            }
            response.setStatus(1);
            response.setMessage("El Modulo ha sido eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error("ERROR - eliminar Modulo No encontrado "+e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR - eliminarModulo() "+e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al eliminar Modulo: "+e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
