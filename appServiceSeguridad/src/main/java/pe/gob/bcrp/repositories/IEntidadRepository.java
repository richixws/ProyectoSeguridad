package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.entities.Sistema;

import java.util.List;
import java.util.Optional;

public interface IEntidadRepository extends JpaRepository<Entidad, Integer> {

    public Optional<Entidad> findByNombre(String nombre);
    public List<Entidad> findByIsDeletedFalse();
    public Page<Entidad> findByIsDeletedFalse(Pageable pageable);

}
