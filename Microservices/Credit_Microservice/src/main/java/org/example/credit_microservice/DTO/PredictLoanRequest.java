package org.example.credit_microservice.DTO;

public class PredictLoanRequest {
    private Long cne;
    private String loanIntent;
    private double loanAmnt;

    // Getters and setters
    public Long getCne() {
        return cne;
    }

    public void setCne(Long cne) {
        this.cne = cne;
    }

    public String getLoanIntent() {
        return loanIntent;
    }

    public void setLoanIntent(String loanIntent) {
        this.loanIntent = loanIntent;
    }

    public double getLoanAmnt() {
        return loanAmnt;
    }

    public void setLoanAmnt(double loanAmnt) {
        this.loanAmnt = loanAmnt;
    }
}
