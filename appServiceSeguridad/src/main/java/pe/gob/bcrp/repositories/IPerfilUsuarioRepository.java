package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.PerfilUsuario;

import java.util.List;

public interface IPerfilUsuarioRepository  extends JpaRepository<PerfilUsuario, Integer> {

    List<PerfilUsuario> findByIdPerfilUsuario(Integer idUsuario);
}
