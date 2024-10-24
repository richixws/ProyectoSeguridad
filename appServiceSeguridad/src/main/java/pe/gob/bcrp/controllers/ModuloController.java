package pe.gob.bcrp.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.bcrp.dto.DocumentoIdentidadDTO;
import pe.gob.bcrp.dto.ModuloDTO;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
public class ModuloController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/modulos")
    public ResponseEntity<List<ModuloDTO>> listarDocumentosIdentidad(){
        log.info("INI - listarDocumentosIdentidad | requestURL=entidades");
        try {
      //      List<DocumentoIdentidadDTO> listDocumentos=entidadService.getAllDocumentos();
      //      return new ResponseEntity<>(listDocumentos, HttpStatus.OK);
            return  null;
        }catch (Exception e){
            log.error("ERROR - listarEntidades | requestURL=entidades");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
