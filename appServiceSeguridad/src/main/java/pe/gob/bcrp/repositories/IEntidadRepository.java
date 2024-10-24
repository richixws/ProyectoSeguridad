package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.entities.Sistema;

import java.util.List;
import java.util.Optional;

public interface IEntidadRepository extends JpaRepository<Entidad, Integer> {

    public Optional<Entidad> findByNombre(String nombre);
    public List<Entidad> findByIsDeletedFalse();
    public Page<Entidad> findByIsDeletedFalse(Pageable pageable);

    boolean existsByNumeroDocumento(String numeroDocumento);

    boolean existsByNumeroDocumentoAndIdEntidadNot(String numeroDocumento, Integer idEntidad);


   // @Query("SELECT s FROM Entidad s WHERE " + "(:nombre IS NULL OR LOWER(s.nombre) = LOWER(:nombre)) AND " + "s.isDeleted = false")
   // Page<Entidad> findByFilters(@Param("nombre") String nombre, Pageable pageable);


    /**@Query("SELECT s FROM Entidad s WHERE " +
            "(:nombre IS NULL OR LOWER(s.nombre) = LOWER(:nombre)) AND " +
            "(:tipoDocumento IS NULL OR LOWER(s.documentoIdentidad.idDocumentoIdentidad) = LOWER(:tipoDocumento)) AND " +
            "(:numeroDocumento IS NULL OR s.numeroDocumento = :numeroDocumento) AND " +
            "s.isDeleted = false")**/
    @Query("SELECT s FROM Entidad s WHERE " +
            "(:nombre IS NULL OR LOWER(s.nombre) = LOWER(:nombre)) AND " +
            "(:tipoDocumento IS NULL OR s.documentoIdentidad.idDocumentoIdentidad = :tipoDocumento) AND " +
            "(:numeroDocumento IS NULL OR s.numeroDocumento = :numeroDocumento) AND " +
            "s.isDeleted = false")
    Page<Entidad> findByFilters(@Param("nombre") String nombre,
                                @Param("tipoDocumento") Integer tipoDocumento,
                                @Param("numeroDocumento") String numeroDocumento,
                                Pageable pageable);
}
