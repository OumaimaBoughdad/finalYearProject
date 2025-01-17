package org.example.credit_microservice.controller;


import org.example.credit_microservice.entities.Credit;
import org.example.credit_microservice.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;

    // Create or Update Credit
    @PostMapping
    public ResponseEntity<Credit> saveCredit(@RequestBody Credit credit) {
        Credit savedCredit = creditService.saveCredit(credit);
        return ResponseEntity.ok(savedCredit);
    }

    // Get Credit by ID
    @GetMapping("/{id}")
    public ResponseEntity<Credit> getCreditById(@PathVariable int id) {
        Credit credit = creditService.getCreditById(id);
        if (credit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(credit);
    }

    // Get All Credits
    @GetMapping
    public ResponseEntity<List<Credit>> getAllCredits() {
        List<Credit> credits = creditService.getAllCredits();
        return ResponseEntity.ok(credits);
    }

    // Delete Credit by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredit(@PathVariable int id) {
        creditService.deleteCredit(id);
        return ResponseEntity.noContent().build();
    }
}
