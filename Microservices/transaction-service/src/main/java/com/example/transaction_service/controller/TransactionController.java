package com.example.transaction_service.controller;

import com.example.transaction_service.entity.Compte;
import com.example.transaction_service.entity.Employee;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.reposetory.EmployeeRepository;
import com.example.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    private final WebClient webClient;

    @Autowired
    EmployeeRepository employeeRepository;

    public TransactionController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }



    @PostMapping("/make")
    public ResponseEntity<Transaction> makeTransaction(
            @RequestBody TransactionRequest request,
            @RequestHeader("loggedInUser") String loggedInUser,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Récupérer l'employé authentifié à partir de l'email
        Employee employee = employeeRepository.findEmployeeByEmail(loggedInUser);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found for the provided email.");
        }

        // Appeler le service avec l'ID de l'employé authentifié
        return transactionService.makeTransaction(
                request.getCompteId(),
                request.getTypeTransaction(),
                request.getAmount(),
                request.getTargetCompteId(),
                employee.getIdEmployee(), // Utilisation de l'ID de l'employé
                loggedInUser,
                authorizationHeader);
    }

    @GetMapping("/testo")
    public List<Compte> getTesto(@RequestHeader("loggedInUser") String loggedInUser,
                                 @RequestHeader("Authorization") String authorizationHeader) {
        return transactionService.getXihaja(loggedInUser, authorizationHeader);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/compte/{idCompte}")
    public List<Transaction> getTransactionsByCompteId(@PathVariable Long idCompte) {
        return transactionService.getTransactionsByCompteId(idCompte);
    }

}
