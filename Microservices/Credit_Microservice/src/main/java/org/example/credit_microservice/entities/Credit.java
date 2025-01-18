package org.example.credit_microservice.entities;

import jakarta.persistence.*;
@Entity
@Table(name = "credits")
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creditId;

    private String loanIntent;
    private String loanGrade;
    private double loanAmnt;
    private double loanIntRate;
    private double loanPercentIncome;

    @ManyToOne
    @JoinColumn(name = "client_id") // Foreign key to the client table
    private CreditClient client;
    // Constructors
    public Credit() {
    }

    public Credit(String loanIntent, String loanGrade, double loanAmnt, double loanIntRate, double loanPercentIncome, CreditClient client) {
        this.loanIntent = loanIntent;
        this.loanGrade = loanGrade;
        this.loanAmnt = loanAmnt;
        this.loanIntRate = loanIntRate;
        this.loanPercentIncome = loanPercentIncome;
        this.client = client;
    }

    // Getters and setters
    public Long getCreditId() {
        return creditId;
    }

    public void setCreditId(Long creditId) {
        this.creditId = creditId;
    }

    public String getLoanIntent() {
        return loanIntent;
    }

    public void setLoanIntent(String loanIntent) {
        this.loanIntent = loanIntent;
    }

    public String getLoanGrade() {
        return loanGrade;
    }

    public void setLoanGrade(String loanGrade) {
        this.loanGrade = loanGrade;
    }

    public double getLoanAmnt() {
        return loanAmnt;
    }

    public void setLoanAmnt(double loanAmnt) {
        this.loanAmnt = loanAmnt;
    }

    public double getLoanIntRate() {
        return loanIntRate;
    }

    public void setLoanIntRate(double loanIntRate) {
        this.loanIntRate = loanIntRate;
    }

    public double getLoanPercentIncome() {
        return loanPercentIncome;
    }

    public void setLoanPercentIncome(double loanPercentIncome) {
        this.loanPercentIncome = loanPercentIncome;
    }

    public CreditClient getClient() {
        return client;
    }

    public void setClient(CreditClient client) {
        this.client = client;
    }
}
