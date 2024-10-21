package pe.gob.bcrp.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.dto.DocumentoIdentidadDTO;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.entities.DocumentoIdentidad;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.repositories.IDocumentoIdentidadRepository;
import pe.gob.bcrp.services.IDocumentoIdentidadService;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@AllArgsConstructor
public class DocumentoIdentidadServiceImpl implements IDocumentoIdentidadService {

    private final ModelMapper modelMapper;

    private IDocumentoIdentidadRepository documentoIdentidadRepository;

    @Override
    public List<DocumentoIdentidadDTO> findAllDocumentoIdentidades() {
        try {
            log.info("INI - Service findAllDocumentoIdentidades");
            List<DocumentoIdentidad> listDocumentoIdentidad = documentoIdentidadRepository.findAll();
            return listDocumentoIdentidad.stream()
                    .map(documentoIdentidad -> modelMapper.map(documentoIdentidad, DocumentoIdentidadDTO.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("ERROR - Service findAllDocumentoIdentidades() " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
