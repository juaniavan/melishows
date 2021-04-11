package ar.com.juani.melishows.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import javax.annotation.Resource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ar.com.juani.melishows.config.WebConfig;
import ar.com.juani.melishows.service.CommandService;
import ar.com.juani.melishows.service.QueryService;

@WebMvcTest
@ContextConfiguration(classes = { ShowController.class, RestResponseEntityExceptionHandler.class, WebConfig.class })
class ControllerTests {

    @MockBean
    CommandService commandService;

    @MockBean
    QueryService queryService;

    @Test
    void contextLoads() {
        assertNotNull(commandService);
    }
    
    @Resource
    ShowController userController;

    @Resource
    private MockMvc mockMvc;

    @Test
    void givenWrongBodyFormatShouldReturn400() throws Exception {
        String jsonBody = "{\"name\": \"jorge\", \"email\" : \"jorge@domain.com\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/reserve")
          .content(jsonBody)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void givenGoodBodyFormatShouldReturn200() throws Exception {
        String jsonBody = "{\n" + 
                "    \"userName\": \"jorge\",\n" + 
                "    \"userId\": \"33222111\",\n" + 
                "    \"reservedSeats\": [1,2]" + 
                "    \n" + 
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("/reserve")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().is(200));

    }
    
    @Test
    void givenShowSearchWithValidAndInvalidParametersShouldRespond200and400() throws Exception {
       
        URI showsPath = URI.create("/shows");

        testValidParameters(showsPath);
        testInvalidParameters(showsPath);
    }

    @Test
    void givenShowingSearchWithValidAndInvalidParametersShouldRespond200and400() throws Exception {
        
        URI showsPath = URI.create("/showings");

        testValidParameters(showsPath);
        testInvalidParameters(showsPath);
    }

    private void testValidParameters(URI showsPath) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("orderBy", "theater")
                .param("sortDir", "asc")
                .param("priceFrom", "10000")
                .param("priceTo", "22233")
                .param("dateFrom", "01-03-2021")
                .param("dateTo", "01-04-2021"))
        .andExpect(MockMvcResultMatchers.status().is(200));

    }
    
        private void testInvalidParameters(URI showsPath) throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                    .param("orderBy", "sdfdswww"))
            .andExpect(MockMvcResultMatchers.status().is(400));

        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("sortDir", "sdfdswww"))
        .andExpect(MockMvcResultMatchers.status().is(400));

        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("priceFrom", "1222ff"))
        .andExpect(MockMvcResultMatchers.status().is(400));
        
        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("priceTo", "1112,43"))
        .andExpect(MockMvcResultMatchers.status().is(400));

        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("priceTo", "1112.43"))
        .andExpect(MockMvcResultMatchers.status().is(400));
        
        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("dateFrom", "2021/03/01"))
        .andExpect(MockMvcResultMatchers.status().is(400));
        
        mockMvc.perform(MockMvcRequestBuilders.get(showsPath)
                .param("dateTo", "01/02/2021"))
        .andExpect(MockMvcResultMatchers.status().is(400));
    }
}