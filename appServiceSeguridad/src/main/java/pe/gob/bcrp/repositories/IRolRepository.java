package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Opcion;
import pe.gob.bcrp.entities.Rol;

public interface IRolRepository  extends JpaRepository<Rol, Integer> {

    public Page<Rol> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT s FROM Rol s WHERE " + "(:idSistema IS NULL OR s.sistema.idSistema  = :idSistema) AND " +
                                             "(:idRol IS NULL OR  s.idRol = :idRol) AND " +
                                             "s.isDeleted = false")
    Page<Rol> findByFilters(
                                    @Param("idSistema") Integer idSistema,
                                    @Param("idRol") Integer idRol,
                                    Pageable pageable);


}
