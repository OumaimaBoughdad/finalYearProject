package com.example.compte_service.controller;

import com.example.compte_service.entity.Compte.TypeCompte;

public class CompteRequest {

    private String numeroCompte;
    private TypeCompte typeCompte;
    private double solde;

    // Getters and setters

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public TypeCompte getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }


}
