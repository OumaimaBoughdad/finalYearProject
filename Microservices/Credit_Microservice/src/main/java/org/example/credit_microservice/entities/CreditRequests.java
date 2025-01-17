//package org.example.credit_microservice.entities;
//
//import jakarta.persistence.*;
//
//@Entity
//public class CreditRequests {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "client_id", nullable = false)
//    private Client client;
//
//    private String typeOfLoan;
//    private int interestRate;
//    private int delayFromDueDate;
//    private float numOfDelayedPayment;
//    private float outstandingDebt;
//    private String paymentOfMinAmount;
//    private float monthlyBalance;
//    private int predictedCreditScore; // 0: Poor, 1: Standard, 2: Good
//
//    // Getters and Setters
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Client getClient() {
//        return client;
//    }
//
//    public void setClient(Client client) {
//        this.client = client;
//    }
//
//    public String getTypeOfLoan() {
//        return typeOfLoan;
//    }
//
//    public void setTypeOfLoan(String typeOfLoan) {
//        this.typeOfLoan = typeOfLoan;
//    }
//
//    public int getInterestRate() {
//        return interestRate;
//    }
//
//    public void setInterestRate(int interestRate) {
//        this.interestRate = interestRate;
//    }
//
//    public int getDelayFromDueDate() {
//        return delayFromDueDate;
//    }
//
//    public void setDelayFromDueDate(int delayFromDueDate) {
//        this.delayFromDueDate = delayFromDueDate;
//    }
//
//    public float getNumOfDelayedPayment() {
//        return numOfDelayedPayment;
//    }
//
//    public void setNumOfDelayedPayment(float numOfDelayedPayment) {
//        this.numOfDelayedPayment = numOfDelayedPayment;
//    }
//
//    public float getOutstandingDebt() {
//        return outstandingDebt;
//    }
//
//    public void setOutstandingDebt(float outstandingDebt) {
//        this.outstandingDebt = outstandingDebt;
//    }
//
//    public String getPaymentOfMinAmount() {
//        return paymentOfMinAmount;
//    }
//
//    public void setPaymentOfMinAmount(String paymentOfMinAmount) {
//        this.paymentOfMinAmount = paymentOfMinAmount;
//    }
//
//    public float getMonthlyBalance() {
//        return monthlyBalance;
//    }
//
//    public void setMonthlyBalance(float monthlyBalance) {
//        this.monthlyBalance = monthlyBalance;
//    }
//
//    public int getPredictedCreditScore() {
//        return predictedCreditScore;
//    }
//
//    public void setPredictedCreditScore(int predictedCreditScore) {
//        this.predictedCreditScore = predictedCreditScore;
//    }
//}
