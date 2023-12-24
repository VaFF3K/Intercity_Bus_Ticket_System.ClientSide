package com.example.coursework3.controller;

import com.example.coursework3.model.Routes;
import com.example.coursework3.repository.RoutesRepository;
import com.example.coursework3.service.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/routes")
public class RoutesController {

    @Autowired
    private RoutesRepository routesRepository;
    @Autowired
    private RoutesService routesService;

    @GetMapping("/all")
    public List<Routes> getAllRoutes() {
        return routesRepository.findAll();
    }

    @GetMapping("/{routeId}")
    public Routes getRouteById(@PathVariable int routeId) {
        return routesService.getRouteById(routeId);
    }

    @PutMapping("/occupySeats/{routeId}/{seats}")
    public ResponseEntity<String> occupySeats(@PathVariable int routeId, @PathVariable int seats) {
        try {
            Routes route = routesService.getRouteById(routeId);
            int occupiedSeats = Math.min(route.getOccupied_seats() + seats, route.getMax_seats());

            if (occupiedSeats >= route.getMax_seats()) {
                routesRepository.deleteById(routeId);
                return ResponseEntity.ok("Маршрут видалено, оскільки всі місця зайняті.");
            }

            route.setOccupied_seats(occupiedSeats);
            routesRepository.save(route);

            return ResponseEntity.ok("Місця успішно зайняті.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Помилка при обробці запиту.");
        }
    }


    @PutMapping("/{routeId}/updateOccupiedSeats")
    public ResponseEntity<Map<String, Integer>> updateOccupiedSeats(
            @PathVariable int routeId,
            @RequestParam int occupiedSeats
    ) {
        Optional<Routes> optionalRoute = Optional.ofNullable(routesRepository.findById(routeId));

        if (optionalRoute.isPresent()) {
            Routes route = optionalRoute.get();
            int availableSeats = route.getMax_seats() - route.getOccupied_seats();

            if (occupiedSeats <= availableSeats) {
                route.setOccupied_seats(occupiedSeats+route.getOccupied_seats());
                routesRepository.save(route);
                // Оновити вільні та зайняті місця в відповіді
                Map<String, Integer> seatsInfo = new HashMap<>();
                seatsInfo.put("occupied_seats", route.getOccupied_seats());
                seatsInfo.put("max_seats", route.getMax_seats());

                return ResponseEntity.ok(seatsInfo);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
