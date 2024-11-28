package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Usuario;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

   Optional<Usuario> findByUsuario(String usuario);
   //Optional<Usuario> findByUsuarioAndEstadoAndIsDeletedFalse(String usuario, String estado);

   public Page<Usuario> findByIsDeletedFalse(Pageable pageable);


  @Query("SELECT s FROM Usuario s WHERE " +
            "(:nombres IS NULL OR LOWER(s.persona.nombres) = LOWER(:nombres)) AND " +
            "(:tipoDocumento IS NULL OR s.persona.tipoDocumento.idDocumentoIdentidad = :tipoDocumento) AND " +
            "(:numeroDocumento IS NULL OR s.persona.numeroDocumento = :numeroDocumento) AND " +
            "(:ambito IS NULL OR LOWER(s.ambito) = LOWER(:ambito)) AND " +
            "s.isDeleted = false")
    Page<Usuario> findByFilters(@Param("nombres") String nombres,
                                @Param("tipoDocumento") Integer tipoDocumento,
                                @Param("numeroDocumento") String numeroDocumento,
                                @Param("ambito") String ambito,
                                Pageable pageable);



  // obtiene la lista de usuarios asociados a un sistema
   @Query("SELECT u FROM Usuario u " +
           "JOIN PerfilUsuario pu ON u.idUsuario = pu.usuario.idUsuario " +
           "JOIN Perfil p ON pu.perfil.idPerfil = p.idPerfil " +
           "JOIN Rol r ON p.rol.idRol = r.idRol " +
           "JOIN Sistema s ON r.sistema.idSistema = s.idSistema " +
           "WHERE s.idSistema = :idSistema")
   Page<Usuario> findBySistemaId(@Param("idSistema") Integer idSistema, Pageable pageable);




}
