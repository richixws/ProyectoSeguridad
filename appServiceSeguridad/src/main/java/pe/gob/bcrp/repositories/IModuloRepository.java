package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.entities.Sistema;

import java.util.Optional;

public interface IModuloRepository extends JpaRepository<Modulo, Integer> {


    //public Page<Modulo> findByIsDeletedFalse(Pageable pageable);

    Optional<Modulo> findByNombreModuloContainingIgnoreCase(String nombre);

    boolean existsByNombreModuloIgnoreCaseAndAndIdModuloNot(String nombre,Integer idSistema);

    @Query("SELECT s FROM Modulo s WHERE " +
            "(:nombre IS NULL OR LOWER(s.nombreModulo) LIKE %:nombre%) AND " +
            "(:idSistema IS NULL OR s.sistema.idSystem = :idSistema)  ")
            //"AND s.isDeleted = false")
    Page<Modulo> findByFilters(@Param("idSistema") Integer idSistema, @Param("nombre") String nombre, Pageable pageable);



}
