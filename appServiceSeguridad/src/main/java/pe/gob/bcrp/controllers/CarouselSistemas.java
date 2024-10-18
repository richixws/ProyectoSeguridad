package pe.gob.bcrp.controllers;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.services.ISistemaService;

import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/v1")
public class CarouselSistemas {


    @Autowired
    private ISistemaService sistemaService;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/catalogo/sistemas")
    public ResponseEntity<List<SistemaDTO>> listarCarouselSistemas() {
        log.info("Listando lista de catalogo de sistemas");
        try {

            List<SistemaDTO> listSistemas=sistemaService.getSistemaCarousel();
            return ResponseEntity.ok().body(listSistemas);

        }catch (Exception e) {
            log.error(" ERROR - listar sistemas"+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
}
