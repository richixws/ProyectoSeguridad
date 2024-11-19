package pe.gob.bcrp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.dto.response.PersonaResponse;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IPersonaService;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "REST APIs Persona",description = "REST APIs - get All Personas, save Persona, update Persona, delete Persona")
public class PersonaController {

    private IPersonaService personaService;

    public  PersonaController(IPersonaService personaService) {
        this.personaService = personaService;
    }

    @Operation(summary = "get All Personas REST API", description = "Obtener la lista de todos las personas de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/personas")
    public ResponseEntity<PersonaResponse> getAllPersonas(
            @RequestParam(name = "pageNumber",  defaultValue = "0",       required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    defaultValue = "50",      required = false) Integer pageSize,
            @RequestParam(name = "sortBy",      defaultValue = "nombres", required = false) String sortBy,
            @RequestParam(name = "sortOrder",   defaultValue = "asc",     required = false) String sortOrder,
            @RequestParam(name = "nombre", required = false) String nombre){
       log.info(" INI - getAllPersonas | requestUrl=personas");
       try {
           PersonaResponse entidadPersonas=personaService.getAllPersonas(pageNumber,pageSize,sortBy,sortOrder,nombre);
           return new ResponseEntity<>(entidadPersonas, HttpStatus.OK);
       }catch (Exception e){
           log.error(" ERROR - getAllPersonas | requestUrl=personas");
           return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
       }
    }

    @Operation(summary = "Save Persona REST API", description = "Guarda la persona en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/persona")
    public ResponseEntity<ResponseDTO<PersonaDTO>> savePersona(@Valid @RequestBody PersonaDTO personaDTO) {
         log.info(" INI - addPersona | requestUrl=persona");
         ResponseDTO<PersonaDTO> response=new ResponseDTO<>();
        try {
            PersonaDTO newPersonaDTO=personaService.addPersona(personaDTO);
            response.setStatus(1);
            response.setMessage("la Persona fue guardado con exito");
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }catch (Exception e){
            log.error(" ERROR - addPersona | requestUrl=persona");
            response.setStatus(0);
            response.setMessage("Error al guardar la Persona "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Update Persona REST API", description = "Actualiza la Persona en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/persona/{idPersona}")
    public ResponseEntity<ResponseDTO<PersonaDTO>> updatePersona(@PathVariable Integer idPersona,
                                                                @Valid @RequestBody PersonaDTO personaDTO) {
        log.info(" INI - updatePersona | requestUrl=persona/idpersona");
        ResponseDTO<PersonaDTO> response=new ResponseDTO<>();
        try {

            PersonaDTO updatePersona=personaService.updatePersona(idPersona,personaDTO);
            response.setStatus(1);
            response.setMessage("la Persona fue actualizado de manera exitosa");


        }catch (Exception e){
            log.error(" ERROR - updatePersona | requestUrl=persona/idpersona");
            response.setStatus(0);
            response.setMessage("Error al actualizar la Persona "+ e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @Operation(summary = "Delete Persona REST API", description = "Elimina la persona por el IdPersona de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @DeleteMapping("/persona/{idPersona}")
    public ResponseEntity<ResponseDTO<PersonaDTO>> deletePersona(@PathVariable Integer idPersona) {
        ResponseDTO<PersonaDTO> response=new ResponseDTO<>();
        log.info(" INI - deletePersona | requestUrl=persona/idpersona");
        try {

            boolean eliminado=personaService.deletePersona(idPersona);
            if(!eliminado){
                throw new ResourceNotFoundException("La Persona no existe, ya se encuentra eliminado" );
            }
            response.setStatus(1);
            response.setMessage("la Persona fue eliminado con exito");
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch (ResourceNotFoundException e){
            log.error(" ERROR - deletePersona | requestUrl=persona/idpersona"+e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error(" ERROR - deletePersona | requestUrl=persona/idpersona");
            response.setStatus(0);
            response.setMessage("Error al eliminar la Persona "+ e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
