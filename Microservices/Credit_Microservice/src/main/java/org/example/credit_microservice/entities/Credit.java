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

    private final double loanIntRate = 5.5;

    private double loanPercentIncome;

    @ManyToOne
    @JoinColumn(name = "cni")
    private CreditClient client;

    // Constructors
    public Credit() {
    }

    public Credit(String loanIntent, double loanAmnt, CreditClient client) {
        this.loanIntent = loanIntent;
        this.loanAmnt = loanAmnt;
        this.client = client;
        this.loanGrade = calculateLoanGrade(client.getPersonIncome());
        this.loanPercentIncome = calculateLoanPercentIncome();
    }

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
        this.loanPercentIncome = calculateLoanPercentIncome(); // Recalculate when loanAmnt changes
    }

    public double getLoanIntRate() {
        return loanIntRate;
    }

    public double getLoanPercentIncome() {
        return calculateLoanPercentIncome(); // Always calculate dynamically
    }

    public CreditClient getClient() {
        return client;
    }

    public void setClient(CreditClient client) {
        this.client = client;
        this.loanGrade = calculateLoanGrade(client.getPersonIncome());
        this.loanPercentIncome = calculateLoanPercentIncome(); // Recalculate when client changes
    }

    private double calculateLoanPercentIncome() {
        if (client == null || client.getPersonIncome() == 0) {
            return 0; // Avoid division by zero
        }
        return (loanAmnt / client.getPersonIncome()) * 100;
    }

    private String calculateLoanGrade(double income) {
        if (income > 5000) {
            return "A";
        } else if (income > 3000) {
            return "B";
        } else if (income > 2000) {
            return "C";
        } else {
            return "D";
        }
    }
}