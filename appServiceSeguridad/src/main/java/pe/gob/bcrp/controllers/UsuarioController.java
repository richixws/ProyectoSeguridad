package pe.gob.bcrp.controllers;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.UsuarioDTO;
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
    public ResponseEntity<List<UsuarioDTO>> listaUsuarios() {
          log.info("INI - listaUsuarios | requestURL=usuarios");
          List<UsuarioDTO> usuarioDTO=usuarioService.getUsuarios();
          return ResponseEntity.ok(usuarioDTO);
    }

    @PostMapping("/usuario")
    public ResponseEntity<UsuarioDTO> addUsuario(@RequestBody UsuarioDTO usuarioDTO) {

          UsuarioDTO newUsuarioDTO=usuarioService.save(usuarioDTO);
          return ResponseEntity.ok(newUsuarioDTO);
    }

    @PutMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDTO> editUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) {

        UsuarioDTO usuarioUpdate=usuarioService.updateUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioUpdate);
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<UsuarioDTO> deleteUsuario(@PathVariable Integer id) {
          usuarioService.delete(id);
          return ResponseEntity.ok().build();
    }



}
