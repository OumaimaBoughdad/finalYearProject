package com.example.transaction_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaction", nullable = false, updatable = false)
    private long idTransaction;

    @ManyToOne
    @JoinColumn(name = "id_employee", nullable = false)
    private Employee employee;

    @ManyToMany
    @JoinTable(
            name = "transaction_comptes",
            joinColumns = @JoinColumn(name = "id_transaction"),
            inverseJoinColumns = @JoinColumn(name = "id_compte")
    )
    private Set<Compte> comptes = new HashSet<>();

    @Column(name = "montant", nullable = false)
    private double montant;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime dateTransaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_transaction", nullable = false)
    private TypeTransaction typeTransaction;

    public enum TypeTransaction {
        DEBIT,
        CREDIT,
        TRANSFERT
    }

    // Default constructor
    public Transaction() {}

    // Parameterized constructor
    public Transaction(Employee employee, Set<Compte> comptes, double montant, LocalDateTime dateTransaction, TypeTransaction typeTransaction) {
        this.employee = employee;
        this.comptes = comptes;
        this.montant = montant;
        this.dateTransaction = dateTransaction;
        this.typeTransaction = typeTransaction;
    }

    // Getters and Setters
    public long getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(long idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Set<Compte> getComptes() {
        return comptes;
    }

    public void setComptes(Set<Compte> comptes) {
        this.comptes = comptes;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateTransaction() {
        return dateTransaction;
    }

    public void setDateTransaction(LocalDateTime dateTransaction) {
        this.dateTransaction = dateTransaction;
    }

    public TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }
}
