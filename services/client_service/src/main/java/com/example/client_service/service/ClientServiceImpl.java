package com.example.client_service.service;


import com.example.client_service.dto.ClientDTO;
import com.example.client_service.entity.Client;
import com.example.client_service.entity.Employee;
import com.example.client_service.reposetory.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository; // Ensure you have this to retrieve employee

    @Override
    public void addClient(ClientDTO clientDTO, String authHeader) throws Exception {
        // Decode the Authorization Header (Basic Auth assumed here)
        String credentials = new String(Base64.getDecoder().decode(authHeader.split(" ")[1]));
        String[] parts = credentials.split(":");
        String email = parts[0];
        String password = parts[1];

        // Authenticate the employee
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Invalid employee credentials"));

        if (!employee.getPassword().equals(password)) {
            throw new Exception("Invalid employee credentials");
        }

        // Create and save the client
        Client client = new Client();
        client.setName(clientDTO.getName());
        client.setEmail(clientDTO.getEmail());
        client.setEmployeeId(employee.getId()); // Associate employee

        clientRepository.save(client);
    }

}
