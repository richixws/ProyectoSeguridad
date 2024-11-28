package pe.gob.bcrp.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.bcrp.dto.sistemaDTO.SistemaDTO;
import pe.gob.bcrp.services.ISistemaService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
//@Tag(name = "REST APIs Carousel",description = "REST APIs - find All Carousel Sistemas")
public class CarouselSistemas {

    private ISistemaService sistemaService;

    public CarouselSistemas(ISistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    //@Operation(summary = "find All Carousel REST API", description = "Obtener todos los Sistemas de carousel de la base de datos")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/catalogo/sistemas")
    public ResponseEntity<List<SistemaDTO>> findAllCarouselSistemas() {
        log.info("Listando lista de catalogo de sistemas");
        try {

            List<SistemaDTO> listSistemas=sistemaService.getSistemaCarousel();
            return ResponseEntity.ok().body(listSistemas);

        }catch (Exception e) {
            log.error(" ERROR - listar sistemas"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);

        }

    }
}
