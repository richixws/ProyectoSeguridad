package pe.gob.bcrp.services;

import pe.gob.bcrp.controllers.DocumentoIdentidad;
import pe.gob.bcrp.dto.DocumentoIdentidadDTO;

import java.util.List;

public interface IDocumentoIdentidadService {

    public List<DocumentoIdentidadDTO> findAllDocumentoIdentidades();
}
