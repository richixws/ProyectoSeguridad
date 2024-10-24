package pe.gob.bcrp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.entities.Persona;


public interface IPersonaRepository extends JpaRepository<Persona, Integer> {

    public Page<Persona> findByIsDeletedFalse(Pageable pageable);

    boolean existsByDocumentoIdentidad(String numeroDocumento);

    //Método para verificar si ya existe otra entidad con el mismo número de documento, excluyendo la actual por ID
    boolean existsByDocumentoIdentidadAndIdPersonaNot(String documentoIdentidad, Integer idEntidad);

    @Query("SELECT s FROM Persona s WHERE " + "(:nombre IS NULL OR s.nombres = :nombre) AND " + "s.isDeleted = false")
    Page<Persona> findByFilters(@Param("nombre") String nombre, Pageable pageable);


}
