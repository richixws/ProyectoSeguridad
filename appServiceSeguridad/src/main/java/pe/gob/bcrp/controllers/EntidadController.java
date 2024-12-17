package pe.gob.bcrp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.entidadDTO.EntidadDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IEntidadService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "Entidad",description = "Operaciones de la Entidad  - Listar Entidades, Guardar Entidad, Actualizar Entidad, Eliminar Entidad")
public class EntidadController {


    private IEntidadService entidadService;

    public EntidadController(IEntidadService entidadService) {
        this.entidadService = entidadService;
    }

    //@Operation(summary = "find All Documento Identidad REST API", description = "Obtener la lista de los documentos de identidad de la base de datos")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/entidad/documentos")
    public ResponseEntity<List<DocumentoIdentidadDTO>> findAllDocumentoIdentidad(){
        log.info("INI - listarDocumentoIdentidad | requestURL=entidad");
        try {
            List<DocumentoIdentidadDTO> listDocumentos=entidadService.getAllDocumentos();
            return new ResponseEntity<>(listDocumentos, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listarDocumentoIdentidad | requestURL=entidad/documentos");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Listar las Entidades", description = "Obtener la lista de todos las Entidades de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/entidades")
    public ResponseEntity<EntidadResponse> getAllEntidades(
            @RequestParam(name = "pageNumber", defaultValue = "0",      required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",   defaultValue = "50",     required = false) Integer pageSize,
            @RequestParam(name = "sortBy",     defaultValue = "idEntidad", required = false) String sortBy,
            @RequestParam(name = "sortOrder",  defaultValue = "desc",    required = false) String sortOrder,
            @RequestParam(name = "name",     required = false) String nombre,
            @RequestParam(name = "documentType",   required = false) Integer tipoDocumento,
            @RequestParam(name = "documentNumber", required = false) String numeroDocumento
            ){
        log.info("INI - getAllEntidades | requestURL=entidades");
        try {

            EntidadResponse entidadResponse=entidadService.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder,nombre,tipoDocumento,numeroDocumento);
            return new ResponseEntity<>(entidadResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - getAllEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar Entidad", description = "Guarda la Entidad en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/entidad")
    @JsonView(Views.Create.class)
    public  ResponseEntity<ResponseDTO<EntidadDTO>> saveEntidad(@Valid @RequestBody  EntidadDTO entidadDto){

        log.info("INI - guardarEntidad | requestURL=entidadDto");
        ResponseDTO<EntidadDTO> response=new ResponseDTO();
        try {
            EntidadDTO entidadDTO=entidadService.saveEntidad(entidadDto);

            response.setStatus(1);
            response.setMessage("La Entidad fue guardado de manera exitosa");
           // response.setBody(entidadDTO);

        } catch (ResourceNotFoundException e){
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
          response.setStatus(0);
          response.setMessage(e.getMessage());
          return ResponseEntity.badRequest().body(response);
       }catch (Exception e){
            log.error("ERROR - guardarEntidad | requestURL=entidadDto");
            response.setStatus(0);
            response.setMessage("Error al guardar la Entidad "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Entidad", description = "Actualiza la Entidad en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/entidad/{entityId}")
    @JsonView(Views.Update.class)
    public ResponseEntity<ResponseDTO<EntidadDTO>> updateEntidad(@PathVariable("entityId") Integer idEntidad,
                                                                 @Validated @RequestBody EntidadDTO entidadDTO){
        log.info("INI - updateEntidad | requestURL=entidad");
        ResponseDTO<EntidadDTO> response=new ResponseDTO();
        try {

            EntidadDTO entidadDto=entidadService.updateEntidad(idEntidad, entidadDTO);
            response.setStatus(1);
            response.setMessage("la Entidad fue actualizado de manera exitosa");
           // response.setBody(entidadDTO);

        }catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);

        }catch (ResourceNotFoundException e) {
            log.error("ERROR - updateEntidad No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al Actualizar la Entidad, "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e){
            log.error("ERROR - updateEntidad | requestURL=entidad");
            response.setStatus(0);
            response.setMessage("Error al Actualizar la Entidad "+e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);

    }

    @Operation(summary = "Eliminar Entidad", description = "Elimina la Entidad por el IdEntidad de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/entidad/{entityId}")
    public ResponseEntity<ResponseDTO<EntidadDTO>> deleteEntidad(@PathVariable("entityId") Integer idEntidad){
        ResponseDTO<EntidadDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarEntidad | requestURL=entidadDto");
        try {

           boolean eliminado= entidadService.deleteEntidad(idEntidad);
           if(!eliminado){
               throw new ResourceNotFoundException("La Entidad no existe, ya se encuentra eliminado");
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
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }
    
}
