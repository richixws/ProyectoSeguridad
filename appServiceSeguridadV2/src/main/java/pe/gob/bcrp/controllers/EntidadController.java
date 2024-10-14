package pe.gob.bcrp.controllers;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.services.IEntidadService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
public class EntidadController {

    @Autowired
    private IEntidadService entidadService;

    @GetMapping("/entidades")
    public ResponseEntity<List<EntidadDTO>> listarEntidades(){
        log.info("INI - listarEntidades | requestURL=entidades");
        List<EntidadDTO> entidadDto=entidadService.getEntidades();
        return ResponseEntity.ok(entidadDto);
    }

    @GetMapping("/entidades/{nombre}")
    public ResponseEntity<List<EntidadDTO>> listarEntidadesPorNombre(@PathVariable String nombre){
        log.info("INI - listarEntidadesPorNombre | requestURL=nombre");
        List<EntidadDTO> entidadDto=entidadService.getEntidadByNombre(nombre);
        return ResponseEntity.ok(entidadDto);
    }

    @PostMapping("/entidad")
    public  ResponseEntity<EntidadDTO> guardarEntidad(@RequestBody @Valid EntidadDTO entidadDto){
        log.info("INI - guardarEntidad | requestURL=entidadDto");
        EntidadDTO entidadDTO=entidadService.saveEntidad(entidadDto);
        return ResponseEntity.ok(entidadDTO);
    }









}
