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
import pe.gob.bcrp.dto.entidadDTO.EntidadDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.services.impl.EntidadServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    public void testGetAllEntidades_withFilters() {
        // Arrange
        Integer pageNumber = 0;
        Integer pageSize = 10;
        String sortBy = "nombre";
        String sortOrder = "asc";
        String nombre = "John";
        Integer tipoDocumento = 1;
        String numeroDocumento = "12345678";

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Entidad entidad = new Entidad();
        entidad.setNombre("John");

        List<Entidad> entidades = Collections.singletonList(entidad);
        Page<Entidad> pageEntidades = new PageImpl<>(entidades);

        when(entidadRepository.findByFilters(nombre, tipoDocumento, numeroDocumento, pageable)).thenReturn(pageEntidades);
        //   when(modelMapper.map(entidad, EntidadFormDTO.class)).thenReturn(new EntidadFormDTO());


        EntidadResponse response = entidadService.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder, nombre, tipoDocumento, numeroDocumento);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);

    }


    @DisplayName("Obtener las entidades sin filtros")
    @Test
    public void testGetAllEntidades_withoutFilters() {
        // Arrange
        Integer pageNumber = 0;
        Integer pageSize = 10;
        String sortBy = "nombre";
        String sortOrder = "asc";
        String nombre = null;
        Integer tipoDocumento = null;
        String numeroDocumento = null;

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Entidad entidad = new Entidad();
        entidad.setNombre("John");

        List<Entidad> entidades = Collections.singletonList(entidad);
        Page<Entidad> pageEntidades = new PageImpl<>(entidades);

        when(entidadRepository.findAll(pageable)).thenReturn(pageEntidades);

        // Act
        EntidadResponse response = entidadService.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder, nombre, tipoDocumento, numeroDocumento);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

}
