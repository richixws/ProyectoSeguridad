package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Modulo;
import pe.gob.bcrp.entities.Opcion;

public interface IOpcionRepository extends JpaRepository<Opcion, Integer> {

    public Page<Opcion> findByIsDeletedFalse(Pageable pageable);


    @Query("SELECT s FROM Opcion s WHERE " +
            "(:idSistema IS NULL OR s.modulo.sistema.idSistema  = :idSistema) AND " +
            "(:idModulo IS NULL OR  s.modulo.idModulo = :idModulo) AND " +
            "s.isDeleted = false")
    Page<Opcion> findByFilters(
                                @Param("idSistema") Integer idSistema,
                                @Param("idModulo") Integer idModulo,
                                Pageable pageable);


}
