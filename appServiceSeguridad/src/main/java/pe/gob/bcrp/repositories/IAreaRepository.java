package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.entities.Area;

import java.util.Optional;

@Repository
public interface IAreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a WHERE " +
            "(:nombre IS NULL OR LOWER(a.nombreArea) = LOWER(:nombre))")
    Page<Area> findByFilters(@Param("nombre") String nombre,
                                Pageable pageable);

    boolean existsByNombreAreaIgnoreCaseAndIdAreaNot(String nombre,Integer idSistema);

    Optional<Area> findByNombreAreaContainingIgnoreCase(String nombreArea);
}
