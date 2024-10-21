package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.DocumentoIdentidad;

public interface IDocumentoIdentidadRepository extends JpaRepository<DocumentoIdentidad, Integer> {

    DocumentoIdentidad findByTipoDocumentoIdentidad(String tipoDocumentoIdentidad);
}
