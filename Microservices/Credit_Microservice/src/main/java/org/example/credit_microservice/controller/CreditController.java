package org.example.credit_microservice.controller;

import org.example.credit_microservice.entities.Credit;
import org.example.credit_microservice.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;

    // Create a new credit
    @PostMapping
    public ResponseEntity<Credit> createCredit(@RequestBody Credit credit) {
        Credit savedCredit = creditService.createCredit(credit);
        return new ResponseEntity<>(savedCredit, HttpStatus.CREATED);
    }

    // Get all credits
    @GetMapping
    public ResponseEntity<List<Credit>> getAllCredits() {
        List<Credit> credits = creditService.getAllCredits();
        return new ResponseEntity<>(credits, HttpStatus.OK);
    }

    // Get a credit by ID
    @GetMapping("/{creditId}")
    public ResponseEntity<Credit> getCreditById(@PathVariable Long creditId) {
        Optional<Credit> credit = creditService.getCreditById(creditId);
        return credit.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update a credit
    @PutMapping("/{creditId}")
    public ResponseEntity<Credit> updateCredit(@PathVariable Long creditId, @RequestBody Credit updatedCredit) {
        Credit credit = creditService.updateCredit(creditId, updatedCredit);
        return new ResponseEntity<>(credit, HttpStatus.OK);
    }

    // Delete a credit
    @DeleteMapping("/{creditId}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long creditId) {
        creditService.deleteCredit(creditId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}