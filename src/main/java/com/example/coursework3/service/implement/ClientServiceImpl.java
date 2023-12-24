package com.example.coursework3.service.implement;

import com.example.coursework3.model.Client;
import com.example.coursework3.repository.ClientRepository;
import com.example.coursework3.service.ClientService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ClientServiceImpl(ClientRepository clientRepository, BCryptPasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Client getLoggedInClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Client) {
            return (Client) authentication.getPrincipal();
        }

        return null;
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    @Override
    public Long getClientIdByEmail(String email) {
        Client client = clientRepository.findByEmail(email);
        return (client != null) ? client.getId() : null;
    }

    @Override
    public boolean login(String email, String password) {
        Optional<Client> clientOptional = Optional.ofNullable(clientRepository.findByEmail(email));
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            return passwordEncoder.matches(password, client.getPassword());
        }
        return false;
    }
}
