package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.DocumentoIdentidad;

import java.util.List;
import java.util.Optional;

public interface IDocumentoIdentidadRepository extends JpaRepository<DocumentoIdentidad, Integer> {

    Optional<DocumentoIdentidad> findByIdDocumentoIdentidadAndGrupoDocumento(Integer idDocumento, Integer grupoDocumento);

    DocumentoIdentidad findByTipoDocumentoIdentidad(String tipoDocumentoIdentidad);

    List<DocumentoIdentidad> findByGrupoDocumento(Integer grupoDocumento);
    // Método que devuelve los documentos filtrados por grupo_documento = 2
    //List<DocumentoIdentidad> findByGrupoDocumento(Integer grupoDocumento);


}
