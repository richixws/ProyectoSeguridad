package pe.gob.bcrp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pe.gob.bcrp.dto.SistemaDTO;
import pe.gob.bcrp.dto.SistemaFormDTO;
import pe.gob.bcrp.entities.Sistema;
import pe.gob.bcrp.services.ISistemaService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
public class SistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISistemaService sistemaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

   @Test
   public void givenSistemaObject_WhenCrearSistema_thenReturnSabedSistema() throws Exception {

       //given -  dado - condición previa
      /** Sistema sistema= Sistema.builder()
               .codigo("SYS01")
               .nombre("Sistema1")
               .version("V1")
               .url("http://sistema1.com")
               .logoMain("imagen.jpg")
               .logoHead("imagen2.jpg")
              // .isDeleted(false)
               .build();

       SistemaFormDTO sistemaDTO=modelMapper.map(sistema, SistemaFormDTO.class);
       BDDMockito.given(sistemaService.createSistema(ArgumentMatchers.any(sistemaDTO.getClass())))
               .willAnswer((invocation)-> invocation.getArguments());

       //when
       ResultActions response=mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/sistema")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(sistemaDTO)));

       //then
       response.andDo(MockMvcResultHandlers.print()).
               andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.codigo", CoreMatchers.is(sistema.getCodigo())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.nombre", CoreMatchers.is(sistema.getNombre())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.version", CoreMatchers.is(sistema.getVersion())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.url", CoreMatchers.is(sistema.getUrl())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.logoMain", CoreMatchers.is(sistema.getLogoMain())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.logoHead", CoreMatchers.is(sistema.getLogoHead())))
               .andExpect(MockMvcResultMatchers.jsonPath("$.isDeleted", CoreMatchers.is(sistema.isDeleted())));
   }**/

   }

}
