package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.gob.bcrp.entities.PerfilUsuario;
import pe.gob.bcrp.entities.Usuario;

import java.util.List;

public interface IPerfilUsuarioRepository  extends JpaRepository<PerfilUsuario, Integer> {

   // List<PerfilUsuario> findByIdUsuario (Integer usuario);
   //@Query("SELECT pu FROM PerfilUsuario pu WHERE pu.usuario.idUsuario = :idUsuario")
   //List<PerfilUsuario> findByIdUsuario(@Param("idUsuario") Integer idUsuario);

    @Query(value = "SELECT * FROM SW_PERFIL_USUARIO WHERE id_usuario = :idUsuario", nativeQuery = true)
    List<PerfilUsuario> findByIdUsuario(@Param("idUsuario") Integer idUsuario);
}
