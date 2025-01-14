package com.example.compte_service.service;


import com.example.compte_service.entity.Compte;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.CommentEvent;

import java.util.List;


public interface CompteService {
    List<Compte> getAllComptes();
    Compte getCompteByNumero(String numeroCompte);
    List<Compte> getComptesByClientCne(String cne);
    public void sendcompteforadd(Compte compte);
    public void sendComptefodelet(Compte compte);
    public void sendCompteforupdate(Compte compte);
    public Compte createCompte(String numeroCompte, Compte.TypeCompte typeCompte, double solde, double taux,double decouvert,long clientId, long employeeId);




}
