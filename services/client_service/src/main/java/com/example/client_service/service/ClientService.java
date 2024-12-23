package com.example.client_service.service;

import com.example.client_service.dto.ClientDTO;

public interface ClientService {
    void addClient(ClientDTO clientDTO, String authHeader) throws Exception;
}
