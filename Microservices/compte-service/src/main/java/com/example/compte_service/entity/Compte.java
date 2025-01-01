package com.example.compte_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    // Many-to-one relationship with Client
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    private Client client;

    // One-to-many relationship with CarteBancaire

    @JsonManagedReference
    @OneToMany(mappedBy = "compte")
    private Set<CarteBancaire> cartes;

    // Many-to-one relationship with Employee (each account is created by a single employee)
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false) // Foreign key for Employee
    @JsonIgnore
    private Employee employee;

    // Default constructor
    public Compte() {}

    // Parameterized constructor
    public Compte(String numeroCompte, TypeCompte typeCompte, double solde, LocalDate dateOuverture, double taux, double decouvert, Client client, Employee employee) {
        this.numeroCompte = numeroCompte;
        this.typeCompte = typeCompte;
        this.solde = solde;
        this.dateOuverture = dateOuverture;
        this.taux = taux;
        this.decouvert = decouvert;
        this.client = client;
        this.employee = employee;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<CarteBancaire> getCartes() {
        return cartes;
    }

    public void setCartes(Set<CarteBancaire> cartes) {
        this.cartes = cartes;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
