
package org.example.credit_microservice.controller;

import org.example.credit_microservice.entities.Credit;
import org.example.credit_microservice.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;
    @CrossOrigin(origins = "http://localhost:4200")

    @PostMapping("/api/create")
    public ResponseEntity<Credit> createCredit(@RequestParam Long cni, @RequestParam double loanAmnt, @RequestParam String loanIntent) {
        Credit savedCredit = creditService.createCredit(cni, loanAmnt, loanIntent);
        return new ResponseEntity<>(savedCredit, HttpStatus.CREATED);
    }
    @CrossOrigin(origins = "http://localhost:4200")

    @GetMapping
    public ResponseEntity<List<Credit>> getAllCredits() {
        return new ResponseEntity<>(creditService.getAllCredits(), HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200")

    @GetMapping("/{creditId}")
    public ResponseEntity<Credit> getCreditById(@PathVariable Long creditId) {
        return creditService.getCreditById(creditId)
                .map(credit -> new ResponseEntity<>(credit, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:4200")

    @PutMapping("/{creditId}")
    public ResponseEntity<Credit> updateCredit(@PathVariable Long creditId, @RequestBody Credit updatedCredit) {
        return new ResponseEntity<>(creditService.updateCredit(creditId, updatedCredit), HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:4200")

    @DeleteMapping("deletecredit/{creditId}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long creditId) {
        creditService.deleteCredit(creditId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
