package pe.gob.bcrp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.dto.RolDTO;
import pe.gob.bcrp.dto.RolFormDTO;
import pe.gob.bcrp.dto.response.RolResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IRolService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "Rol",description = "Operaciones de Rol - Listar Roles, Guardar Rol, Actualizar Rol, Eliminar Rol")
public class RolController {


    private IRolService rolService;

    public RolController(IRolService rolService) {
        this.rolService = rolService;
    }

    @Operation(summary = "Listar Roles", description = "Obtener la lista de todos los Roles de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/roles")
    public ResponseEntity<RolResponse> getAllRoles(
            @RequestParam(name = "pageNumber", defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "idRol", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(name = "idSistema", required = false) Integer idSistema,
            @RequestParam(name = "idRol", required = false) Integer idRol ){

        log.info("INI - getAllRoles | requestURL=roles");
        try {

            RolResponse rolResponse=rolService.getAllRoles(pageNumber, pageSize, sortBy, sortOrder,idSistema,idRol);
            return new ResponseEntity<>(rolResponse, HttpStatus.OK);

        }catch (Exception e){
            log.error("ERROR - getAllRoles | requestURL=roles{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Guardar Rol", description = "Guarda el Rol en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rol")
    public  ResponseEntity<ResponseDTO<RolFormDTO>> saveRol(@Valid @RequestBody RolFormDTO rolDto){

        log.info("INI - saveRol | requestURL=rol");
        ResponseDTO<RolFormDTO> response=new ResponseDTO<>();
        try {
            RolFormDTO moduloDto=rolService.saveRole(rolDto);
            response.setStatus(1);
            response.setMessage("El Rol fue guardado de manera exitosa");
            // response.setBody(entidadDTO);

        }catch (Exception e){
            log.error("ERROR - saveRol | requestURL=rol{}", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al guardar el Rol "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Rol", description = "Actualiza el Rol por el IdRol en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/rol/{idRol}")
    public ResponseEntity<ResponseDTO<RolFormDTO>> updateRol(@Valid @RequestBody  RolFormDTO rolDTO,
                                                             @PathVariable("idRol") Integer idRol){
        log.info("INI - updateRol | requestURL=rol");
        ResponseDTO<RolFormDTO> response=new ResponseDTO<>();
        try {
            RolFormDTO rolDto=rolService.updateRole(rolDTO,idRol);
            response.setStatus(1);
            response.setMessage("El Rol fue actualizado exitosamente");

        }catch ( ResourceNotFoundException e) {
            log.error("ERROR - update Rol No encontrado {}", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al actualizar el rol "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.equals("ERROR - updateRol | requestURL=rol"+e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al actualizar el  "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    @Operation(summary = "Eliminar Rol", description = "Elimina el rol por el IdRol de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/rol/{idRol}")
    public ResponseEntity<ResponseDTO<RolDTO>> deleteRol(@PathVariable("idRol") Integer idRol){
        ResponseDTO<RolDTO> response=new ResponseDTO<>();
        log.info("INI - deleteRol | requestURL=rol");
        try {

            boolean eliminado=rolService.deleteRole(idRol);
            if(!eliminado){
                throw new ResourceNotFoundException("El rol no existe, ya se encuentra eliminado");
            }
            response.setStatus(1);
            response.setMessage("El rol ha sido eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteRol Not found {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR - deleteRol() {}", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al eliminar Rol: "+e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
