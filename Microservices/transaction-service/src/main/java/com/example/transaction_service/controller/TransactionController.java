package com.example.transaction_service.controller;

import com.example.transaction_service.entity.Compte;
import com.example.transaction_service.entity.Transaction;
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

    public TransactionController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }


    @PostMapping("/make")
    public ResponseEntity<Transaction> makeTransaction(@RequestParam String compteId,
                                                       @RequestParam Transaction.TypeTransaction typeTransaction,
                                                       @RequestParam double amount,
                                                       @RequestParam(required = false) String targetCompteId,
                                                       @RequestParam Long employeeId,
                                                       @RequestHeader("loggedInUser") String loggedInUser,
                                                       @RequestHeader("Authorization") String authorizationHeader) {
        return transactionService.makeTransaction(compteId, typeTransaction, amount, targetCompteId, employeeId, loggedInUser, authorizationHeader);
    }




    @GetMapping("/testo")
    public List<Compte> getTesto(@RequestHeader("loggedInUser") String loggedInUser,
                                 @RequestHeader("Authorization") String authorizationHeader) {
        return transactionService.getXihaja(loggedInUser, authorizationHeader);
    }



}
