package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Modulo;

public interface IModuloRepository extends JpaRepository<Modulo, Integer> {


    public Page<Modulo> findByIsDeletedFalse(Pageable pageable);


    @Query("SELECT s FROM Modulo s WHERE " +
            "(:idSistema IS NULL OR s.sistema.idSistema = :idSistema) AND " +
            "s.isDeleted = false")
    Page<Modulo> findByFilters(@Param("idSistema") Integer idSistema,Pageable pageable);



}
