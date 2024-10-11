package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Entidad;

import java.util.List;

public interface IEntidadRepository extends JpaRepository<Entidad, Integer> {

    public List<Entidad> findByNombre(String nombre);

}
