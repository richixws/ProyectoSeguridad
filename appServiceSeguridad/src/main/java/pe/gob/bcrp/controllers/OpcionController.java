package pe.gob.bcrp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.opcionDTO.OpcionDTO;
import pe.gob.bcrp.dto.response.OpcionResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IOpcionService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "Opcion",description = "Operaciones de la Opcion - listar Opciones, Guardar Opcion, Actualizar Opcion, Eliminar Opcion")
public class OpcionController {


    private IOpcionService opcionService;

    public OpcionController(IOpcionService opcionService) {
        this.opcionService = opcionService;
    }

    /**
     *  Listar las opciones
     * @param pageNumber
     * @param pageSize
     * @param sortBy
     * @param sortOrder
     * @param idSistema
     * @param idModulo
     * @return
     */
    @Operation(summary = "Listar Opciones", description = "Obtener la lista de todos las opciones de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/opciones")
    public ResponseEntity<OpcionResponse> getAllOpciones(
            @RequestParam(name = "pageNumber",  defaultValue = "0",     required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    defaultValue = "10",    required = false) Integer pageSize,
            @RequestParam(name = "sortBy",      defaultValue = "idOpcion", required = false) String sortBy,
            @RequestParam(name = "sortOrder",   defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(name = "systemId",   required = false) Integer idSistema,
            @RequestParam(name = "moduleId",    required = false) Integer idModulo,
            @RequestParam(name = "name",     required = false) String name){

        log.info("INI - getAllOpciones | requestURL=opciones");
        try {

            OpcionResponse opcionResponse=opcionService.getAllOpciones(pageNumber, pageSize, sortBy, sortOrder,
                    idSistema,idModulo,name);
            return new ResponseEntity<>(opcionResponse, HttpStatus.OK);

        }catch (Exception e){
            log.error("ERROR - getAllOpciones | requestURL=opciones{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Guardar  Opcion
     * @param opcionDTO
     * @return
     */
    @Operation(summary = "Guardar Opcion", description = "Guarda la Opcion en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/opcion")
    public  ResponseEntity<ResponseDTO<OpcionDTO>> saveOpcion(@Valid @RequestBody  OpcionDTO opcionDTO){

        log.info("INI - guardarOpcion | requestURL=opcion");
        ResponseDTO<OpcionDTO> response=new ResponseDTO<>();
        try {
            OpcionDTO moduloDto=opcionService.saveOpcion(opcionDTO);
            response.setStatus(1);
            response.setMessage("La opcion fue guardado de manera exitosa.");
            //response.setBody(entidadDTO);

        }catch (ResourceNotFoundException e) {
            log.error("ERROR - Opcion no encontrado {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("ERROR - guardarEntidad | requestURL=entidadDto");
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Opcion", description = "Actualiza la Opcion en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/opcion/{optionId}")
    public ResponseEntity<ResponseDTO<OpcionDTO>> updateOpcion(@Valid @RequestBody  OpcionDTO opcionDTO,
                                                                @PathVariable("optionId") Integer idOpcion){
        log.info("INI - upodateOpcion | requestURL=opcion");
        ResponseDTO<OpcionDTO> response=new ResponseDTO<>();
        try {
            OpcionDTO opcionDto=opcionService.updateOpcion(opcionDTO,idOpcion);
            response.setStatus(1);
            response.setMessage("La opcion fue actualizado exitosamente.");

        }catch (ResourceNotFoundException e) {
            log.error("ERROR - update Opcion No encontrado" + e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.equals("ERROR - update Opcion | requestURL=opcion"+e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     *  Eliminar Opcion por IdOpcion
     * @param idOpcion
     * @return
     */
    @Operation(summary = "Eliminar Opcion", description = "Elimina la Opcion por el IdOpcion de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/opcion/{optionId}")
    public ResponseEntity<ResponseDTO<OpcionDTO>> deleteOpcion(@PathVariable("optionId") Integer idOpcion){
        ResponseDTO<OpcionDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarOpcion | requestURL=opcion");
        try {

            boolean eliminado=opcionService.deleteOpcion(idOpcion);
            if(!eliminado){
                throw new ResourceNotFoundException("La opción no existe, ya se encuentra eliminado.");
            }
            response.setStatus(1);
            response.setMessage("La opcion ha sido eliminado con éxito.");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error("ERROR - eliminarOpcion No encontrado "+e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR - eliminarOpcion() {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }
}
