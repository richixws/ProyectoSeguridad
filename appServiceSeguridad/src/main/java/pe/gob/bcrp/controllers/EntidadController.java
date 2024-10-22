package pe.gob.bcrp.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.*;
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



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/entidad/documentos")
    public ResponseEntity<List<DocumentoIdentidadDTO>> listarDocumentosIdentidad(){
        log.info("INI - listarDocumentosIdentidad | requestURL=entidades");
        try {
            List<DocumentoIdentidadDTO> listDocumentos=entidadService.getAllDocumentos();
            return new ResponseEntity<>(listDocumentos, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listarEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/lista/entidades")
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/entidades")
    public ResponseEntity<EntidadResponse> getAllEntidades(
            @RequestParam(name = "pageNumber", defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "50",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "nombre", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(name = "nombre", required = false) String nombre){
        log.info("INI - getAllEntidades | requestURL=entidades");
        try {

            EntidadResponse entidadResponse=entidadService.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder,nombre);
            return new ResponseEntity<>(entidadResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listarEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/entidad/{nombre}")
    public ResponseEntity<EntidadDTO> buscarEntidadPorNombre(@PathVariable String nombre){
        log.info("INI - listarEntidadesPorNombre | requestURL=nombre");
        try {

            EntidadDTO entidadDto=entidadService.findEntidadByNombre(nombre);
            return new ResponseEntity<>(entidadDto, HttpStatus.OK);

        }catch (Exception e){
            log.error("ERROR - buscarEntidadPorNombre | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/entidad")
    public  ResponseEntity<ResponseDTO<EntidadDTO>> guardarEntidad(@Valid @RequestBody  EntidadDTO entidadDto,
                                                                          @RequestParam("idDocumento") Integer idDocumento
                                                                          //@RequestParam("tipoDocumento") String tipoDocumento
                                                                            ){

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //String nombreUsuario =  authentication.getName();

        log.info("INI - guardarEntidad | requestURL=entidadDto");
        ResponseDTO<EntidadDTO> response=new ResponseDTO();
        try {
            EntidadDTO entidadDTO=entidadService.saveEntidad(entidadDto,idDocumento);

            response.setStatus(1);
            response.setMessage("la Entidad fue guardado de manera exitosa");
           // response.setBody(entidadDTO);

        }catch (Exception e){
            log.error("ERROR - guardarEntidad | requestURL=entidadDto");
            response.setStatus(0);
            response.setMessage("Error al guardar la Entidad"+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/entidad/{idEntidad}")
    public ResponseEntity<ResponseDTO<EntidadDTO>> updateEntidad(@PathVariable("idEntidad") Integer idEntidad,
                                                                 @Validated @RequestBody EntidadDTO entidadDTO,
                                                                 @RequestParam("idDocumento") Integer IdDocumento){
        log.info("INI - updateEntidad | requestURL=entidad");
        ResponseDTO<EntidadDTO> response=new ResponseDTO();
        try {

            EntidadDTO entidadDto=entidadService.updateEntidad(idEntidad, entidadDTO,IdDocumento);
            response.setStatus(1);
            response.setMessage("la Entidad fue actualizado de manera exitosa");
           // response.setBody(entidadDTO);

        }catch (ResourceNotFoundException e) {
            log.error("ERROR - updateEntidad No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e){
            log.error("ERROR - updateEntidad | requestURL=entidad");
            response.setStatus(0);
            response.setMessage("Error al guardar la Entidad"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);

    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/entidad/{idEntidad}")
    public ResponseEntity<ResponseDTO<EntidadDTO>> eliminarEntidad(@PathVariable("idEntidad") Integer idEntidad){
        ResponseDTO<EntidadDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarEntidad | requestURL=entidadDto");
        try {

           boolean eliminado= entidadService.deleteEntidad(idEntidad);
           if(!eliminado){
               throw new ResourceNotFoundException("La Entidad a eliminar con Id "+idEntidad+" no existe");
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
