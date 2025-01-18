package org.example.credit_microservice.service;

import ch.qos.logback.core.net.server.Client;
import jakarta.transaction.Transactional;
import org.example.credit_microservice.entities.CreditClient;
import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.example.credit_microservice.repositories.ClientRepository;
import org.example.credit_microservice.repositories.LoanPredictionRepository;
import org.example.credit_microservice.repositories.LoanPredictionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class CreditScoreService {

    @Autowired
    private LoanPredictionRepository loanPredictionRepository;

    @Autowired
    private LoanPredictionResponseRepository loanPredictionResponseRepository;

    @Autowired
    private ClientRepository clientRepository;

//    public String predictCreditScore(LoanPredictionRequest loanRequest) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://localhost:5000/predict";
//
//        // Set default loan_int_rate to 5.5
//        loanRequest.setLoan_int_rate(5.5);
//
//        // Calculate loan_grade based on client's income
//        String loanGrade = calculateLoanGrade(loanRequest.getPerson_income());
//        loanRequest.setLoan_grade(loanGrade);
//
//        // Calculate loan_percent_income
//        double loanPercentIncome = calculateLoanPercentIncome(loanRequest.getLoan_amnt(), loanRequest.getPerson_income());
//        loanRequest.setLoan_percent_income(loanPercentIncome);
//
//        // Prepare and send the request to the Flask ML API
//        ResponseEntity<Map> response = restTemplate.postForEntity(url, loanRequest, Map.class);
//
//        // Extract the loan status (loan prediction)
//        int predictedLoanStatus = (int) response.getBody().get("prediction");
//
//        // Save the loan prediction request to the database
//        LoanPredictionRequest savedRequest = loanPredictionRepository.save(loanRequest);
//
//        // Create and save the loan prediction response linked to the saved request
//        LoanPredictionResponse loanResponse = new LoanPredictionResponse(savedRequest, predictedLoanStatus);
//        loanPredictionResponseRepository.save(loanResponse);
//
//        // Return the descriptive message
//        loanResponse.setCreatedAt(LocalDateTime.now());
//        return loanResponse.getLoan_status_message();
//    }
public String predictCreditScore(LoanPredictionRequest loanRequest) {
    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:5000/predict";

    // Set default loan_int_rate to 5.5
    loanRequest.setLoan_int_rate(5.5);

    // Calculate loan_grade based on client's income
    String loanGrade = calculateLoanGrade(loanRequest.getPerson_income());
    loanRequest.setLoan_grade(loanGrade);

    // Calculate loan_percent_income
    double loanPercentIncome = calculateLoanPercentIncome(loanRequest.getLoan_amnt(), loanRequest.getPerson_income());
    loanRequest.setLoan_percent_income(loanPercentIncome);

    // Prepare and send the request to the Flask ML API
    ResponseEntity<Map> response = restTemplate.postForEntity(url, loanRequest, Map.class);

    // Extract the loan status (loan prediction)
    int predictedLoanStatus = (int) response.getBody().get("prediction");

    // Save the loan prediction request to the database
    LoanPredictionRequest savedRequest = loanPredictionRepository.save(loanRequest);

    // Create and save the loan prediction response linked to the saved request
    LoanPredictionResponse loanResponse = new LoanPredictionResponse(savedRequest, predictedLoanStatus);
    loanResponse.setCreatedAt(LocalDateTime.now()); // Explicitly set the createdAt field
    loanPredictionResponseRepository.save(loanResponse);

    // Return the descriptive message
    return loanResponse.getLoan_status_message();
}

    public LoanPredictionRequest prepareLoanPredictionRequest(Long cne, String loanIntent, double loanAmnt) {
        CreditClient client = clientRepository.findByCni(cne)
                .orElseThrow(() -> new RuntimeException("Client not found with CNI: " + cne));

        // Debugging: Print client details
        System.out.println("Client Details: " + client);

        // Calculate loan_grade based on income
        String loanGrade = calculateLoanGrade(client.getPersonIncome());

        // Calculate loan_percent_income
        double loanPercentIncome = calculateLoanPercentIncome(loanAmnt, client.getPersonIncome());

        // Prepare the LoanPredictionRequest object
        LoanPredictionRequest request = new LoanPredictionRequest();
        request.setPerson_age(client.getPersonAge());
        request.setPerson_income(client.getPersonIncome());
        request.setPerson_home_ownership(client.getPersonHomeOwnership());
        request.setPerson_emp_length(client.getPersonEmpLength());
        request.setLoan_grade(loanGrade);
        request.setLoan_int_rate(5.5); // Fixed value
        request.setLoan_percent_income(loanPercentIncome);
        request.setCb_person_default_on_file(client.getCbPersonDefaultOnFile());
        request.setCb_person_cred_hist_length(client.getCbPersonCredHistLength());
        request.setLoan_intent(loanIntent);
        request.setLoan_amnt(loanAmnt);

        // Debugging: Print LoanPredictionRequest details
        System.out.println("LoanPredictionRequest Details: " + request);

        return request;
    }
    private String calculateLoanGrade(double income) {
        if (income > 5000) {
            return "A";
        } else if (income > 3000) {
            return "B";
        } else if (income > 2000) {
            return "C";
        } else {
            return "D";
        }
    }

    private double calculateLoanPercentIncome(double loanAmnt, double personIncome) {
        if (personIncome == 0) {
            return 0; // Avoid division by zero
        }
        return (loanAmnt / personIncome) * 100;
    }

}