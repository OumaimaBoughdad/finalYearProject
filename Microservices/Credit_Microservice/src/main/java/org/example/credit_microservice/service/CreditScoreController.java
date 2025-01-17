package org.example.credit_microservice.service;

import org.example.credit_microservice.controller.CreditScoreService;
import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditScoreController {

    @Autowired
    private CreditScoreService creditScoreService;

@PostMapping("/predict")
public ResponseEntity<LoanPredictionResponse> predictLoanStatus(@RequestBody LoanPredictionRequest request) {
    // Get the predicted loan status
    int predictedLoanStatus = creditScoreService.predictCreditScore(request);

    // Return the prediction in the response
    LoanPredictionResponse response = new LoanPredictionResponse(predictedLoanStatus);
    return ResponseEntity.ok(response);
}

}
