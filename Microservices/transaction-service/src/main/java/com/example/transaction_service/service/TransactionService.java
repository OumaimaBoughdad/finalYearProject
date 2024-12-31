package com.example.transaction_service.service;


import com.example.transaction_service.entity.Compte;
import com.example.transaction_service.entity.Employee;
import com.example.transaction_service.reposetory.EmployeeRepository;
import com.example.transaction_service.reposetory.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import com.example.transaction_service.entity.Transaction;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Set;



@Service
public class TransactionService {

    Logger log = LoggerFactory.getLogger(TransactionService.class);


    @Autowired
    private EmployeeRepository employeeRepository;



    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WebClient webclient;


    public ResponseEntity<Transaction> makeTransaction(String compteId, Transaction.TypeTransaction typeTransaction, double amount, String targetCompteId,Long employeeId) {
        // Retrieve the account (Compte) from the Compte microservice
        Compte compte = webclient.get()
                .uri("/api/compte/" + compteId)
                .retrieve()
                .bodyToMono(Compte.class)
                .block(); // Synchronous call

        if (compte == null)
        {
            log.info("Account not found  2");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setComptes(Set.of(compte)); // Add the source account
        transaction.setMontant(amount);
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setTypeTransaction(typeTransaction);

        Employee employee = webclient.get()
                .uri("/api/employee/" + employeeId)
                .retrieve()
                .bodyToMono(Employee.class)
                .block(); // Synchronous call


        if(employee == null ){
            log.info("Emplyee not foud  1");

        }

       transaction.setEmployee(employee);

        // Handle the transaction type
        if (typeTransaction == Transaction.TypeTransaction.DEBIT) {
            // Validate sufficient balance for debit
            if (compte.getSolde() < amount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); // Insufficient balance
            }
            // Deduct amount from account
            compte.setSolde(compte.getSolde() - amount);
        } else if (typeTransaction == Transaction.TypeTransaction.CREDIT) {
            // Add amount to account
            compte.setSolde(compte.getSolde() + amount);
        } else if (typeTransaction == Transaction.TypeTransaction.TRANSFERT) {
            // Ensure target account is specified
            if (targetCompteId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); // Missing target account
            }

            // Retrieve target account
            Compte targetCompte = webclient.get()
                    .uri("/api/compte/" + targetCompteId)
                    .retrieve()
                    .bodyToMono(Compte.class)
                    .block();

            if (targetCompte == null) {
                log.info("Account not found 3");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Validate sufficient balance for transfer
            if (compte.getSolde() < amount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); // Insufficient balance
            }

            // Deduct amount from source account and add to target account
            compte.setSolde(compte.getSolde() - amount);
            targetCompte.setSolde(targetCompte.getSolde() + amount);

            // Save updated target account
            webclient.put()
                    .uri("/api/compte/" + targetCompte.getIdCompte())
                    .bodyValue(targetCompte)
                    .retrieve()
                    .bodyToMono(Compte.class)
                    .block();
        } else {
             log.info("Account not found kkkk  ");
            // Invalid transaction type
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        // Save updated source account
        webclient.put()
                .uri("/api/compte/" + compte.getIdCompte())
                .bodyValue(compte)
                .retrieve()
                .bodyToMono(Compte.class)
                .block();

        // Save transaction record in the database
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Return the saved transaction
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }



}

   /* @Override

   public enum TypeTransaction {
        DEBIT,
        CREDIT,
        TRANSFERT
    }
    public ResponseEntity<Employee> getEmployeeById(Long id) {
        Employee employee=  webclient.get()
                .uri("/api/employees/" + id)
                .retrieve()
                .bodyToMono(Employee.class)
                .block();//async
        return ResponseEntity.ok(employee);
    }*/


