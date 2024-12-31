package com.example.transaction_service.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "comptes")
public class Compte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compte", nullable = false, updatable = false)
    private long idCompte;

    @Column(name = "numero_compte", nullable = false, unique = true)
    private String numeroCompte;

    public enum TypeCompte {
        COURANT,
        EPARGNE
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type_compte", nullable = false)
    private TypeCompte typeCompte;

    @Column(nullable = false)
    private double solde;

    @Column(name = "date_ouverture", nullable = false)
    private LocalDate dateOuverture;

    @Column(nullable = true)
    private double taux; // Seulement applicable pour le compte épargne

    @Column(nullable = true)
    private double decouvert; // Seulement applicable pour le compte courant







    // Default constructor
    public Compte() {}

    // Parameterized constructor
    public Compte(String numeroCompte, TypeCompte typeCompte, double solde, LocalDate dateOuverture, double taux, double decouvert) {
        this.numeroCompte = numeroCompte;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.dateOuverture = dateOuverture;
        this.taux = taux;
        this.decouvert = decouvert;

    }

    // Getters and Setters
    public long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(long idCompte) {
        this.idCompte = idCompte;
    }

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

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public double getTaux() {
        return taux;
    }

    public void setTaux(double taux) {
        this.taux = taux;
    }

    public double getDecouvert() {
        return decouvert;
    }

    public void setDecouvert(double decouvert) {
        this.decouvert = decouvert;
    }



}

