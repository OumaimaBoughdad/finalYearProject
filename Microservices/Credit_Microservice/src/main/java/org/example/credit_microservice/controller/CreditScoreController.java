package org.example.credit_microservice.controller;

import org.example.credit_microservice.DTO.PredictLoanRequest;
import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.example.credit_microservice.service.CreditScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CreditScoreController {

    @Autowired
    private CreditScoreService creditScoreService;

    @PostMapping("/predict")
    public ResponseEntity<String> predictLoanStatus(@RequestBody LoanPredictionRequest request) {
        String loanStatusMessage = creditScoreService.predictCreditScore(request);

        return ResponseEntity.ok(loanStatusMessage);
    }


    @PostMapping("/predictbyCNE")
    public ResponseEntity<String> predictLoanStatus(@RequestBody PredictLoanRequest predictLoanRequest) {
        LoanPredictionRequest request = creditScoreService.prepareLoanPredictionRequest(
                predictLoanRequest.getCne(),
                predictLoanRequest.getLoanIntent(),
                predictLoanRequest.getLoanAmnt()
        );

        String loanStatusMessage = creditScoreService.predictCreditScore(request);

        return ResponseEntity.ok(loanStatusMessage);
    }


}
