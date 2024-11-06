package pe.gob.bcrp.controllers;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.services.StorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final StorageService storageService;


    @PostMapping("/upload")
    Map<String, String> upload(@RequestParam("file") MultipartFile multipartFile) {
        Map<String, String> fileData  = storageService.store(multipartFile);
        return fileData;
    }


    @GetMapping("/{filename}")
    ResponseEntity<Resource> getResource(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource(filename);
        String contentType = Files.probeContentType(resource.getFile().toPath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);
    }

}
