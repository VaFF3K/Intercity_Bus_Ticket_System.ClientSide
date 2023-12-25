package com.example.coursework3;

import com.example.coursework3.DataTransfer.ClientDataTransfer;
import com.example.coursework3.controller.ClientController;
import com.example.coursework3.repository.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientController clientController;

    @Test
    void testRegisterUser_Success() throws Exception {
        ClientDataTransfer clientData = new ClientDataTransfer();
        clientData.setFirstName("John");
        clientData.setLastName("Doe");
        clientData.setEmail("john.doe@example.com");
        clientData.setPassword("password");

        when(clientRepository.existsByEmail(clientData.getEmail())).thenReturn(false);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();

        mockMvc.perform(post("/api/client/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clientData)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Registration successful\",\"success\":\"true\"}"));
    }

    @Test
    void testRegisterUser_EmailExists() throws Exception {
        ClientDataTransfer clientData = new ClientDataTransfer();
        clientData.setEmail("john.doe@example.com");

        when(clientRepository.existsByEmail(clientData.getEmail())).thenReturn(true);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(clientController).build();

        mockMvc.perform(post("/api/client/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(clientData)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"Email already exists\",\"success\":\"false\"}"));
    }


    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
