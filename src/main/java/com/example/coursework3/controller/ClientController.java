package com.example.coursework3.controller;

import com.example.coursework3.DataTransfer.ClientDataTransfer;
import com.example.coursework3.model.Client;
import com.example.coursework3.repository.ClientRepository;
import com.example.coursework3.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;
    private final ClientRepository clientRepository;

    public ClientController(ClientService clientService, ClientRepository clientRepository) {
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody ClientDataTransfer clientData) {
        // Перевірка унікальності email
        if (clientRepository.existsByEmail(clientData.getEmail())) {
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Email already exists");
            responseMap.put("success", "false");
            return ResponseEntity.ok(responseMap);
        }

        // Якщо перевірки пройшли успішно, реєструємо користувача
        Client client = new Client();
        client.setFirstName(clientData.getFirstName());
        client.setLastName(clientData.getLastName());
        client.setEmail(clientData.getEmail());
        client.setPassword(clientData.getPassword());
        clientRepository.save(client);

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("message", "Registration successful");
        responseMap.put("success", "true");
        return ResponseEntity.ok(responseMap);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody ClientDataTransfer clientData) {
        boolean loginSuccessful = clientService.login(clientData.getEmail(), clientData.getPassword());

        Map<String, String> responseMap = new HashMap<>();
        if (loginSuccessful) {
            responseMap.put("success", "true");
            responseMap.put("message", "Login successful");
        } else {
            responseMap.put("success", "false");
            responseMap.put("message", "Login failed");
        }
        return ResponseEntity.ok(responseMap);
    }
    @GetMapping("/current")
    public ResponseEntity<ClientDataTransfer> getCurrentClientInfoByEmail(@RequestParam String email) {
        try {
            Client loggedInClient = clientService.getClientByEmail(email);

            if (loggedInClient != null) {
                ClientDataTransfer clientData = new ClientDataTransfer();
                clientData.setUserID(loggedInClient.getId());
                clientData.setFirstName(loggedInClient.getFirstName());
                clientData.setLastName(loggedInClient.getLastName());
                clientData.setEmail(loggedInClient.getEmail());
               return ResponseEntity.ok(clientData);

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
