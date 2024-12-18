package pe.gob.bcrp.controllers;

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
import pe.gob.bcrp.dto.PerfilDTO;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.dto.response.PerfilResponse;
import pe.gob.bcrp.dto.validacion.ValidationGroups;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IPerfilService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "Perfil",description = "Operaciones del Perfil - listar Perfiles, Guardar Prefil, Actualizar Perfil, eliminar Perfil")
public class PerfilController {

    private IPerfilService perfilService;

    public PerfilController(IPerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @Operation(summary = "Listar Perfiles ", description = "Obtener la lista de todos los Perfiles de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/perfiles")
    public ResponseEntity<PerfilResponse> getAllPerfiles(
            @RequestParam(name = "pageNumber", defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "idPerfil", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(name = "systemId", required = false) Integer idSistema,
            @RequestParam(name = "profileId", required = false) Integer idPerfil,
            @RequestParam(name = "name",     required = false) String name){

        log.info("INI - getAllPerfiles | requestURL=perfiles");
        try {

            PerfilResponse perfilResponse=perfilService.getAllPerfiles(pageNumber, pageSize, sortBy, sortOrder,
                    idSistema,idPerfil, name);
            return new ResponseEntity<>(perfilResponse, HttpStatus.OK);

        }catch (Exception e){
            log.error("ERROR - getAllPerfiles | requestURL=perfiles{}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar Perfil", description = "Guarda el Perfil en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/perfil")
    public  ResponseEntity<ResponseDTO<PerfilDTO>> savePerfil(@Validated(ValidationGroups.OnCreate.class) @RequestBody  PerfilDTO perfilDTO){

        log.info("INI - guardarPerfil | requestURL=perfil");
        ResponseDTO<PerfilDTO> response=new ResponseDTO<>();
        try {
            PerfilDTO moduloDto=perfilService.savePerfil(perfilDTO);
            response.setStatus(1);
            response.setMessage("El Perfil fue guardado de manera exitosa");
            // response.setBody(entidadDTO);
        } catch (ResourceNotFoundException e) {
            log.error("ERROR - Perfil No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e){
            log.error("ERROR - guardarPerfil | requestURL=perfil{}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Perfil", description = "Actualiza el Perfil en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/perfil/{profileId}")
    public ResponseEntity<ResponseDTO<PerfilDTO>> updatePerfil(@Validated(ValidationGroups.OnUpdate.class) @RequestBody  PerfilDTO perfilDTO,
                                                               @PathVariable("profileId") Integer idPerfil){
        log.info("INI - upodatePerfil | requestURL=perfil");
        ResponseDTO<PerfilDTO> response=new ResponseDTO<>();
        try {
            PerfilDTO perfilDto=perfilService.updatePerfil(perfilDTO,idPerfil);
            response.setStatus(1);
            response.setMessage("El perfil fue actualizado exitosamente");

        }
         catch (IllegalArgumentException e) {
            log.error("ERROR | update perfil{}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);

        }catch (ResourceNotFoundException e) {
            log.error("ERROR - update perfil No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error("ERROR - update Perfil | requestURL=perfil{}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Elimina Perfil", description = "Elimina el perfil por el IdPerfil de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/perfil/{profileId}")
    public ResponseEntity<ResponseDTO<PerfilDTO>> deletePerfil(@PathVariable("profileId") Integer idPerfil){
        ResponseDTO<PerfilDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarPerfil | requestURL=perfil");
        try {

            boolean eliminado=perfilService.deletePerfil(idPerfil);
            if(!eliminado){
                throw new ResourceNotFoundException("El perfil no existe, ya se encuentra eliminado");
            }
            response.setStatus(1);
            response.setMessage("El perfil ha sido eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error("ERROR - eliminarPerfil No encontrado {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR - eliminarPerfil() {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
