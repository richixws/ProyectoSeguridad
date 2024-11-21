package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Perfil;

import java.util.Optional;

public interface IPerfilRepository  extends JpaRepository<Perfil, Integer> {

    public Page<Perfil> findByIsDeletedFalse(Pageable pageable);

    //Optional<Perfil> findByNombreContainingIgnoreCaseIsDeletedFalse(String nombre);

    @Query("SELECT s FROM Perfil s WHERE " +
            "(:idSistema IS NULL OR s.rol.sistema.idSistema  = :idSistema) AND " +
            "(:idPerfil IS NULL OR  s.idPerfil = :idPerfil) AND " +
            "s.isDeleted = false")
    Page<Perfil> findByFilters(
                                @Param("idSistema") Integer idSistema,
                                @Param("idPerfil")  Integer idPerfil,
                                Pageable pageable);


}
