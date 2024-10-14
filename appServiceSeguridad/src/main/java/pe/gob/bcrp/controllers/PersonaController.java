package pe.gob.bcrp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.PersonaDTO;
import pe.gob.bcrp.services.IPersonaService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PersonaController {

    @Autowired
    private IPersonaService personaService;

    @GetMapping("/personas")
    public ResponseEntity<List<PersonaDTO>> listarPersonas() {

        List<PersonaDTO> listPersonas=personaService.getPersonas();
        return ResponseEntity.ok().body(listPersonas);
    }

    @PostMapping("/persona")
    public ResponseEntity<PersonaDTO> addPersona(@RequestBody PersonaDTO personaDTO) {

        PersonaDTO newPersonaDTO=personaService.save(personaDTO);
        return ResponseEntity.ok().body(newPersonaDTO);
    }

    @PutMapping("/persona/{id}")
    public ResponseEntity<PersonaDTO> editPersona(@PathVariable Integer id, @RequestBody PersonaDTO personaDTO) {

        PersonaDTO updatePersona=personaService.updatePersona(id,personaDTO);
        return ResponseEntity.ok().body(updatePersona);

    }

    @DeleteMapping("/persona/{id}")
    public ResponseEntity<PersonaDTO> deletePersona(@PathVariable Integer id) {
        personaService.delete(id);
        return ResponseEntity.ok().build();

    }





}
