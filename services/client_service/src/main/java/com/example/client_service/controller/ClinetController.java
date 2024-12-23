package com.example.client_service.controller;


import com.example.client_service.dto.ClientDTO;
import com.example.client_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clients")
public class ClinetController {
    @Autowired
    private ClientService clientService;

    @PostMapping("/add")
    public ResponseEntity<?> addClient(@RequestBody ClientDTO clientDTO, @RequestHeader("Authorization") String authHeader) {
        try {
            clientService.addClient(clientDTO, authHeader);
            return ResponseEntity.status(HttpStatus.CREATED).body("Client added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
