package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Sistema;

import java.util.List;

public interface ISistemaRepository extends JpaRepository<Sistema, Integer> {



    public List<Sistema> findByIsDeletedFalse();

    public Page<Sistema> findByIsDeletedFalse(Pageable pageable);


    // Consulta personalizada para buscar por código, nombre y versión
    @Query("SELECT s FROM Sistema s WHERE " +
           // "(:codigo IS NULL OR s.codigo = :codigo) AND " +
            "(:nombre IS NULL OR s.nombre = :nombre) AND " +
            "(:version IS NULL OR s.version = :version) AND " +
            "s.isDeleted = false")
    Page<Sistema> findByFilters(//@Param("codigo") String codigo,
                                @Param("nombre") String nombre,
                                @Param("version") String version,
                                Pageable pageable);


}
