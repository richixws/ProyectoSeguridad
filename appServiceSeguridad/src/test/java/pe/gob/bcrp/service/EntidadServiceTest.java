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
import pe.gob.bcrp.dto.DocumentoIdentidadDTO;
import pe.gob.bcrp.dto.entidadDTO.EntidadDTO;
import pe.gob.bcrp.dto.entidadDTO.EntidadFormDTO;
import pe.gob.bcrp.dto.response.EntidadResponse;
import pe.gob.bcrp.entities.DocumentoIdentidad;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IDocumentoIdentidadRepository;
import pe.gob.bcrp.repositories.IEntidadRepository;
import pe.gob.bcrp.services.impl.EntidadServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntidadServiceTest {


    @Mock
    private IEntidadRepository entidadRepository;

    @Mock
    private IDocumentoIdentidadRepository documentoIdentidadRepository;

    @InjectMocks
    private EntidadServiceImpl entidadServices;

    private Entidad entidad;

    private DocumentoIdentidad documentoIdentidad;

    @Mock
    private ModelMapper modelMapper;
    private EntidadDTO entidadDto;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

        entidadDto = new EntidadDTO();
        entidadDto.setNombre("Entidad Test");
        entidadDto.setNumeroDocumento("12345678");
        entidadDto.setIdDocumento(2);


        DocumentoIdentidad documentoIdentida=new DocumentoIdentidad();
        documentoIdentida.setIdDocumentoIdentidad(6);
        documentoIdentida.setTipoDocumentoIdentidad("RUC");

        Entidad entidad=new Entidad();
        entidad.setDocumentoIdentidad(documentoIdentida);
        entidad.setNumeroDocumento("483458502268");
        entidad.setSigla("LM");
        entidad.setNombre("La Marina");
        entidad.setCodExterno("7919ec7a-2d73-4e2e-914d-79992a97beb7");
        entidad.setEstado(1);




    }

    /**@DisplayName("Obtener las entidades con filtros")
    @Test
    public void testGetAllEntidades_withFilters() {
        // Arrange
        Integer pageNumber = 0;
        Integer pageSize = 10;
        String sortBy = "nombre";
        String sortOrder = "asc";
        String name = "La Marina";
        Integer documentType = 6;
        String documentNumber = "483458502268";

        Sort sort = Sort.by(Sort.Order.asc(sortBy));
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);


        String nombreLowerCase = name != null ? name.toLowerCase() : null;

        //Entidad entidad = new Entidad();
        //entidad.setNombre("John");

        List<Entidad> listEntidades = Collections.singletonList(entidad);
        Page<Entidad> pageEntidades = new PageImpl<>(listEntidades);

        when(entidadRepository.findByFilters(nombreLowerCase, documentType, documentNumber, pageable)).thenReturn(pageEntidades);

        //when(modelMapper.map(any(Entidad.class),eq(EntidadFormDTO.class))).thenReturn(new EntidadFormDTO());
        when(modelMapper.map(any(Entidad.class), eq(EntidadFormDTO.class))).thenAnswer(invocation -> {
            Entidad entidad = invocation.getArgument(0);
            EntidadFormDTO dto = new EntidadFormDTO();
            dto.setTipoDocumento(entidad.getDocumentoIdentidad().getTipoDocumentoIdentidad());
            return dto;
        });



        EntidadResponse response = entidadServices.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder, name, documentType, documentNumber);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);

    }**/


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
        EntidadResponse response = entidadServices.getAllEntidades(pageNumber, pageSize, sortBy, sortOrder, nombre, tipoDocumento, numeroDocumento);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @DisplayName("Lanzar no encontrado cuando el documento no existe")
    @Test
    void saveEntidad_WhenDocumentDoesNotExist_LanceResourceNotFoundException() {
        // Arrange
        when(documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(anyInt(), eq(2)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> entidadServices.saveEntidad(entidadDto));
    }




}
