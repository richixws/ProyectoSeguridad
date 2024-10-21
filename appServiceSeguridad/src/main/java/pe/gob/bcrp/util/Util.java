package pe.gob.bcrp.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import pe.gob.bcrp.services.IUsuarioService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
@Slf4j
public class Util {


    @Autowired
    private IUsuarioRepository usuarioRepository;

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void guardar(MultipartFile archivo) throws IOException {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
        Path rutaArchivo = Paths.get("src/main/resources/static/images").resolve(nombreArchivo).toAbsolutePath();

        log.info("Ruta del archivo: " + rutaArchivo.toString());

        try {
            // Copiar el archivo a la ruta especificada
            Files.copy(archivo.getInputStream(), rutaArchivo);
        } catch (IOException e) {
            log.error("Error al guardar la imagen " + nombreArchivo, e);
            throw new IOException("Error al guardar el archivo: " + nombreArchivo, e);
        }
       // return nombreArchivo;
    }

    /**
     * Obtener el usuario logeado del sistema
     * @return
     */
    public Usuario getUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user=usuarioRepository.findByUsuario(authentication.getName()).orElseThrow(()->new  ResourceNotFoundException("Usuario no encontrado con nombre"+authentication.getName()));
        return user;

    }



}
