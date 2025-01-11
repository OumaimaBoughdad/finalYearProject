package com.example.transaction_service.controller;

import com.example.transaction_service.entity.Compte;
import com.example.transaction_service.entity.Employee;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.reposetory.CompteRepository;
import com.example.transaction_service.reposetory.EmployeeRepository;
import com.example.transaction_service.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    private Long targetCompteId;

    Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final WebClient webClient;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    CompteRepository compteRepository;




    public TransactionController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }



    @PostMapping("/make")
    public ResponseEntity<String> makeTransaction(
            @RequestBody TransactionRequest request,
            @RequestHeader("loggedInUser") String loggedInUser,
            @RequestHeader("Authorization") String authorizationHeader) {

        // Récupérer l'employé authentifié à partir de l'email
        Employee employee = employeeRepository.findEmployeeByEmail(loggedInUser);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found for the provided email.");
        }

        // Appeler la fonction makeTransaction et obtenir la transaction
        transactionService.makeTransaction(
                request.getCompteId(),
                request.getTypeTransaction(),
                request.getAmount(),
                request.getTargetCompteId(),
                employee.getIdEmployee(), // Utilisation de l'ID de l'employé
                loggedInUser,
                authorizationHeader);





        // Récupérer le compte source (compte)
        Optional<Compte> optionalCompte = compteRepository.findById(request.getCompteId());
        if (optionalCompte.isEmpty()) {
            throw new IllegalArgumentException("Compte not found for the provided ID.");
        }
        Compte compte = optionalCompte.get();
        transactionService.sendCompteforupdate1(compte);
        log.info("compte send for update " + compte.getIdCompte());

        // Initialiser le compte cible (targetCompte)
        Compte targetCompte = null;

        // Si le type de transaction est un transfert, vérifier le compte cible
        if (request.getTypeTransaction() == Transaction.TypeTransaction.TRANSFERT) {
            if (request.getTargetCompteId() == null) {
                throw new IllegalArgumentException("Target compte ID is required for a transfer transaction.");
            }

            // Récupérer le compte cible (targetCompte)
            Optional<Compte> optionalTargetCompte = compteRepository.findById( request.getTargetCompteId());
            if (optionalTargetCompte.isEmpty()) {
                throw new IllegalArgumentException("Target compte not found for the provided ID.");
            }
            targetCompte = optionalTargetCompte.get();
            transactionService.sendCompteforupdate2(targetCompte);
            log.info("Target compte send for update " + targetCompte.getIdCompte());
        }



        // Construire un message de succès
        String responseMessage = String.format(
                "Transaction effectuée avec succès : ID %d, type %s, montant %.2f",
                request.getCompteId(), request.getTypeTransaction(), request.getAmount());

        // Retourner le message avec un statut HTTP 200 (OK)
        return ResponseEntity.ok(responseMessage);
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
