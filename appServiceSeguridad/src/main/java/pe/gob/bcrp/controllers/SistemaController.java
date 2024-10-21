package pe.gob.bcrp.controllers;

import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.ISistemaService;
import pe.gob.bcrp.services.IUploadFileService;
import pe.gob.bcrp.util.Util;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class SistemaController {

    @Autowired
    private ISistemaService sistemaService;

    @Autowired
    private IUploadFileService uploadFileService;


   /**
    * Metodo Listar todos los Sistemas
    * **/
   @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sistemas2")
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sistemas")
    public ResponseEntity<SistemaResponse> getAllSistemas(
            @RequestParam(name = "pageNumber", defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "50",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "nombre", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder,
            //@RequestParam(name = "codigo", required = false) String codigo,
            @RequestParam(name = "nombre", required = false) String nombre,
            @RequestParam(name = "version", required = false) String version
            ){
        log.info("INI - getAllSistemas | requestURL=entidades");
        try {

            SistemaResponse sistemaResponse=sistemaService.getAllSistemas(pageNumber, pageSize, sortBy, sortOrder,nombre,version);
            return new ResponseEntity<>(sistemaResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listarEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



   /**
    * Metodo Guardar sistema
    * **/
   @PreAuthorize("hasRole('ADMIN')")
   @PostMapping(value = "/sistema2")
   public ResponseEntity<ResponseDTO<SistemaFormDTO>> guardarSistema(@Validated @RequestBody SistemaFormDTO sistemaDTO){
        log.info("INFO - Guardar Sistema");
        ResponseDTO<SistemaFormDTO> response=new ResponseDTO();
        try {

            SistemaFormDTO sistemaDto=sistemaService.createSistema(sistemaDTO);
            response.setStatus(1);
            response.setMessage("El Sistema fue guardado de manera exitosa");
           // response.setBody(sistemaDto);

        }catch (Exception e ){
            log.error("ERROR - guardarSistema |", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al guardar el Sistema : "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Metodo Actualizar sistema por Id
     * **/
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/sistema2/{id}")
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
          //  response.setBody(sistemaDto);

        }catch (Exception e){
            log.error("ERROR - actualizarSistema |", e);
            response.setStatus(0);
            response.setMessage("Error al actualizar el Sistema");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Metodo Eliminar sistema por id
     * **/
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/sistema/{idSistema}")
    public ResponseEntity<ResponseDTO<?>> eliminarSistema(@PathVariable("idSistema") Integer idSistema) {
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<>();
        log.info("INFO - Eliminar Sistema");
        try {
            boolean eliminado = sistemaService.deleteSistemas(idSistema);
            if (!eliminado) {
                throw new ResourceNotFoundException("El sistema a eliminar con ID " + idSistema + " no existe");
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

    /**
     * Metodo Guardar sistema por parametros
     * **/
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/sistema", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> guardarSistema(//@RequestParam @NotNull String codigo,
                                                                      @RequestParam @NotNull String nombre,
                                                                      @RequestParam @NotNull String version,
                                                                      @RequestParam @NotNull String usuarioResponsable,
                                                                      @RequestParam @NotNull String usuarioResponsableAlt,
                                                                      @RequestParam(value = "imageLogoMain", required = false) MultipartFile multiLogoMain,
                                                                      @RequestParam(value = "imageLogoHead", required = false) MultipartFile multiLogoHead,
                                                                      @RequestParam @NotNull String url ) throws InvalidCredentialsException {
        log.info("INFO - Guardar Sistema ");
        ResponseDTO<SistemaFormDTO> response=new ResponseDTO();
        try {

            SistemaFormDTO sistemaDto=sistemaService.guardarSistemaPorParametro(nombre,version, multiLogoMain,multiLogoHead,url, usuarioResponsable,usuarioResponsableAlt);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/sistema", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> actualizarSistema(@RequestParam @NotNull Integer idSistema,
                                                                         //@RequestParam String codigo,
                                                                         @RequestParam @NotNull String nombre,
                                                                         @RequestParam @NotNull String version,
                                                                         @RequestParam @NotNull String usuarioResponsable,
                                                                         @RequestParam @NotNull String usuarioResponsableAlt,
                                                                         @RequestParam(value = "imageLogoMain", required = false) MultipartFile multiLogoMain,
                                                                         @RequestParam(value = "imageLogoHead", required = false) MultipartFile multiLogoHead,
                                                                         @RequestParam @NotNull String url) {
        log.info("INFO - Actualizar Sistema");
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<>();
        try {
            // Llamar al servicio de actualización
            SistemaFormDTO sistemaDto = sistemaService.actualizarSistemaPorParametro(idSistema, nombre, version, multiLogoMain, multiLogoHead, url,usuarioResponsable,usuarioResponsableAlt);

            response.setStatus(1);
            response.setMessage("El Sistema fue actualizado de manera exitosa");
            response.setBody(sistemaDto);

        } catch (ResourceNotFoundException e) {
            log.error("ERROR - El sistema no existe: ", e);
            response.setStatus(0);
            response.setMessage("El sistema con id " + idSistema + " no existe.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("ERROR - actualizarSistema | ", e);
            response.setStatus(0);
            response.setMessage("Error al actualizar el Sistema: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
