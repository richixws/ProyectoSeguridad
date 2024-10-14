package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Modulo;

public interface IModuloRepository extends JpaRepository<Modulo, Integer> {
}
