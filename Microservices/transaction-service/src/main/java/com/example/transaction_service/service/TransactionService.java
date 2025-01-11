package com.example.transaction_service.service;


import com.example.transaction_service.controller.TransactionRequest;
import com.example.transaction_service.entity.Compte;
import com.example.transaction_service.entity.Employee;
import com.example.transaction_service.reposetory.CompteRepository;
import com.example.transaction_service.reposetory.EmployeeRepository;
import com.example.transaction_service.reposetory.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;



import com.example.transaction_service.entity.Transaction;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class TransactionService {

    Logger log = LoggerFactory.getLogger(TransactionService.class);


    @Autowired
    private EmployeeRepository employeeRepository;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final WebClient webClient;

    public TransactionService(KafkaTemplate<String, String> kafkaTemplate, WebClient.Builder webClientBuilder, CompteRepository compteRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
        this.compteRepository = compteRepository;
    }


    @Autowired
    private CompteRepository compteRepository;



    public ResponseEntity<Transaction> makeTransaction(long compteId, Transaction.TypeTransaction typeTransaction,
                                                       double amount, Long targetCompteId, Long employeeId,
                                                       @RequestHeader("loggedInUser") String loggedInUser,
                                                       @RequestHeader("Authorization") String authorizationHeader) {

        // Extract the token from the Authorization header
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            token = null;
        }

        // Validate the token
        boolean isTokenValid = validateToken(token); // Implement this in your service
        if (!isTokenValid) {
            System.out.println("Invalid token for user: " + loggedInUser);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Unauthorized access
        }

        System.out.println("Token validated successfully for user: " + loggedInUser);

        log.info("Searching for account with ID: {}", compteId);
        // Retrieve the source account (Compte) from the repository
        Optional<Compte> optionalCompte = compteRepository.findById(Long.valueOf(compteId));
        if (optionalCompte.isEmpty()) {
            log.info("Source account not found in the local database");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Compte compte = optionalCompte.get();
        log.info("Source account found in the local database");

        // Retrieve the employee from the local database
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee with ID " + employeeId + " not found"));

        log.info("Employee " + employee.getIdEmployee() + " found");

        // Create a new transaction
        Transaction transaction = new Transaction();
        transaction.setComptes(Set.of(compte)); // Associate the source account
        transaction.setMontant(amount);
        transaction.setDateTransaction(LocalDateTime.now());
        transaction.setTypeTransaction(typeTransaction);
        transaction.setEmployee(employee); // Associate the employee

        // Handle the transaction types
        if (typeTransaction == Transaction.TypeTransaction.DEBIT) {
            // Validate sufficient balance for debit
            if (compte.getSolde() < amount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Insufficient balance
            }
            // Deduct amount from account
            compte.setSolde(compte.getSolde() - amount);
            log.info("Amount {} debited from account ID {}", amount, compteId);

        } else if (typeTransaction == Transaction.TypeTransaction.CREDIT) {
            // Add amount to account
            compte.setSolde(compte.getSolde() + amount);
            log.info("Amount {} credited to account ID {}", amount, compteId);

        } else if (typeTransaction == Transaction.TypeTransaction.TRANSFERT) {
            if (targetCompteId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Missing target account
            }

            // Retrieve the target account
            Optional<Compte> optionalTargetCompte = compteRepository.findById(targetCompteId);
            if (optionalTargetCompte.isEmpty()) {
                log.info("Target account not found in the local database");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Compte targetCompte = optionalTargetCompte.get();

            // Validate sufficient balance for transfer
            if (compte.getSolde() < amount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Insufficient balance
            }

            // Deduct amount from source account and add to target account
            compte.setSolde(compte.getSolde() - amount);
            targetCompte.setSolde(targetCompte.getSolde() + amount);

            log.info("Amount {} transferred from account ID {} to account ID {}", amount, compteId, targetCompteId);

            // Save updated target account
            compteRepository.save(targetCompte);

            // Create and save transaction for the target account
            Transaction targetTransaction = new Transaction();
            targetTransaction.setComptes(Set.of(targetCompte)); // Associate the target account
            targetTransaction.setMontant(amount);
            targetTransaction.setDateTransaction(LocalDateTime.now());
            targetTransaction.setTypeTransaction(Transaction.TypeTransaction.CREDIT); // Target account receives a credit
            targetTransaction.setEmployee(employee);

            transactionRepository.save(targetTransaction); // Save the target transaction
        } else {
            log.info("Invalid transaction type");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // Invalid transaction type
        }

        // Save updated source account
        compteRepository.save(compte);

        // Save the source transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }



    // checks id the token is valid or not
    public boolean validateToken(String token) {
        if(token == null || token.isEmpty()) {
            return false;
        }else {
            webClient.get()
                    .uri("/auth/validate")
                    .header("Authorization", "Bearer " + token)
                    .exchange()
                    .block();
            return true;
        }

    }



    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByCompteId(Long idCompte) {
        return transactionRepository.findByComptes_IdCompte(idCompte);
    }
    public void sendCompteforupdate1(Compte compte) {
        Message<Compte> message = MessageBuilder
                .withPayload(compte)
                .setHeader(KafkaHeaders.TOPIC, "tranup")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendCompteforupdate2(Compte targetcompte) {
        Message<Compte> message = MessageBuilder
                .withPayload(targetcompte)
                .setHeader(KafkaHeaders.TOPIC, "tranup2")
                .build();
        kafkaTemplate.send(message);
    }
}



