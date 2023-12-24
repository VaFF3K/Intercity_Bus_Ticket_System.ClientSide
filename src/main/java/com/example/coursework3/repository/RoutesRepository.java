package com.example.coursework3.repository;

import com.example.coursework3.model.Routes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutesRepository extends JpaRepository<Routes, Integer> {


    // метод для отримання маршруту за його ID
    Routes findById(int routeId);

}
