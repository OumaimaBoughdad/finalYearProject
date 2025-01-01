package com.example.compte_service.entity;



import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "cartes_bancaires")
public class CarteBancaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carte", nullable = false, updatable = false)
    private long idCarte;

    @Column(name = "numero_carte", nullable = false, unique = true)
    private String numeroCarte;

    @Column(name = "date_expiration", nullable = false)
    private String dateExpiration;

    @Column(name = "code_pin", nullable = false)
    private String codePin;

    @Column(name = "limite_carte", nullable = false)
    private double limiteCarte;

    // Many-to-one relationship with Compte
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "compte_id", nullable = false)
    private Compte compte;

    // Default constructor
    public CarteBancaire() {}

    // Parameterized constructor
    public CarteBancaire(String numeroCarte, String dateExpiration, String codePin, double limiteCarte, Compte compte) {
        this.numeroCarte = numeroCarte;
        this.dateExpiration = dateExpiration;
        this.codePin = codePin;
        this.limiteCarte = limiteCarte;
        this.compte = compte;
    }

    // Getters and Setters
    public long getIdCarte() {
        return idCarte;
    }

    public void setIdCarte(long idCarte) {
        this.idCarte = idCarte;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(String dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getCodePin() {
        return codePin;
    }

    public void setCodePin(String codePin) {
        this.codePin = codePin;
    }

    public double getLimiteCarte() {
        return limiteCarte;
    }

    public void setLimiteCarte(double limiteCarte) {
        this.limiteCarte = limiteCarte;
    }

    public Compte getCompte() {
        return compte;
    }

    public void setCompte(Compte compte) {
        this.compte = compte;
    }
}
