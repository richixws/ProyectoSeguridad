package pe.gob.bcrp.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.moduloDTO.ModuloDTO;
import pe.gob.bcrp.dto.response.ModuloResponse;
import pe.gob.bcrp.dto.validacion.ValidationGroups;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IModuloService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "Modulo",description = "Operaciones de Modulo - listar Modulo, Guardar Modulo, Actualizar Modulo, Eliminar Modulo")
public class ModuloController {


    private IModuloService moduloService;

    public ModuloController(IModuloService moduloService ) {
        this.moduloService = moduloService;
    }

    @Operation(summary = "Listar Modulos", description = "Obtener la lista de todos los Modulos de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modulos")
    public ResponseEntity<ModuloResponse> getAllModulos(
            @RequestParam(name = "pageNumber",  defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    defaultValue = "10",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy",      defaultValue = "idModulo", required = false) String sortBy,
            @RequestParam(name = "sortOrder",   defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(name = "systemId", required = false) Integer idSistema,
            @RequestParam(name = "moduleName",     required = false) String name
    ){

        log.info("INI - getAllModulos | requestURL=modulos");
        try {

            ModuloResponse moduloResponse=moduloService.getAllModulos(pageNumber, pageSize, sortBy, sortOrder,idSistema, name);//,nombre
            return new ResponseEntity<>(moduloResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - getAllModulos | requestURL=modulos"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Guardar Modulo", description = "Guarda el Modulo en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/modulo")
    @JsonView(Views.Create.class)
    public  ResponseEntity<ResponseDTO<ModuloDTO>> saveModulo( @Validated(ValidationGroups.OnCreate.class) @RequestBody  ModuloDTO moduloDTO){

        log.info("INI - guardarEntidad | requestURL=entidadDto");
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        try {
            ModuloDTO moduloDto=moduloService.saveModulo(moduloDTO);
            response.setStatus(1);
            response.setMessage("El Modulo fue guardado de manera exitosa");
            // response.setBody(entidadDTO);

        }catch (ResourceNotFoundException e){
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e){
            log.error("ERROR - guardarEntidad | requestURL=entidadDto");
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Modulo", description = "Actualiza el Modulo en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/modulo/{moduleId}")
    @JsonView(Views.Update.class)
    public  ResponseEntity<ResponseDTO<ModuloDTO>> updateModulo( @Validated(ValidationGroups.OnUpdate.class) @RequestBody  ModuloDTO moduloDTO,
                                                                 @PathVariable("moduleId") Integer idModulo){

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
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Eliminar Modulo", description = "Elimina el Modulo por el IdModulo de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/modulo/{moduleId}")
    public ResponseEntity<ResponseDTO<ModuloDTO>> deleteModulo(@PathVariable("moduleId") Integer idModulo){
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        log.info("INI - eliminarModulo | requestURL=moduloDto");
        try {


            boolean eliminado= moduloService.deleteModulo(idModulo);
            if(!eliminado){
                throw new ResourceNotFoundException("El Modulo no existe, ya se encuentra eliminado");
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
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }



}
