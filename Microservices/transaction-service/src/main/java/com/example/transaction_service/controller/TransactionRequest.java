package com.example.transaction_service.controller;


import  com.example.transaction_service.entity.Transaction;



public class TransactionRequest {


    private long compteId;


    private Transaction.TypeTransaction typeTransaction;


    private double amount;


    private Long targetCompteId;


    private Long employeeId;



    public TransactionRequest(Transaction.TypeTransaction typeTransaction, long compteId, double amount, Long  targetCompteId,Long employeeId) {
        this.typeTransaction = typeTransaction;
        this.compteId=compteId;
        this.amount = amount;
        this.targetCompteId = targetCompteId;
        this.employeeId= employeeId;
    }

   public long getCompteId(){
        return compteId;
   }

    public Transaction.TypeTransaction getTypeTransaction() {
        return typeTransaction;
    }

    public double getAmount() {
        return amount;
    }

    public Long getTargetCompteId() {
        return targetCompteId;
    }

    public void setTypeTransaction(Transaction.TypeTransaction typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public void setCompteId(long compteId) {
        this.compteId=compteId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTargetCompteId(Long  targetCompteId) {
        this.targetCompteId = targetCompteId;
    }



    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
