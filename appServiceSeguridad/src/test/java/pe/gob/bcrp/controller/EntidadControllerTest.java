
package pe.gob.bcrp.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
