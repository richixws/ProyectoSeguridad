package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.DocumentoIdentidad;

import java.util.List;

public interface IDocumentoIdentidadRepository extends JpaRepository<DocumentoIdentidad, Integer> {

    DocumentoIdentidad findByTipoDocumentoIdentidad(String tipoDocumentoIdentidad);

    // Método que devuelve los documentos filtrados por grupo_documento = 2
    //List<DocumentoIdentidad> findByGrupoDocumento(Integer grupoDocumento);


}
