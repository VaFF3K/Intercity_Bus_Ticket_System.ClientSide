package com.example.coursework3.service;

import com.example.coursework3.model.Order;
import java.util.List;

public interface OrderService {

    void saveOrder(Order order);

    void returnTicket(Long ticketId);
    List<Order> getTicketHistoryByClientId(Long clientId);

    void saveOrderWithClientIdAndRouteId(Order order, Long clientId, String routeId);

}
