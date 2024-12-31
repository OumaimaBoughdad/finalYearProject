package com.example.compte_service.service;


import com.example.compte_service.entity.Compte;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CompteService {
    Compte createCompte(String numeroCompte, Compte.TypeCompte typeCompte, double solde, long clientId, long employeeId);
    List<Compte> getAllComptes();
    Compte getCompteByNumero(String numeroCompte);
    List<Compte> getComptesByClientCne(String cne);


}
