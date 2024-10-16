package pe.gob.bcrp.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.dto.SistemaFormDTO;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.ISistemaService;
import pe.gob.bcrp.services.IUploadFileService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class SistemaController {

    @Autowired
    private ISistemaService sistemaService;

    @Autowired
    private IUploadFileService uploadFileService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/carousel/sistemas")
    public ResponseEntity<List<SistemaDTO>> listarCarouselSistemas() {

        List<SistemaDTO> listSistemas=sistemaService.getSistemaCarousel();
        return ResponseEntity.ok().body(listSistemas);
    }


    @GetMapping("/sistema/{nombre}")
    public ResponseEntity<SistemaDTO> buscarSistemaPorNombre(@PathVariable String nombre){
        SistemaDTO sistemaDTO=sistemaService.findByNombre(nombre);
        return sistemaDTO != null ? ResponseEntity.ok().body(sistemaDTO) : ResponseEntity.notFound().build();
    }


    @GetMapping("/sistemas")
    public ResponseEntity<List<SistemaDTO>> listarSistemas(){
        log.info("Listando lista de sistemas");
        try {
            List<SistemaDTO> listSistemas=sistemaService.listarSistemas();
            return new ResponseEntity<>(listSistemas,HttpStatus.OK);
        }catch (Exception e) {
            log.error(" ERROR - listar sistemas"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sistemas/page")
    public ResponseEntity<Page<SistemaDTO>> paginate(@PageableDefault(sort = "nombre", direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
        log.info("Listando lista de sistemas");
        try {
            Page<SistemaDTO> page=sistemaService.listarSistemasPaginated(pageable);
            return new ResponseEntity<>(page,HttpStatus.OK);
        }catch (Exception e) {
            log.error(" ERROR - listar sistema Paginado"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


   @PostMapping(value = "/sistema")
   public ResponseEntity<ResponseDTO<SistemaFormDTO>> guardarSistema(@Validated @RequestBody SistemaFormDTO sistemaDTO){
        log.info("INFO - Guardar Sistema");
        ResponseDTO<SistemaFormDTO> response=new ResponseDTO();
        try {

            SistemaFormDTO sistemaDto=sistemaService.createSistema(sistemaDTO);
            response.setStatus(1);
            response.setMessage("El Sistema fue guardado de manera exitosa");
            response.setBody(sistemaDto);

        }catch (Exception e ){
            log.error("ERROR - guardarSistema |", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al guardar el Sistema : "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PutMapping("/sistema/{id}")
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> actualizarSistema(@PathVariable Integer id, @Validated @RequestBody SistemaFormDTO sistemaDTO) {

        log.info("INFO - Actualizar Sistema");
        ResponseDTO<SistemaFormDTO> response=new ResponseDTO();
        try {
            SistemaFormDTO sistemaDto=sistemaService.updateSistema( id, sistemaDTO );
            if(sistemaDto==null){
                response.setStatus(0);
                response.setMessage("Sistema no encontrado con id: " + id);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.setStatus(1);
            response.setMessage("Sistema actualizado con exito");
            response.setBody(sistemaDto);

        }catch (Exception e){
            log.error("ERROR - actualizarSistema |", e);
            response.setStatus(0);
            response.setMessage("Error al actualizar el Sistema");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/sistema/{id}")
    public ResponseEntity<ResponseDTO<?>> eliminarSistema(@PathVariable("id") Integer id) {
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<>();
        log.info("INFO - Eliminar Sistema");
        try {
            boolean eliminado = sistemaService.deleteSistemas(id);
            if (!eliminado) {
                throw new ResourceNotFoundException("El sistema a eliminar con ID " + id + " no existe");
            }else{
                response.setStatus(1);
                response.setMessage("El sistema ha sido eliminado con éxito");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (ResourceNotFoundException e) {
            log.error("ERROR - eliminarSistema No encontrado| {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage()); // Aquí puedes pasar el mensaje de la excepción
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("ERROR - eliminarSistema | {}", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al eliminar el sistema");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




}
