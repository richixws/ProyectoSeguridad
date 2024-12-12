package pe.gob.bcrp.controllers;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.usuarioDTO.RegistroCreateUsuarioDTO;
import pe.gob.bcrp.dto.usuarioDTO.RegistroUsuarioDTO;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.dto.usuarioDTO.UsuarioFormDTO;
import pe.gob.bcrp.dto.response.UsuarioResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IUsuarioService;
import pe.gob.bcrp.services.impl.UsuarioServiceImpl;

import java.sql.SQLException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
//@PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
@Tag(name = "Usuario",description = "Operaciones Usuario - Listar Usuarios, Guardar Usuario, Actualizar Usuario, Inhabilitar Usuario, Cargar Usuarios")
public class UsuarioController {

    private IUsuarioService usuarioService;

    public UsuarioController( IUsuarioService usuarioService ) {
        this.usuarioService = usuarioService;
    }



    @Operation(summary = "Listar Usuarios", description = "Obtener la lista de todos los usuarios de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> getAllUsuarios(
            @RequestParam(name = "pageNumber",  defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    defaultValue = "50", required = false) Integer pageSize,
            @RequestParam(name = "sortBy",      defaultValue = "idUsuario", required = false) String sortBy,
            @RequestParam(name = "sortOrder",   defaultValue = "desc", required = false) String sortOrder,
            @RequestParam(name = "documentType",   required = false)   Integer tipoDocumento,
            @RequestParam(name = "documentNumber", required = false)   String  numeroDocumento,
            @RequestParam(name = "names",         required = false)   String  nombres,
            @RequestParam(name = "scope",          required = false)   String  ambito,
            @RequestParam(name = "systemId",       required = false)   Integer idSistema
    ){
        log.info("INI - getAllUsuario | requestURL=usuarios");
        try {

            UsuarioResponse usurioResponse=usuarioService.getAllUsuarios(pageNumber, pageSize, sortBy, sortOrder,nombres,tipoDocumento,numeroDocumento,idSistema,ambito);
            return new ResponseEntity<>(usurioResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listar Usuarios | requestURL=usuarios");
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    //@Operation(summary = "Cargar Usuarios", description = "Cargar la lista de usuarios desde archivo csv.")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @Hidden
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload/usuarios")
    public ResponseEntity<ResponseDTO<?>>  uploadUsuarios(@RequestParam("file") MultipartFile file) {

        log.info("INI - UploadsUsuario | uploadUsuarios=upload/usuarios");
        ResponseDTO<List<UsuarioFormDTO>> response=new ResponseDTO<>();
        try {

            List<UsuarioFormDTO> list=usuarioService.uploadUserCsv(file);
            response.setStatus(1);
            response.setMessage("Se cargo exitosamente las lista de usuarios de archivo csv.");
            response.setBody(list);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
         catch (BadRequestException e) {
            log.error("ERROR - al cargar archivo"+e.getMessage());
             response.setStatus(0);
             response.setMessage("Error al cargar archivo "+e.getMessage() );
            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
        }catch (RuntimeException e){
            log.error(" ERROR - uploadUsuarios | requestURL=usuarios ");
            response.setStatus(0);
            response.setMessage(e.getMessage() );
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @Operation(summary = "Guardar Usuario", description = "Guarda el usuario en la base de datos")
    @ApiResponse(responseCode = "201",description = "HTTP Status 201 CREATED")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/usuario",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<RegistroCreateUsuarioDTO>> saveUsuario(@Valid @ModelAttribute RegistroCreateUsuarioDTO registroUsuarioDTO,
                                                                             @RequestParam(value = "sustento", required = false) MultipartFile sustento) throws InvalidCredentialsException {


        log.info("INI - guardarUsuario | requestURL=usuarios");
        ResponseDTO<RegistroCreateUsuarioDTO> response=new ResponseDTO<>();

        try {

            validarArchivoSustento(sustento);
            RegistroCreateUsuarioDTO newUsuarioFormDTO=usuarioService.guardarUsuario(registroUsuarioDTO.getTipoDocumento(),
                                                                                     registroUsuarioDTO.getNumeroDocumento(),
                                                                                     registroUsuarioDTO.getNombres(),
                                                                                     registroUsuarioDTO.getApePaterno(),
                                                                                     registroUsuarioDTO.getApeMaterno(),
                                                                                     registroUsuarioDTO.getCorreoElectronico(),
                                                                                     registroUsuarioDTO.getAmbito(), sustento);

            response.setStatus(1);
            response.setMessage("El Usuario fue guardado de manera existosa");

        } catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        catch (Exception e){
            log.error(" ERROR - add Usuario | requestURL=usuarios ");
            response.setStatus(0);
            response.setMessage("Error al guardar el Usuario "+e.getMessage() );
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }



    @Operation(summary = "Actualizar Usuario", description = "Actualiza el usuario en la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuario/{userId}")
    public ResponseEntity<ResponseDTO<RegistroUsuarioDTO>> updateUsuario(@PathVariable("userId") Integer idUsuario,  @RequestBody RegistroUsuarioDTO registroUsuarioDTO) {
        log.info("INI - Editar Usuario | requestURL=usuario");
        ResponseDTO<RegistroUsuarioDTO> response=new ResponseDTO<>();
        try {
            RegistroUsuarioDTO updUsuarioFormDTO=usuarioService.updateUsuario(idUsuario,registroUsuarioDTO);
            response.setStatus(1);
            response.setMessage("El Usuario fue actualizado de manera exitosa");

        }catch (IllegalArgumentException e) {
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch (ResourceNotFoundException e) {
            log.error("ERROR - updateUsuario No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al Actualizar el Usuario "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error(" ERROR - Editar Usuario | requestURL=usuario ");
            response.setStatus(0);
            response.setMessage("Error al actualizar el Usuario "+e.getMessage() );
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @Operation(summary = "Inhabilitar Usuario", description = "Inhabilita el usuario en particular de la base de datos")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PutMapping("/usuario/{userId}/inhabilitar")
    public ResponseEntity<ResponseDTO<RegistroUsuarioDTO>> InhabilitarUsuario(@PathVariable("userId") Integer idusuario) {
          log.info("INI - Eliminar Usuario | requestURL=IdUsuario");
          ResponseDTO<RegistroUsuarioDTO> response=new ResponseDTO<>();
          try {
              boolean eliminado=usuarioService.deleteUsuario(idusuario);
              if(!eliminado){
                  throw new ResourceNotFoundException("El Usuario a Inhabilitar no existe");
              }
              response.setStatus(1);
              response.setMessage("El Usuario fue Inhabilitado de manera exitosa");
              return new ResponseEntity<>(response,HttpStatus.OK);

          }catch (ResourceNotFoundException e){
              log.error("ERROR - deleteUsuario No encontrado {}", e.getMessage());
              response.setStatus(0);
              response.setMessage(e.getMessage());
              return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

          }catch (Exception e){
              log.error(" ERROR - delete Usuario | requestURL=IdUsuario ");
              response.setStatus(0);
              response.setMessage("Error al Inhabilitar el Usuario "+e.getMessage() );
              return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
          }
    }

    @Operation(summary = "Asigne Rol a usuario", description = "Asigna rol con todos los perfiles al usuario")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PostMapping("/usuario/{userId}/{releId}")
    public ResponseEntity<ResponseDTO<RegistroUsuarioDTO>> AsignarRolToUsuario(@PathVariable("userId") Integer idusuario,
                                                                               @PathVariable("roleId") Integer idRol) {
        log.info("INI - Asigne Rol a usuario | requestURL=IdUsuario,IdRol");
        ResponseDTO<RegistroUsuarioDTO> response=new ResponseDTO<>();
        try {
            boolean result = usuarioService.AddProfilesToUsuario(idusuario, idRol);
            if(!result){
                throw new ResourceNotFoundException("Ocurrió un error al asignar rol a usuario");
            }
            response.setStatus(1);
            response.setMessage("Se asigno el rol al usuario");
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            log.error("ERROR - AsignarRolToUsuario {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

        } catch (DataAccessException e){
            log.error("ERROR - AddProfilesToUsuario() "+e.getMessage());
            response.setStatus(0);
            response.setMessage("No se puede volver a asignar el mismo rol al usuario");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

        } catch (Exception e){
            log.error(" ERROR - AsignarRolToUsuario | requestURL=IdUsuario ");
            response.setStatus(0);
            response.setMessage("Error al asignar rol a usuario "+e.getMessage() );
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Operation(summary = "Asigne Perfil a usuario", description = "Asigna perfil al usuario")
    @ApiResponse(responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PostMapping("/usuario/{userId}/{profileId}")
    public ResponseEntity<ResponseDTO<RegistroUsuarioDTO>> AsignarPerfilToUsuario(@PathVariable("userId") Integer idusuario,
                                                                               @PathVariable("profileId") Integer idPerfil) {
        log.info("INI - Asigne perfil a usuario | requestURL=IdUsuario,IdPerfil");
        ResponseDTO<RegistroUsuarioDTO> response=new ResponseDTO<>();
        try {
            boolean result = usuarioService.AddProfileToUsuario(idusuario, idPerfil);
            if(!result){
                throw new ResourceNotFoundException("Ocurrió un error al asignar perfil a usuario");
            }
            response.setStatus(1);
            response.setMessage("Se asigno el perfil al usuario");
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (ResourceNotFoundException e){
            log.error("ERROR - AsignarPerfilToUsuario {}", e.getMessage());
            response.setStatus(0);
            response.setMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

        } catch (DataAccessException e){
            log.error("ERROR - AsignarPerfilToUsuario() "+e.getMessage());
            response.setStatus(0);
            response.setMessage("No se puede volver a asignar el mismo perfil al usuario");
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);

        } catch (Exception e){
            log.error(" ERROR - AsignarPerfilToUsuario | requestURL=IdUsuario ");
            response.setStatus(0);
            response.setMessage("Error al asignar perfil a usuario "+e.getMessage() );
            return new ResponseEntity<>(response,HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    // validar archivo sustento
    private void validarArchivoSustento(MultipartFile sustento) {

        if (sustento == null || sustento.isEmpty()) {
            throw new IllegalArgumentException("El archivo de sustento es obligatorio.");
        }

        // Validar el tamaño máximo permitido (por ejemplo, 2MB)
         if (sustento.getSize() > 2 * 1024 * 1024) { // 2MB en bytes
           throw new IllegalArgumentException("El archivo de sustento no debe exceder los 2MB.");
         }

        // Validar el tipo de archivo (por ejemplo, aceptar solo PDF o imágenes)
        String contentType = sustento.getContentType();
        if (!"application/pdf".equals(contentType)) {
            throw new IllegalArgumentException("El archivo de sustento debe ser en formato PDF");
        }
    }


}
