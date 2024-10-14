package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Perfil;

public interface IPerfilRepository  extends JpaRepository<Perfil, Integer> {

}
