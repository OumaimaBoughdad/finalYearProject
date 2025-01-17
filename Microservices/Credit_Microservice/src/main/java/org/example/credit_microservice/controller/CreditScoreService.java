package org.example.credit_microservice.controller;

import jakarta.transaction.Transactional;
import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.example.credit_microservice.repositories.LoanPredictionRepository;
import org.example.credit_microservice.repositories.LoanPredictionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

//package org.example.credit_microservice.controller;
//
//
//import org.example.credit_microservice.entities.ClientData;
//import org.example.credit_microservice.entities.CreditScoreRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
////@RestController
////@RequestMapping("/api/loan")
////public class LoanPredictionController {
////
////    private final LoanPredictionService loanPredictionService;
////
////    public LoanPredictionController(LoanPredictionService loanPredictionService) {
////        this.loanPredictionService = loanPredictionService;
////    }
////
////    @PostMapping("/predict")
////    public ResponseEntity<LoanPredictionResponse> predictLoanStatus(@RequestBody LoanPredictionRequest request) {
////        LoanPredictionResponse response = loanPredictionService.predictLoanStatus(request);
////        return ResponseEntity.ok(response);
////    }
////}
////
////@RestController
////@RequestMapping("/api/credit-score")
////public class CreditScoreController {
////
////    @Autowired
////    private  LoanPredictionService loanPredictionService;
////    private static final String FLASK_API_URL = "http://localhost:5000/predict";
////
////    @PostMapping("/predict")
////    public ResponseEntity<LoanPredictionResponse> predictLoanStatus(@RequestBody LoanPredictionRequest request) {
////        LoanPredictionResponse response = loanPredictionService.predictLoanStatus(request);
////        return ResponseEntity.ok(response);
////    }
////
////
////}
//// src/main/java/com/example/demo/controller/LoanPredictionController.java
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Service
//public class CreditScoreService {
//
////    public Map<String, Object> predictCreditScore(Map<String, Object> clientData) {
////        String flaskUrl = "http://localhost:5000/predict";
////        RestTemplate restTemplate = new RestTemplate();
////        return restTemplate.postForObject(flaskUrl, clientData, Map.class);
////    }
//    public int predictCreditScore(ClientData client) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:5000/predict";
//
//        // Prepare and send the request to the ML model API
//        ResponseEntity<Integer> response = restTemplate.postForEntity(url, client, Integer.class);
//        int predictedScore = response.getBody();
//
//        return predictedScore;
//    }
//}
@Service
@Transactional

public class CreditScoreService {


    @Autowired
    private LoanPredictionRepository loanPredictionRepository;
    @Autowired
    private LoanPredictionResponseRepository LoanPredictionResponseRepository;

    public int predictCreditScore(LoanPredictionRequest loanRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5000/predict";

        // Prepare and send the request to the Flask ML API
        ResponseEntity<Map> response = restTemplate.postForEntity(url, loanRequest, Map.class);

        // Extract the loan status (loan prediction)
        int predictedLoanStatus = (int) response.getBody().get("prediction");

        // Save the loan prediction request to the database
        LoanPredictionRequest savedRequest = loanPredictionRepository.save(loanRequest);

        // Create and save the loan prediction response linked to the saved request
        LoanPredictionResponse loanResponse = new LoanPredictionResponse(savedRequest, predictedLoanStatus);
        LoanPredictionResponseRepository.save(loanResponse);

        return predictedLoanStatus;
    }

}
