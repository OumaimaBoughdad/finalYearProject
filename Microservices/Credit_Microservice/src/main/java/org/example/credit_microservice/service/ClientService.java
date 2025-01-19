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

    public CreditClient saveClient(CreditClient client) {
        return clientRepository.save(client);
    }

    public CreditClient getClientByCni(Long cni) {
        return clientRepository.findById(cni).orElse(null);
    }

    public List<CreditClient> getAllClients() {
        return clientRepository.findAll();
    }

    public void deleteClient(Long cni) {
        clientRepository.deleteById(cni);
    }
}
