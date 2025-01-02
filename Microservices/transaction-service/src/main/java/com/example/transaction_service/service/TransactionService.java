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

    private final WebClient webClient;

    public TransactionService(WebClient.Builder webClientBuilder,CompteRepository compteRepository) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
        this.compteRepository = compteRepository;
    }


    @Autowired
    private CompteRepository compteRepository;

//
//    public String authenticateAndGetToken() {
//        String authUrl = "http://localhost:8080/auth/login";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> credentials = new HashMap<>();
//        credentials.put("username", "admin");
//        credentials.put("password", "password");
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(credentials, headers);
//
//        ResponseEntity<Map<String, String>> response = restTemplate.postForEntity(authUrl, request, Map.class);
//        return response.getBody().get("token"); // Assuming the token
//    }


    public ResponseEntity<Transaction> makeTransaction(long compteId, Transaction.TypeTransaction typeTransaction,
                                                       double amount, String targetCompteId, Long employeeId,
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
            Optional<Compte> optionalTargetCompte = compteRepository.findById(Long.parseLong(targetCompteId));
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







//    public ResponseEntity<Transaction> makeTransaction(String compteId, Transaction.TypeTransaction typeTransaction, double amount, String targetCompteId,Long employeeId) {
//        // Retrieve the account (Compte) from the Compte microservice
//        Compte compte = webClient.get()
//                .uri("/api/compte/" + compteId)
//                .retrieve()
//                .bodyToMono(Compte.class)
//                .block(); // Synchronous call
//
//        if (compte == null)
//        {
//            log.info("Account not found  2");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//
//        log.info("Account found  1");
//
//        // Create a new transaction
//        Transaction transaction = new Transaction();
//        transaction.setComptes(Set.of(compte)); // Add the source account
//        transaction.setMontant(amount);
//        transaction.setDateTransaction(LocalDateTime.now());
//        transaction.setTypeTransaction(typeTransaction);
//
//        Employee employee = webClient.get()
//                .uri("/api/employee/" + employeeId)
//                .retrieve()
//                .bodyToMono(Employee.class)
//                .block(); // Synchronous call
//
//
//        if(employee == null ){
//            log.info("Emplyee not foud  1");
//
//        }
//
//       transaction.setEmployee(employee);
//
//        // Handle the transaction type
//        if (typeTransaction == Transaction.TypeTransaction.DEBIT) {
//            // Validate sufficient balance for debit
//            if (compte.getSolde() < amount) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(null); // Insufficient balance
//            }
//            // Deduct amount from account
//            compte.setSolde(compte.getSolde() - amount);
//        } else if (typeTransaction == Transaction.TypeTransaction.CREDIT) {
//            // Add amount to account
//            compte.setSolde(compte.getSolde() + amount);
//        } else if (typeTransaction == Transaction.TypeTransaction.TRANSFERT) {
//            // Ensure target account is specified
//            if (targetCompteId == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(null); // Missing target account
//            }
//
//            // Retrieve target account
//            Compte targetCompte = webClient.get()
//                    .uri("/api/compte/" + targetCompteId)
//                    .retrieve()
//                    .bodyToMono(Compte.class)
//                    .block();
//
//            if (targetCompte == null) {
//                log.info("Account not found 3");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//
//            // Validate sufficient balance for transfer
//            if (compte.getSolde() < amount) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(null); // Insufficient balance
//            }
//
//            // Deduct amount from source account and add to target account
//            compte.setSolde(compte.getSolde() - amount);
//            targetCompte.setSolde(targetCompte.getSolde() + amount);
//
//            // Save updated target account
//            webClient.put()
//                    .uri("/api/compte/" + targetCompte.getIdCompte())
//                    .bodyValue(targetCompte)
//                    .retrieve()
//                    .bodyToMono(Compte.class)
//                    .block();
//        } else {
//             log.info("Account not found kkkk  ");
//            // Invalid transaction type
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(null);
//        }
//
//        // Save updated source account
//        webClient.put()
//                .uri("/api/compte/" + compte.getIdCompte())
//                .bodyValue(compte)
//                .retrieve()
//                .bodyToMono(Compte.class)
//                .block();
//
//        // Save transaction record in the database
//        Transaction savedTransaction = transactionRepository.save(transaction);
//
//        // Return the saved transaction
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
//    }



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

    public List<Compte> getXihaja(@RequestHeader("loggedInUser") String loggedInUser,
                                  @RequestHeader("Authorization") String authorizationHeader) {
        // Extract the token from the Authorization header
        String token;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            token = null;
        }

        // Validate the token
        boolean isTokenValid = validateToken(token); // Implement this in transactionService
        if (!isTokenValid) {
            System.out.println("Invalid token for user: " + loggedInUser);
            //return Flux.error(new RuntimeException("Unauthorized access: Token validation failed."));
        }

        System.out.println("Token validated successfully for user: " + loggedInUser);

        // If token is valid, proceed to fetch Compte objects using WebClient


        List<Compte> comptes = webClient.get()
                .uri("/api/compte") // Update the endpoint if required
                .headers(headers -> headers.setBearerAuth(token)) // Add the token as Bearer Auth
                .retrieve()
                .bodyToFlux(Compte.class) // Deserialize the response into a Flux<Compte>
                .onErrorResume(error -> {
                    // Handle potential errors from the API call
                    System.out.println("Error while fetching Compte objects: " + error.getMessage());
                    return Flux.error(new RuntimeException("Failed to fetch Compte objects from external API."));
                })
                .collectList() // Collect the Flux into a List<Compte>
                .block(); // Block the call to get the List<Compte> (This is a blocking operation)



        for ( Compte compte : comptes ) {
            saveCompte(compte);
        }

        return comptes;

    }


    public void saveCompte(Compte compte) {
        Long id = compte.getIdCompte();

        // Vérifier si le compte existe
        String sql1 = "SELECT COUNT(*) FROM comptes WHERE id_compte = ?";
        Integer count = jdbcTemplate.queryForObject(sql1, Integer.class, id);

        if (count == null || count == 0) {
            // Insérer un nouveau compte
            String sqlInsert = "INSERT INTO comptes (id_compte, numero_compte, type_compte, solde, date_ouverture, taux, decouvert) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sqlInsert, id, compte.getNumeroCompte(), compte.getTypeCompte().name(),
                    compte.getSolde(), compte.getDateOuverture(), compte.getTaux(), compte.getDecouvert());
            log.info("Compte inséré avec succès.");
        } else {
            // Vérifier si une mise à jour est nécessaire
            String sqlCheck = "SELECT numero_compte, type_compte, solde, date_ouverture, taux, decouvert FROM comptes WHERE id_compte = ?";
            Compte existingCompte = jdbcTemplate.queryForObject(sqlCheck, new BeanPropertyRowMapper<>(Compte.class), id);

            if (!compte.equals(existingCompte)) { // Implémentez une méthode equals() dans Compte
                // Mettre à jour le compte existant
                String sqlUpdate = "UPDATE comptes SET numero_compte = ?, type_compte = ?, solde = ?, date_ouverture = ?, taux = ?, decouvert = ? " +
                        "WHERE id_compte = ?";
                int rowsAffected = jdbcTemplate.update(sqlUpdate, compte.getNumeroCompte(), compte.getTypeCompte().name(),
                        compte.getSolde(), compte.getDateOuverture(), compte.getTaux(),
                        compte.getDecouvert(), id);
                log.info("Nombre de lignes mises à jour : {}", rowsAffected);
            } else {
                log.info("Aucune mise à jour nécessaire.");
            }
        }
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByCompteId(Long idCompte) {
        return transactionRepository.findByComptes_IdCompte(idCompte);
    }

}



