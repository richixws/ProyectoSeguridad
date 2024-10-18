package pe.gob.bcrp.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.repositories.IFilesRepository;
import pe.gob.bcrp.repositories.ISistemaRepository;
import pe.gob.bcrp.services.IUploadFileService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class IUploadFileServiceImpl implements IUploadFileService {


    @Autowired
    private IFilesRepository iFilesRepository;

    @Autowired
    private ISistemaRepository isistemaRepository;


    private final String FOLDER = "src//main//resources//static//images//";
    private final static String DIRECTORIO_UPLOAD="uploads";
    private final String IMG_DEFAULT = "default.jpg";

    @Override
    public Resource cargar(String nombreFoto) throws MalformedURLException {

        Path rutaArchivo=getPath(nombreFoto);
        log.info(rutaArchivo.toString());

        Resource recurso=new UrlResource(rutaArchivo.toUri());
        if(!recurso.exists() && !recurso.isReadable()) {
            rutaArchivo= Paths.get("src/main/resources/static/images").resolve("no-usuario.png").toAbsolutePath();
            recurso=new UrlResource(rutaArchivo.toUri());
            log.error("error no se pudo cargar la imagen "+nombreFoto);
        }

        return recurso;
    }

    @Override
    public String copiar(MultipartFile archivo) throws IOException {

        String nombreArchivo= UUID.randomUUID().toString()+"_"+archivo.getOriginalFilename().replace(" ", "");
        Path rutaArchivo= getPath(nombreArchivo);
        log.info(rutaArchivo.toString());
        Files.copy(archivo.getInputStream(), rutaArchivo);
        return nombreArchivo;

    }

    @Override
    public Boolean eliminar(String nombreFoto) {

        if (nombreFoto != null && nombreFoto.length() > 0) {
            Path rutaFotoAnterior=Paths.get("uploads").resolve(nombreFoto).toAbsolutePath();
            File archivoFotoAnterior=rutaFotoAnterior.toFile();
            if (archivoFotoAnterior.exists() &&	archivoFotoAnterior.canRead()) {
                archivoFotoAnterior.delete();
                return true;
            }

        }
        return false;
    }

    @Override
    public Path getPath(String nombreFoto) {
        return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
    }

    /**
     *
     *  Metodo para guardar archivo imagen uso actual
     * @param multipartFile
     * @return
     * @throws IOException
     */

    public String upload (MultipartFile multipartFile) throws IOException {
        if (multipartFile!=null){
            byte [] bytes = multipartFile.getBytes();
            Path path = Paths.get(FOLDER+multipartFile.getOriginalFilename());
            Files.write(path, bytes);
            return multipartFile.getOriginalFilename();
        }
        return  IMG_DEFAULT;
    }

    /**public void delete(String nameFile){
        File file = new File(FOLDER+nameFile);
        file.delete();
    }**/
    public void delete(String filePath) throws IOException {
        // Verificar si el archivo existe antes de eliminarlo
        Path path = Paths.get(FOLDER + filePath);
        if (Files.exists(path)) {
            Files.delete(path);  // Eliminar el archivo si existe
        }
    }


    @Override
    public pe.gob.bcrp.entities.Files almacenarDatosFile(MultipartFile file, Integer idSistema) {
        if (file == null || file.isEmpty()) {
            return null; // Si el archivo no está presente, no hacer nada
        }

        // Generar un nombre único para evitar colisiones
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Crear la entidad FileEntity y asignar los valores
        pe.gob.bcrp.entities.Files  files = new pe.gob.bcrp.entities.Files();
        files.setIdIdentidad(idSistema);
        files.setHoraFechaFile(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()) );
       // files.setIdModulo();
       // files.setIdUsuario();

        files.setFilename(file.getOriginalFilename());
        files.setPath(FOLDER + file.getOriginalFilename()); // Ajusta según tu lógica de almacenamiento
        files.setExtension(getFileExtension(file.getOriginalFilename()));
        files.setMime(file.getContentType());
        files.setSizes((int) file.getSize());

        // Guardar la información del archivo en la base de datos
        return iFilesRepository.save(files);
    }

    @Override
    public pe.gob.bcrp.entities.Files updateDatosFile(MultipartFile multipartFile, pe.gob.bcrp.entities.Files filesNew) throws IOException {

        //uploadFileService.delete(files.getPath());
        //delete(filesNew.getPath());
         upload(multipartFile);
        // Luego, sube el nuevo archivo
        //uploadFileService.upload(newFile);
        // Actualizar los detalles del archivo
        filesNew.setFilename(multipartFile.getOriginalFilename());
        filesNew.setPath(FOLDER + multipartFile.getOriginalFilename());
        filesNew.setExtension(getFileExtension(multipartFile.getOriginalFilename()));
        filesNew.setMime(multipartFile.getContentType());
        filesNew.setSizes((int)multipartFile.getSize());
        // Guardar el archivo actualizado en la base de datos
        return iFilesRepository.save(filesNew);

    }

    /**
     * Método auxiliar para obtener la extensión de un archivo a partir de su nombre.
     * @param filename El nombre del archivo.
     * @return La extensión del archivo.
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
