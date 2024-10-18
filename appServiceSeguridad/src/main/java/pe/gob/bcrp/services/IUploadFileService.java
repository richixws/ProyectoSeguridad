package pe.gob.bcrp.services;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.entities.Files;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public interface IUploadFileService {

    public Resource cargar(String nombreFoto)throws MalformedURLException;
    public String copiar(MultipartFile archivo)throws IOException;
    public Boolean eliminar(String nombreFoto);
    public Path getPath(String nombreFoto);

    /** metodos par acarga de archivo actual**/
    public String upload(MultipartFile multipartFile) throws IOException;
    public void delete(String nombreFile) throws IOException;

    public Files almacenarDatosFile(MultipartFile multipartFile, Integer idSistema) throws IOException;
    public Files updateDatosFile(MultipartFile multipartFile, Files files) throws IOException;

}
