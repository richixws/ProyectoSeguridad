package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Persona;

public interface IPersonaRepository extends JpaRepository<Persona, Integer> {



}
