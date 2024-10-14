package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Sistema;

public interface ISistemaRepository extends JpaRepository<Sistema, Integer> {

}
