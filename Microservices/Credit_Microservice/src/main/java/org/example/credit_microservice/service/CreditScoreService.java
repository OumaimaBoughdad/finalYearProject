package org.example.credit_microservice.service;

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

    public String predictCreditScore(LoanPredictionRequest loanRequest) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:5000/predict";

        loanRequest.setLoan_int_rate(5.5);

        String loanGrade = calculateLoanGrade(loanRequest.getPerson_income());
        loanRequest.setLoan_grade(loanGrade);

        double loanPercentIncome = calculateLoanPercentIncome(loanRequest.getLoan_amnt(), loanRequest.getPerson_income());
        loanRequest.setLoan_percent_income(loanPercentIncome);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, loanRequest, Map.class);

        int predictedLoanStatus = (int) response.getBody().get("prediction");

        LoanPredictionRequest savedRequest = loanPredictionRepository.save(loanRequest);

        LoanPredictionResponse loanResponse = new LoanPredictionResponse(savedRequest, predictedLoanStatus);
        loanPredictionResponseRepository.save(loanResponse);

        return loanResponse.getLoan_status_message();
    }

    public LoanPredictionRequest prepareLoanPredictionRequest(Long cne, String loanIntent, double loanAmnt) {
        // Fetch client details using CNI
        CreditClient client = clientRepository.findByCni(cne)
                .orElseThrow(() -> new RuntimeException("Client not found with CNI: " + cne));

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

        return request;
    }

    private String calculateLoanGrade(double income) {
        if (income > 30000) {
            return "A";
        } else if (income > 10000) {
            return "B";
        } else if (income > 8000) {
            return "C";
        } else if (income > 5000) {
            return "D";
        }else if (income >1000) {
            return "E";
        }else{
            return "F";
        }
    }

    private double calculateLoanPercentIncome(double loanAmnt, double personIncome) {
        if (personIncome == 0) {
            return 0; // Avoid division by zero
        }
        return (loanAmnt / personIncome) ;
    }

}