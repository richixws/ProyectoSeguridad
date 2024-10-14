package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Perfil;
import pe.gob.bcrp.entities.PerfilUsuario;

public interface IPerfilRepository  extends JpaRepository<Perfil, Integer> {

   // PerfilUsuario findByPerfilIdUsuario(Integer id);

}
