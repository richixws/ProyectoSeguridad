
package pe.gob.bcrp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import pe.gob.bcrp.dto.EntidadDTO;
import pe.gob.bcrp.entities.DocumentoIdentidad;
import pe.gob.bcrp.entities.Entidad;
import pe.gob.bcrp.services.IEntidadService;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Date;

@WebMvcTest
public class EntidadControllerTest {


    /*@Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEntidadService entidadService;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void givenEntidadObject_whenCreateEntidad_thenReturnSavedEntidad() throws Exception {

        //given
        DocumentoIdentidad documentoIdentidad = new DocumentoIdentidad();
        documentoIdentidad.setTipoDocumentoIdentidad("dni");
        documentoIdentidad.setIdDocumentoIdentidad(1);

        Entidad entidad = Entidad.builder()
                .codExterno("codExterno")
                .nombre("nombreEntidad")
                .sigla("siglaEntidad")
                .documentoIdentidad(documentoIdentidad)
                .numeroDocumento("48345850")
                .isDeleted(false)
                .horaCreacion(null)
                .horaActualizacion(null)
                .horaDeEliminacion(null)
                .usuarioCreacion(null)
                .usuarioEliminacion(null)
                .usuarioActualizacion(null)
                .build();

        EntidadDTO entidadDTO=modelMapper.map(entidad, EntidadDTO.class);
        given(entidadService.saveEntidad(any(EntidadDTO.class))).willReturn(entidadDTO);

        // when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/entidad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entidadDTO)))
                .andReturn()
                .getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(objectMapper.readValue(response.getContentAsString(), EntidadDTO.class))
                .isEqualTo(entidadDTO);


    }*/
}
