package com.example.coursework3.model;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Base64;
import java.util.List;


@Entity
@Table(name = "clients")

public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Transient
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private String email;
    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Order> orders;


    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(columnDefinition = "text")
    private String encryptedFirstName;

    public String getFirstName() {
        return decryptFirstName();
    }

    public void setFirstName(String firstName) {
        // Encrypt the full name before storing it
        this.encryptedFirstName = encryptFirstName(firstName);
    }

    private String encryptFirstName(String firstName) {
        return Base64.getEncoder().encodeToString(firstName.getBytes());
    }

    private String decryptFirstName() {
        return new String(Base64.getDecoder().decode(encryptedFirstName));
    }

    @Column(columnDefinition = "text")
    private String encryptedLastName;

    public String getLastName() {
        return decryptLastName();
    }

    public void setLastName(String lastName) {
        this.encryptedLastName = encryptLastName(lastName);
    }

    private String encryptLastName(String lastName) {
        return Base64.getEncoder().encodeToString(lastName.getBytes());
    }

    private String decryptLastName() {
        return new String(Base64.getDecoder().decode(encryptedLastName));
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getEncryptedLastName() {
        return encryptedLastName;
    }

    public void setEncryptedLastName(String encryptedLastName) {
        this.encryptedLastName = encryptedLastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

}