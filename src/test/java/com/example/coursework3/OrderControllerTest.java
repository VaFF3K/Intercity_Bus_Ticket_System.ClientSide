package com.example.coursework3;

import com.example.coursework3.controller.OrderController;
import com.example.coursework3.model.Order;
import com.example.coursework3.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void testCreateOrder_Success() throws Exception {
        doNothing().when(orderService).saveOrderWithClientIdAndRouteId(any(), any(), any());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();

        mockMvc.perform(post("/api/tickets/1/create")
                        .param("routeId", "1")  // Змінюємо Id маршруту для перевірки
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new Order())))
                .andExpect(status().isOk())
                .andExpect(content().string("Order created successfully!"));
    }
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}