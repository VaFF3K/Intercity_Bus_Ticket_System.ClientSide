package com.example.coursework3.repository;

import com.example.coursework3.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String username);

    boolean existsByEmail(String email);

    boolean existsByEncryptedFirstName(String encryptedFirstName);
}
