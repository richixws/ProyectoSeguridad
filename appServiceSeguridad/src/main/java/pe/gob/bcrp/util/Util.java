package pe.gob.bcrp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IUsuarioRepository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
@Slf4j
public class Util {


    @Autowired
    private IUsuarioRepository usuarioRepository;

    public  void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

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

    public  void guardar(MultipartFile archivo) throws IOException {
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
        /**Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user=usuarioRepository.findByUsuario(authentication.getName()).orElseThrow(()->new  ResourceNotFoundException("Usuario no encontrado con nombre"+authentication.getName()));
        return user;**/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();

            // Extraer el claim 'name'
            String name = jwt.getClaim("name");
            String username = jwt.getClaim("preferred_username");
            String firstName = jwt.getClaim("given_name");
            String lastName = jwt.getClaim("family_name");
            String email = jwt.getClaim("email");

            // Crear y devolver el usuario con el nombre extraído
            Usuario usuarioResp = new Usuario();
            Persona persona=new Persona();

            persona.setNombres(firstName);
            persona.setApellidoPaterno(lastName);
            persona.setCorreo(email);
            usuarioResp.setPersona(persona);
            usuarioResp.setUsuario(username);
            usuarioResp.setIdUsuario(1);
            return usuarioResp;
        }

        return null;

    }



}
