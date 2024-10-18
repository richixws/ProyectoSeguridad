package pe.gob.bcrp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.gob.bcrp.entities.Files;

import java.util.List;

public interface IFilesRepository extends JpaRepository<Files, Integer> {

   public  Files findFileByIdIdentidadAndFilename(Integer id, String nombre);

   public List<Files> findAllByIdIdentidad(Integer id);
}
