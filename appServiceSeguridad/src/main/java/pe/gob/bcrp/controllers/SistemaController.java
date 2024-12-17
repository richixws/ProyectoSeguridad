package pe.gob.bcrp.controllers;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.SistemaResponse;
import pe.gob.bcrp.dto.sistemaDTO.RegistroSistemaDTO;
import pe.gob.bcrp.dto.sistemaDTO.SistemaFormDTO;
import pe.gob.bcrp.dto.validacion.ValidationGroups;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.ISistemaService;
import pe.gob.bcrp.services.IUploadFileService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Sistema",description = "Operaciones de Sistema - Listar Sistemas, Guardar Sistema, Actualizar Sistema,Eliminar Sistema, Listar Usuarios Responsables, Listar Estados Criticos")
public class SistemaController {


    private ISistemaService sistemaService;
    private IUploadFileService uploadFileService;


    public SistemaController(ISistemaService sistemaService, IUploadFileService uploadFileService) {
        this.sistemaService = sistemaService;
        this.uploadFileService = uploadFileService;
    }

    /**
     * Metodo Listar usuarios responsables del los Sistemas
     * **/
    //@Operation(summary = "Listar Usuarios Sistema", description = "Obtener la lista de los usuarios responsables del sistema de la base de datos")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sistema/usuarios")
    public ResponseEntity<List<UsuarioResponsableDTO>> findAllUsuariosResponsables(){

       log.info("INFO - listado de usuarios");
       try {
           List<UsuarioResponsableDTO> listarUsuariosResponsable=sistemaService.listarUsuariosResponsable();
           return new ResponseEntity<>(listarUsuariosResponsable,HttpStatus.OK);

       } catch (Exception e) {
           log.error("ERROR - listado de usuarios" +e.getMessage());
           throw new RuntimeException(e);
       }
    }

