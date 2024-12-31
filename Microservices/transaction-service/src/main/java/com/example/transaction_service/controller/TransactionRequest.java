package com.example.transaction_service.controller;


import  com.example.transaction_service.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;


public class TransactionRequest {

    @JsonProperty("compteId")
    private String compteId;

    @JsonProperty("typeTransaction")
    private Transaction.TypeTransaction typeTransaction;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("targetCompteId")
    private String targetCompteId;

    @JsonProperty("employeeId")
    private Long employeeId;



    public TransactionRequest(Transaction.TypeTransaction typeTransaction, String compteId, double amount, String  targetCompteId,Long employeeId) {
        this.typeTransaction = typeTransaction;
        this.compteId=compteId;
        this.amount = amount;
        this.targetCompteId = targetCompteId;
        this.employeeId= employeeId;
    }

   public String getCompteId(){
        return compteId;
   }

    public Transaction.TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public double getAmount() {
        return amount;
    }

    public String  getTargetCompteId() {
        return targetCompteId;
    }

    public void setTypeTransaction(Transaction.TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public void setCompteId(String compteId) {
        this.compteId=compteId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTargetCompteId(String  targetCompteId) {
        this.targetCompteId = targetCompteId;
    }



    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
