package org.example.credit_microservice.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "loan_prediction_responses")
public class LoanPredictionResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private LoanPredictionRequest loanPredictionRequest;

    private int loan_status;

    // Constructor
    public LoanPredictionResponse() {}

    public LoanPredictionResponse(LoanPredictionRequest loanPredictionRequest, int loan_status) {
        this.loanPredictionRequest = loanPredictionRequest;
        this.loan_status = loan_status;
    }

    public LoanPredictionResponse(int predictedLoanStatus) {
        this.loan_status = predictedLoanStatus;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LoanPredictionRequest getLoanPredictionRequest() {
        return loanPredictionRequest;
    }

    public void setLoanPredictionRequest(LoanPredictionRequest loanPredictionRequest) {
        this.loanPredictionRequest = loanPredictionRequest;
    }

    public int getLoan_status() {
        return loan_status;
    }

    public void setLoan_status(int loan_status) {
        this.loan_status = loan_status;
    }
}