    /**
     * Metodo Listar estados del los Sistemas
     * **/
    //@Operation(summary = "Listar Estado Sistema", description = "Obtener la lista de los estados del sistema de la base de datos")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sistema/estados")
    public ResponseEntity<List<EstadoCriticoDto>> findAllEstadosCriticos(){

        log.info("INFO - listado de estados criticos");
        try {
            List<EstadoCriticoDto> listEstadosCriticos=sistemaService.listarEstadosCriticos();
            return new ResponseEntity<>(listEstadosCriticos,HttpStatus.OK);

        } catch (Exception e) {
            log.error("ERROR - listado de estados criticos" +e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

   /**
    * Metodo Listar todos los Sistemas
    * **/
    @Operation(summary = "Listar Sistemas", description = "Obtener la lista de todos los sistemas de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sistemas")
    public ResponseEntity<SistemaResponse> getAllSistemas(
            @RequestParam(name = "pageNumber", defaultValue = "0",    required = false)      Integer pageNumber,
            @RequestParam(name = "pageSize",   defaultValue = "50",   required = false)      Integer pageSize,
            @RequestParam(name = "sortBy",     defaultValue = "idSystem", required = false) String sortBy,
            @RequestParam(name = "sortOrder",  defaultValue = "desc", required = false)      String sortOrder,
            @RequestParam(name = "name", required = false) String nombre,
            @RequestParam(name = "version", required = false) String version
            ){
        log.info("INI - getAllSistemas | requestURL=entidades");
        try {

            SistemaResponse sistemaResponse=sistemaService.getAllSistemas(pageNumber, pageSize, sortBy, sortOrder,nombre,version);
            return new ResponseEntity<>(sistemaResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listarEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    /**
     * Metodo Eliminar sistema por idSistema
     * **/
    @Operation(summary = "Eliminar Sistema", description = "Elimina el sistema por el Id de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/sistema/{idSistema}")
    public ResponseEntity<ResponseDTO<?>> deleteSistema(@PathVariable("idSistema") Integer idSistema) {
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<>();
        log.info("INFO - Eliminar Sistema");
        try {
            boolean eliminado = sistemaService.deleteSistema(idSistema);
            if (!eliminado) {
                throw new ResourceNotFoundException("El sistema no existe, ya se encuentra eliminado");
            }else{
                response.setStatus(1);
                response.setMessage("El sistema ha sido eliminado con éxito");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

        } catch (ResourceNotFoundException e) {
            log.error("ERROR - eliminarSistema No encontrado| {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("ERROR - eliminarSistema | {}", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al eliminar el sistema");
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Metodo Guardar sistema por parametros
     * **/
    /**@Operation(summary = "Save Sistema REST API", description = "Guarda el Sistema en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/sistema", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> saveSistema(
                                                                      @RequestParam @NotNull String nombre,
                                                                      @RequestParam @NotNull String version,
                                                                      @RequestParam @NotNull String usuarioResponsable,
                                                                      @RequestParam          String usuarioResponsableAlt,
                                                                      @RequestParam          Integer idUsuarioResponsable,
                                                                      @RequestParam          Integer idUsuarioResponsableAlt,
                                                                      @RequestParam(value = "imageLogoMain", required = false) MultipartFile multiLogoMain,
                                                                      @RequestParam(value = "imageLogoHead", required = false) MultipartFile multiLogoHead,
                                                                      @RequestParam @NotNull String url,
                                                                      @RequestParam @NotNull String urlExterno,
                                                                      @RequestParam @NotNull Integer idEstadoCritico,
                                                                      @RequestParam @NotNull String unidOrganizacional) throws InvalidCredentialsException {
        log.info("INFO - Guardar Sistema ");
        ResponseDTO<SistemaFormDTO> response=new ResponseDTO();
        try {

            SistemaFormDTO sistemaDto=sistemaService.guardarSistema(nombre,version,
                                                                    multiLogoMain,multiLogoHead,url, usuarioResponsable,
                                                                    usuarioResponsableAlt,idUsuarioResponsable,idUsuarioResponsableAlt,
                                                                    urlExterno,idEstadoCritico,unidOrganizacional);
            response.setStatus(1);
            response.setMessage("El Sistema fue guardado de manera exitosa");
            response.setBody(sistemaDto);

        }catch (Exception e ){
            log.error("ERROR - guardar Sistema ", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al guardar el Sistema : "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }  **/

    @Operation(summary = "Guardar Sistema", description = "Guarda el Sistema en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/sistema", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @JsonView(Views.Create.class)
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> saveSistema(@Validated(ValidationGroups.OnCreate.class) @ModelAttribute  RegistroSistemaDTO registroSistemaDTO,
                                                                   @RequestParam(value = "logoMain", required = false) MultipartFile[] multiLogoMain,
                                                                   @RequestParam(value = "logoHead", required = false) MultipartFile[] multiLogoHead
                                                                     ) throws InvalidCredentialsException {
        log.info("INFO - Guardar Sistema ");
        ResponseDTO<SistemaFormDTO> response=new ResponseDTO();
        try {

            validarLogo(multiLogoMain);
            validarLogo(multiLogoHead);
            SistemaFormDTO sistemaDto=sistemaService.guardarSistema(registroSistemaDTO.getName(),registroSistemaDTO.getVersion(),
                    multiLogoMain[0],multiLogoHead[0],registroSistemaDTO.getUrl()
                    ,registroSistemaDTO.getIdUserResponsible(),registroSistemaDTO.getIdUserResponsibleAlternate(),registroSistemaDTO.getUrlExternal(),
                    registroSistemaDTO.getIdStateCritical(),registroSistemaDTO.getUnitOrganizational(),registroSistemaDTO.getEstate());
            response.setStatus(1);
            response.setMessage("El Sistema fue guardado de manera exitosa");
            //response.setBody(sistemaDto);

        }
        catch (IllegalArgumentException e) {
             response.setStatus(0);
             response.setMessage(e.getMessage());
             return ResponseEntity.badRequest().body(response);
        }catch (Exception e ){
            log.error("ERROR - guardar Sistema ", e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al guardar el Sistema : "+ e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Metodo Actualizar sistema
     * **/
   /**@Operation(summary = "Update Sistema REST API", description = "Actualiza el Sistema en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/sistemas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> updateSistema(   @RequestParam @NotNull Integer idSistema,
                                                                         @RequestParam @NotNull String nombre,
                                                                         @RequestParam @NotNull String version,
                                                                         @RequestParam @NotNull String usuarioResponsable,
                                                                         @RequestParam @NotNull String usuarioResponsableAlt,
                                                                         @RequestParam @NotNull Integer idUsuarioResponsable,
                                                                         @RequestParam          Integer idUsuarioResponsableAlt,
                                                                         @RequestParam(value = "imageLogoMain", required = false) MultipartFile multiLogoMain,
                                                                         @RequestParam(value = "imageLogoHead", required = false) MultipartFile multiLogoHead,
                                                                         @RequestParam @NotNull String url,
                                                                         @RequestParam @NotNull String urlExterno,
                                                                         @RequestParam @NotNull Integer idEstadoCritico,
                                                                         @RequestParam @NotNull String unidOrganizacional) {
        log.info("INFO - Actualizar Sistema");
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<>();
        try {
            // Llamar al servicio de actualización
            SistemaFormDTO sistemaDto = sistemaService.actualizarSistema(idSistema, nombre, version, multiLogoMain,
                                                                        multiLogoHead, url,usuarioResponsable,usuarioResponsableAlt,
                                                                        idUsuarioResponsable,idUsuarioResponsableAlt,
                                                                        urlExterno,idEstadoCritico,unidOrganizacional);

            response.setStatus(1);
            response.setMessage("El Sistema fue actualizado de manera exitosa");
           //response.setBody(sistemaDto);

        } catch (ResourceNotFoundException e) {
            log.error("ERROR - El sistema no existe: ", e);
            response.setStatus(0);
            response.setMessage("El sistema con id " + idSistema + " no existe.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("ERROR - actualizarSistema | ", e);
            response.setStatus(0);
            response.setMessage("Error al actualizar el Sistema: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }**/


    @Operation(summary = "Actualizar Sistema", description = "Actualiza el Sistema en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/sistema", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated
    @JsonView(Views.Update.class)
    public ResponseEntity<ResponseDTO<SistemaFormDTO>> updateSistema(  @Validated(ValidationGroups.OnUpdate.class) @ModelAttribute  RegistroSistemaDTO registroSistemaDTO,
                                                                       // @RequestParam @NotNull Integer idSistema,
                                                                       @RequestParam(value = "logoMain", required = false) MultipartFile[] multiLogoMain,
                                                                       @RequestParam(value = "logoHead", required = false) MultipartFile[] multiLogoHead
                                                                       ) {
        log.info("INFO - Actualizar Sistema");
        ResponseDTO<SistemaFormDTO> response = new ResponseDTO<>();
        try {

            validarLogo(multiLogoMain);
            validarLogo(multiLogoHead);


            SistemaFormDTO sistemaDto = sistemaService.actualizarSistema(registroSistemaDTO.getIdSystem(), registroSistemaDTO.getName(),
                    registroSistemaDTO.getVersion(), multiLogoMain[0], multiLogoHead[0], registroSistemaDTO.getUrl(),
                    registroSistemaDTO.getIdUserResponsible(),registroSistemaDTO.getIdUserResponsibleAlternate(),
                    registroSistemaDTO.getUrlExternal(),registroSistemaDTO.getIdStateCritical(),registroSistemaDTO.getUnitOrganizational(),registroSistemaDTO.getEstate());

            response.setStatus(1);
            response.setMessage("El Sistema fue actualizado de manera exitosa");
            //response.setBody(sistemaDto);

        }
        catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        catch (ResourceNotFoundException e) {
            log.error("ERROR - El sistema no existe: ", e);
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            log.error("ERROR - actualizarSistema | ", e);
            response.setStatus(0);
            response.setMessage("Error al actualizar el Sistema: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // validar archivo logo
    private void validarLogo(MultipartFile[] logos) {

        if (logos == null || logos.length == 0) {
            throw new IllegalArgumentException("El logo es obligatorio.");
        }

        if (logos.length > 1) {
            throw new IllegalArgumentException("Sólo se permite un archivo de logo.");
        }

        MultipartFile logo=logos[0];

        if (logo.isEmpty()) {
            throw new IllegalArgumentException("El logo es obligatorio.");
        }

        // Validar el tamaño máximo permitido  2MB
        if (logo.getSize() > 2 * 1024 * 1024) { // 2MB en bytes
            throw new IllegalArgumentException("El logo no debe exceder los 2MB.");
        }

        // Validar el tipo de archivo  aceptar solo  imágenes
        String contentType = logo.getContentType();
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new IllegalArgumentException("El logo debe estar en formato de imagen (JPEG o PNG).");
        }
    }


}
