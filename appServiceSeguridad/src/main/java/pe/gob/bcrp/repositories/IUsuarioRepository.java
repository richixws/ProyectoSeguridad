package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {


}
