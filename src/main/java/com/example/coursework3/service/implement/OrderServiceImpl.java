package com.example.coursework3.service.implement;

import com.example.coursework3.model.Client;
import com.example.coursework3.model.Order;
import com.example.coursework3.model.Routes;
import com.example.coursework3.repository.OrderRepository;
import com.example.coursework3.repository.RoutesRepository;
import com.example.coursework3.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RoutesRepository routesRepository;
    @Override
    public void saveOrder(Order order) {
        // Логіка для збереження замовлення у базі даних
        orderRepository.save(order);
    }


    @Override
    public List<Order> getTicketHistoryByClientId(Long clientId) {
        // Логіка для отримання історії квитків користувача за його ідентифікатором
        return orderRepository.findByClientId(clientId);
    }
    @Override
    public void returnTicket(Long ticketId) {
        Order ticket = orderRepository.findById(ticketId).orElse(null);

        if (ticket != null) {
            // Отримати маршрут за ідентифікатором, який міститься в квитку
            Routes route = ticket.getRoute();

            if (route != null) {
                // Відняти кількість квитків у квитка від occupied_seats у маршрута
                int newOccupiedSeats = Math.max(0, route.getOccupied_seats() - ticket.getSeats());
                route.setOccupied_seats(newOccupiedSeats);

                // Зберегти оновлені значення маршрута в БД
                routesRepository.save(route);
            }
            // Логіка для видалення квитка та оновлення значень в БД
            orderRepository.deleteById(ticketId);
        }
    }
    @Override
    public void saveOrderWithClientIdAndRouteId(Order order, Long clientId, String routeId) {
        // Логіка для збереження замовлення з clientId та routeId у базі даних
        if (clientId != null) {
            Client client = new Client();
            client.setId(clientId);
            order.setClient(client);
        }

        if (routeId != null) {
            Routes route = routesRepository.findById(Integer.parseInt(routeId));
            order.setRoute(route);
        }

        orderRepository.save(order);
    }
}

