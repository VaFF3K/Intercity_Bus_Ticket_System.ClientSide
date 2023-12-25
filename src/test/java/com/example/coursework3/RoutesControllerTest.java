package com.example.coursework3;

import com.example.coursework3.controller.RoutesController;
import com.example.coursework3.model.Routes;
import com.example.coursework3.service.RoutesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RoutesControllerTest {

    @Mock
    private RoutesService routesService;

    @InjectMocks
    private RoutesController routesController;

    @Test
    void testGetRouteById_Success() throws Exception {
        Routes route = new Routes();

        when(routesService.getRouteById(anyInt())).thenReturn(route);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(routesController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/routes/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

}