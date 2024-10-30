package pe.gob.bcrp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.services.impl.EntidadServiceImpl;
import java.util.List;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntidadServiceTest {

    @Mock
    private IEntidadRepository entidadRepository;

    @InjectMocks
    private EntidadServiceImpl entidadService;

    private Entidad entidad;


    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);


    }

    @DisplayName("Obtener las entidades con filtros")
    @Test
    void testGetAllEntidadesWithFilters() {
        // Datos de prueba
        String nombre = "Entidad Prueba";
        Integer tipoDocumento = 1;
        String numeroDocumento = "12345678";
        Integer pageNumber = 0;
        Integer pageSize = 1;
        String sortBy = "nombre";
        String sortOrder = "asc";

        // Configurar Page y Pageable
        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Entidad entidad = new Entidad();
        entidad.setNombre(nombre);
        Page<Entidad> entidadPage = new PageImpl<>(List.of(entidad));

        when(entidadRepository.findByFilters(nombre, tipoDocumento, numeroDocumento, pageable)).thenReturn(entidadPage);

        // Configurar el mapeo de entidad a DTO
        EntidadDTO entidadDTO = new EntidadDTO();
     //   when(modelMapper.map(entidad, EntidadDTO.class)).thenReturn(entidadDTO);

        // Ejecutar el método
        EntidadResponse response = entidadService.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder, nombre, tipoDocumento, numeroDocumento);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(pageNumber, response.getPageNumber());
        assertEquals(pageSize, response.getPageSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isLastPage());

        // Verificar interacciones
        verify(entidadRepository, times(1)).findByFilters(nombre, tipoDocumento, numeroDocumento, pageable);
      //  verify(modelMapper, times(1)).map(entidad, EntidadDTO.class);
    }





    @DisplayName("Obtener las entidades sin filtros")
    @Test
    void testGetAllEntidadesWithoutFilters() {

        // Datos de prueba
        Integer pageNumber = 0;
        Integer pageSize = 1;
        String sortBy = "nombre";
        String sortOrder = "desc";

        // Configurar Page y Pageable
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Entidad entidad = new Entidad();
        Page<Entidad> entidadPage = new PageImpl<>(List.of(entidad));

        when(entidadRepository.findByIsDeletedFalse(pageable)).thenReturn(entidadPage);

        // Configurar el mapeo de entidad a DTO
        EntidadDTO entidadDTO = new EntidadDTO();
//        when(modelMapper.map(entidad, EntidadDTO.class)).thenReturn(entidadDTO);

        // Ejecutar el método
        EntidadResponse response = entidadService.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder, null, null, null);

        // Verificar el resultado
        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(pageNumber, response.getPageNumber());
        assertEquals(pageSize, response.getPageSize());
        assertEquals(1, response.getTotalElements());
        assertEquals(1, response.getTotalPages());
        assertTrue(response.isLastPage());

        // Verificar interacciones
        verify(entidadRepository, times(1)).findByIsDeletedFalse(pageable);
       // verify(modelMapper, times(1)).map(entidad, EntidadDTO.class);
    }

}
