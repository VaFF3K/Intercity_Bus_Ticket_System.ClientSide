package com.example.coursework3.service;

import com.example.coursework3.model.Client;

public interface ClientService {

    Client getLoggedInClient();

    Client getClientByEmail(String email);

    Long getClientIdByEmail(String email);

    // метод для отримання користувача за email та паролем
    boolean login(String email, String password);


}
