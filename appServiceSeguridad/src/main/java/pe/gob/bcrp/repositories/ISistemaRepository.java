package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Sistema;

import java.util.List;

public interface ISistemaRepository extends JpaRepository<Sistema, Integer> {

    public Sistema findByNombre(String nombre);

    public List<Sistema> findByIsDeletedFalse();

    public Page<Sistema> findByIsDeletedFalse(Pageable pageable);
}
