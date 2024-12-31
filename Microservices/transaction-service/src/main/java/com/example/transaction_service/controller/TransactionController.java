package com.example.transaction_service.controller;

import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Transaction> makeTransaction( @RequestBody TransactionRequest transactionRequest) {

        // Call the service to handle the transaction
        ResponseEntity<Transaction> response = transactionService.makeTransaction(
                transactionRequest.getCompteId(),
                transactionRequest.getTypeTransaction(),
                transactionRequest.getAmount(),
                transactionRequest.getTargetCompteId(),
                transactionRequest.getEmployeeId()
        );
        return response;
    }

}
