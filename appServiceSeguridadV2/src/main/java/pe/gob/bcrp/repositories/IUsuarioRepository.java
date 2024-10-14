package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.Usuario;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {

   Optional<Usuario> findByUsuario(String usuario);
  // @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.perfilUsuarios WHERE u.usuario = :username")
   //Optional<Usuario> findByUsuario(@Param("username") String username);

   //  Optional<Usuario> findByUsernameOrEmail(String username, String email);

   @Query("SELECT DISTINCT u FROM Usuario u " +
           "LEFT JOIN FETCH u.perfilUsuarios p " +
           "LEFT JOIN FETCH p.rol r " +
           "WHERE u.usuario = :username")
   Optional<Usuario> findByUsuarioWithPerfilesAndRoles(@Param("username") String username);
}
