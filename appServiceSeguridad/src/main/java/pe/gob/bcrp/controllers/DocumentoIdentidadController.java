package pe.gob.bcrp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.bcrp.dto.DocumentoIdentidadDTO;
import pe.gob.bcrp.services.IDocumentoIdentidadService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins ="*", allowedHeaders = "*")
@Tag(name = "REST APIs Documento Identidad",description = "REST APIs - get All Documentos Identidad")
public class DocumentoIdentidadController {


    private IDocumentoIdentidadService documentoIdentidadService;

    public DocumentoIdentidadController(IDocumentoIdentidadService documentoIdentidadService) {
        this.documentoIdentidadService = documentoIdentidadService;
    }

    @Operation(summary = "get All Documento Identidad REST API", description = "Obtener todos los documentos de identidad de la base de datos")
    @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/documento/identidad")
    public ResponseEntity<List<DocumentoIdentidadDTO>> findAllDocumentoIdentidades() {
        log.info("INI - findAllDocumentoIdentidades | requestURL=documento");
        try {

            List<DocumentoIdentidadDTO> documentoIdentidadDto=documentoIdentidadService.findAllDocumentoIdentidades();
            return new ResponseEntity<>(documentoIdentidadDto, HttpStatus.OK);

        }catch (Exception e){
            log.error("ERROR -  findAllDocumentoIdentidades | requestURL=documento");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
