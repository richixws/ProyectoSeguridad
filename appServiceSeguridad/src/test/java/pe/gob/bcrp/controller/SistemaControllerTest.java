package pe.gob.bcrp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pe.gob.bcrp.services.ISistemaService;

@WebMvcTest
public class SistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISistemaService sistemaService;

    @Autowired
    private ObjectMapper objectMapper;

   @Test
   public void givenSistemaObject_WhenCrear(){

   }

}
