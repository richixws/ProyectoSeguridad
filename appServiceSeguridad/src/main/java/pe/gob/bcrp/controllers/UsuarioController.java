package pe.gob.bcrp.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.services.IUsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('user_client_role') or hasRole('admin_client_role')")
public class UsuarioController {


    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> listaUsuarios() {

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
