package com.example.coursework3.service;

import com.example.coursework3.model.Routes;
import java.util.List;

public interface RoutesService {
    List<Routes> getAllRoutes();
    Routes getRouteById(int routeId);
    void updateOccupiedSeats(int routeId, int occupiedSeats);

}
