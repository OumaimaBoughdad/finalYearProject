package org.example.credit_microservice.controller;


import org.example.credit_microservice.entities.CreditClient;
import org.example.credit_microservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
public class CreditClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create-client")
    public ResponseEntity<CreditClient> saveClient(@RequestBody CreditClient client) {
        CreditClient savedClient = clientService.saveClient(client);
        return ResponseEntity.ok(savedClient);
    }

    @GetMapping("/getclient/{cni}")
    public ResponseEntity<CreditClient> getClientByCni(@PathVariable Long cni) {
        CreditClient client = clientService.getClientByCni(cni);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<CreditClient>> getAllClients() {
        List<CreditClient> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @DeleteMapping("/deleteclient/{cni}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long cni) {
        clientService.deleteClient(cni);
        return ResponseEntity.noContent().build();
    }
}