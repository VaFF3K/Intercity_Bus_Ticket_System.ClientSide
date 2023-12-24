package com.example.coursework3.controller;

import com.example.coursework3.model.Order;
import com.example.coursework3.repository.ClientRepository;
import com.example.coursework3.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/{clientId}/create")
    public String createOrder(@PathVariable Long clientId, @RequestParam String routeId, @RequestBody Order order) {
        System.out.println("Received order: " + order);
        System.out.println("Client ID in order: " + clientId);
        System.out.println("Route ID in order: " + routeId);

        orderService.saveOrderWithClientIdAndRouteId(order, clientId, routeId);
        return "Order created successfully!";
    }


    @GetMapping("/history/{clientId}")
    public ResponseEntity<List<Order>> getTicketHistory(@PathVariable Long clientId) {
        List<Order> ticketHistory = orderService.getTicketHistoryByClientId(clientId);
        return ResponseEntity.ok(ticketHistory);
    }


    @DeleteMapping("/{ticketId}/return")
    public ResponseEntity<String> returnTicket(@PathVariable Long ticketId) {
        try {
            // Отримайте квиток за його ідентифікатором та видаліть його
            orderService.returnTicket(ticketId);
            return ResponseEntity.ok("Квиток повернуто успішно.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Помилка при обробці запиту.");
        }
    }
}

