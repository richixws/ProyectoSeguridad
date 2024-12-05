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
import pe.gob.bcrp.dto.areaDTO.AreaDTO;
import pe.gob.bcrp.dto.moduloDTO.ModuloDTO;
import pe.gob.bcrp.dto.response.AreaResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IAreaService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "Area", description = "Operaciones de Area - listar area, Guardar area, Actualizar area, Eliminar area")
public class AreaController {

    private IAreaService areaService;

    public AreaController(IAreaService areaService ) {
        this.areaService = areaService;
    }

    @Operation(summary = "Listar Areas", description = "Obtener la lista de todos las areas de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/areas")
    public ResponseEntity<AreaResponse> getAllAreas(
            @RequestParam(name = "pageNumber",  defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    defaultValue = "10",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy",      defaultValue = "idArea", required = false) String sortBy,
            @RequestParam(name = "sortOrder",   defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(name = "nombre", required = false) String nombre
            //@RequestParam(name = "idSistema", required = false) Integer idSistema
    ){

        log.info("INI - getAllAreas | requestURL=areas");
        try {

            AreaResponse areaResponse = areaService.getAllAreas(pageNumber, pageSize, sortBy, sortOrder, nombre);
            return new ResponseEntity<>(areaResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - getAllAreas | requestURL=modulos"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Guardar Area", description = "Guarda el area en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/area")
    public  ResponseEntity<ResponseDTO<ModuloDTO>> saveArea(@Valid @RequestBody AreaDTO areaDTO){

        log.info("INI - saveArea | requestURL=areaDTO");
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        try {
            AreaDTO areaResponse = areaService.saveArea(areaDTO);
            response.setStatus(1);
            response.setMessage("El Area fue guardado de manera exitosa");
        }catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch (Exception e){
            log.error("ERROR - saveArea | requestURL=entidadDto");
            response.setStatus(0);
            response.setMessage("Error al guardar el area "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar Area", description = "Actualiza el area en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/area/{id}")
    public  ResponseEntity<ResponseDTO<ModuloDTO>> updateArea(@Valid @RequestBody AreaDTO areaDTO,
                                                                @PathVariable("id") Integer id){

        log.info("INI - updateArea | requestURL=area/id");
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        try {
            AreaDTO araResponse = areaService.updateArea(id, areaDTO);
            response.setStatus(1);
            response.setMessage("El Area fue actualizado de manera exitosa");
        }catch ( ResourceNotFoundException e) {
            log.error("ERROR - update Area No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR -  update Area | requestURL=modulo");
            response.setStatus(0);
            response.setMessage("Error al guardar el Area "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Eliminar Area", description = "Elimina el Area por Id en la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/area/{id}")
    public ResponseEntity<ResponseDTO<ModuloDTO>> deleteArea(@PathVariable("id") Integer id){
        ResponseDTO<ModuloDTO> response=new ResponseDTO<>();
        log.info("INI - deleteArea | requestURL=moduloDto");
        try {

            boolean eliminado = areaService.deleteArea(id);
            if(!eliminado){
                throw new ResourceNotFoundException("El Area no existe, ya se encuentra eliminado");
            }
            response.setStatus(1);
            response.setMessage("El Modulo ha sido eliminado con éxito");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error("ERROR - eliminar Area No encontrado "+e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error("ERROR - deleteArea() "+e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al eliminar Area: "+e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
