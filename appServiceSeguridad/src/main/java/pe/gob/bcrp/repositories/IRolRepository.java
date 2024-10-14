package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Rol;

public interface IRolRepository  extends JpaRepository<Rol, Integer> {
}
