package pe.gob.bcrp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.services.ISistemaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SistemaController {

    @Autowired
    private ISistemaService sistemaService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/catalogo/sistemas")
    public ResponseEntity<List<SistemaDTO>> listarSistemas() {

        List<SistemaDTO> listSistemas=sistemaService.getSistemas();
        return ResponseEntity.ok().body(listSistemas);

    }


}
