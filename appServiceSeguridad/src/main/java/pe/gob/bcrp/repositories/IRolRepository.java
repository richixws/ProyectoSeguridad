package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.entities.Rol;

import java.util.Optional;

public interface IRolRepository  extends JpaRepository<Rol, Integer> {

    public Page<Rol> findByIsDeletedFalse(Pageable pageable);
    Optional<Rol> findFirstByNombreContainingIgnoreCase(String nombre);
    //boolean existsBy(String numeroDocumento);
    boolean existsByNombreIgnoreCaseAndAndIdRolNot(String nombre,Integer idRol);

    @Query("SELECT s FROM Rol s WHERE " +
            "(:nombre IS NULL OR LOWER(s.nombre) LIKE %:nombre%) AND " +
            "(:idSistema IS NULL OR s.sistema.idSistema  = :idSistema) AND " +
            "(:idRol IS NULL OR  s.idRol = :idRol) ")
            //"AND s.isDeleted = false")
    Page<Rol> findByFilters(
                                    @Param("idSistema") Integer idSistema,
                                    @Param("idRol") Integer idRol,
                                    @Param("nombre") String nombre,
                                    Pageable pageable);


}
