package com.example.coursework3.service.implement;

import com.example.coursework3.model.Routes;
import com.example.coursework3.repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.coursework3.service.RoutesService;
import java.util.List;
import java.util.Optional;

@Service
public class RoutesServiceImpl implements RoutesService {

    @Autowired
    private RoutesRepository routesRepository;

    @Override
    public List<Routes> getAllRoutes() {
        return routesRepository.findAll();
    }

    @Override
    public Routes getRouteById(int routeId) {
        Optional<Routes> optionalRoutes = Optional.ofNullable(routesRepository.findById(routeId));

        if (optionalRoutes.isPresent()) {
            return optionalRoutes.get();
        } else {
            return null;
        }
    }

    @Override
    public void updateOccupiedSeats(int routeId, int occupiedSeats) {
        Optional<Routes> optionalRoute = Optional.ofNullable(routesRepository.findById(routeId));
        optionalRoute.ifPresent(route -> {
            route.setOccupied_seats(occupiedSeats);
            routesRepository.save(route);
        });
    }

}
