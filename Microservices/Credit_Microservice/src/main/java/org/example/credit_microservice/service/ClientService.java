package org.example.credit_microservice.service;


import org.example.credit_microservice.entities.CreditClient;
import org.example.credit_microservice.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Create or Update Client
    public CreditClient saveClient(CreditClient client) {
        return clientRepository.save(client);
    }

    // Read Client by CNI
    public CreditClient getClientByCni(Long cni) {
        return clientRepository.findById(cni).orElse(null);
    }

    // Get All Clients
    public List<CreditClient> getAllClients() {
        return clientRepository.findAll();
    }

    // Delete Client by CNI
    public void deleteClient(Long cni) {
        clientRepository.deleteById(cni);
    }
}
