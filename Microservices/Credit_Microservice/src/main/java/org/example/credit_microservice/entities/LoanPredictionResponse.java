package org.example.credit_microservice.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;


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

    private String loan_status_message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public LoanPredictionResponse() {
        this.createdAt = LocalDateTime.now();
    }

    public LoanPredictionResponse(LoanPredictionRequest loanPredictionRequest, int loan_status) {
        this.loanPredictionRequest = loanPredictionRequest;
        this.loan_status = loan_status;
        this.loan_status_message = getLoanStatusMessage(loan_status);
        this.createdAt = LocalDateTime.now();
    }

    public LoanPredictionResponse(int predictedLoanStatus) {
        this.loan_status = predictedLoanStatus;
        this.loan_status_message = getLoanStatusMessage(predictedLoanStatus);
        this.createdAt = LocalDateTime.now();
    }

    private String getLoanStatusMessage(int loan_status) {
        return loan_status == 1 ? "Customer Will Default on This Loan" : "Customer Will Not Default on This Loan";
    }

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
        this.loan_status_message = getLoanStatusMessage(loan_status);
    }

    public String getLoan_status_message() {
        return loan_status_message;
    }

    public void setLoan_status_message(String loan_status_message) {
        this.loan_status_message = loan_status_message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
