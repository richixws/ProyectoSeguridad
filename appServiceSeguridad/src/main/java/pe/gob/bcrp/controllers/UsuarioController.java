package pe.gob.bcrp.controllers;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.log4j.Log4j2;
import org.apache.http.auth.InvalidCredentialsException;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.RegistroUsuarioDTO;
import pe.gob.bcrp.dto.ResponseDTO;
import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.dto.UsuarioFormDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.dto.response.UsuarioResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.services.IUsuarioService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", maxAge = 3600)
//@PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
public class UsuarioController {


    @Autowired
    private IUsuarioService usuarioService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> getAllUsuarios(
            @RequestParam(name = "pageNumber",  defaultValue = "0",  required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",    defaultValue = "50",   required = false) Integer pageSize,
            @RequestParam(name = "sortBy",      defaultValue = "persona.nombres", required = false) String sortBy,
            @RequestParam(name = "sortOrder",   defaultValue = "asc", required = false) String sortOrder,
            @RequestParam(name = "tipoDocumento",   required = false)   Integer tipoDocumento,
            @RequestParam(name = "numeroDocumento", required = false)   String numeroDocumento,
            @RequestParam(name = "nombres",   required = false)         String nombres,
            @RequestParam(name = "ambito",   required = false)         String ambito,
            @RequestParam(name = "idSistema", required = false)         Integer idSistema
    ){
        log.info("INI - getAllUsuario | requestURL=usuarios");
        try {

            UsuarioResponse usurioResponse=usuarioService.getAllUsuarios(pageNumber, pageSize, sortBy, sortOrder,nombres,tipoDocumento,numeroDocumento,idSistema,ambito);
            return new ResponseEntity<>(usurioResponse, HttpStatus.OK);
        }catch (Exception e){
            log.error("ERROR - listar Usuarios | requestURL=usuarios");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload/usuarios")
    public ResponseEntity<List<UsuarioFormDTO>>  uploadUsuarios(@RequestParam("file") MultipartFile file) {

        log.info("INI - UploadsUsuario | uploadUsuarios=upload/usuarios");
        ResponseDTO<UsuarioFormDTO> response=new ResponseDTO<>();
        try {

            List<UsuarioFormDTO> list=usuarioService.uploadUserCsv(file);
            return new ResponseEntity<>(list, HttpStatus.OK);


        }catch (Exception e){
            log.error(" ERROR - add Usuario | requestURL=usuarios ");
            response.setStatus(0);
            response.setMessage("Error al guardar el Usuario "+e.getMessage() );
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usuario")
    public ResponseEntity<ResponseDTO<RegistroUsuarioDTO>> guardarUsuario(@RequestParam @NotNull Integer tipoDocumento,
                                                                          @RequestParam @NotNull String numeroDocumento,
                                                                          @RequestParam @NotNull String nombres,
                                                                          @RequestParam @NotNull String apePaterno,
                                                                          @RequestParam @NotNull String apeMaterno,
                                                                          @RequestParam @NotNull String correoElectronico,
                                                                          @RequestParam @NotNull String ambito,
                                                                          @RequestParam(value = "sustento", required = false) MultipartFile sustento) throws InvalidCredentialsException {


        log.info("INI - guardarUsuario | requestURL=usuarios");
        ResponseDTO<RegistroUsuarioDTO> response=new ResponseDTO<>();
        try {
            RegistroUsuarioDTO newUsuarioFormDTO=usuarioService.guardarUsuario(tipoDocumento,
                                                                               numeroDocumento,
                                                                               nombres,
                                                                               apePaterno,
                                                                               apeMaterno,
                                                                               correoElectronico,
                                                                               ambito,
                                                                               sustento);
            response.setStatus(1);
            response.setMessage("El Usuario fue guardado de manera existosa");

        }catch (Exception e){
            log.error(" ERROR - add Usuario | requestURL=usuarios ");
            response.setStatus(0);
            response.setMessage("Error al guardar el Usuario "+e.getMessage() );
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/usuario/{idUsuario}")
    public ResponseEntity<ResponseDTO<UsuarioDTO>> updateUsuario(@PathVariable("idUsuario") Integer idUsuario,
                                                                @RequestBody RegistroUsuarioDTO registroUsuarioDTO) {
        log.info("INI - Editar Usuario | requestURL=usuario");
        ResponseDTO<UsuarioDTO> response=new ResponseDTO<>();
        try {
            RegistroUsuarioDTO updUsuarioFormDTO=usuarioService.updateUsuario(idUsuario,registroUsuarioDTO);
            response.setStatus(1);
            response.setMessage("El Usuario fue actualizado de manera exitosa");

        }catch (ResourceNotFoundException e) {
            log.error("ERROR - updateUsuario No encontrado " + e.getMessage());
            response.setStatus(0);
            response.setMessage("Error al Actualizar el Usuario "+e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        }catch (Exception e){
            log.error(" ERROR - Editar Usuario | requestURL=usuario ");
            response.setStatus(0);
            response.setMessage("Error al actualizar el Usuario "+e.getMessage() );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }



    @PutMapping("/usuario/{idUsuario}/inhabilitar")
    public ResponseEntity<ResponseDTO<UsuarioDTO>> InhabilitarUsuario(@PathVariable("idUsuario") Integer idusuario) {
          log.info("INI - Eliminar Usuario | requestURL=IdUsuario");
          ResponseDTO<UsuarioDTO> response=new ResponseDTO<>();
          try {
              boolean eliminado=usuarioService.deleteUsuario(idusuario);
              if(!eliminado){
                  throw new ResourceNotFoundException("El Usuario a Inhabilitar "+idusuario+" no existe");
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
              return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
          }
    }


}
