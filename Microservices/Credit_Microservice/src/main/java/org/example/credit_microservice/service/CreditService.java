package org.example.credit_microservice.service;


import org.example.credit_microservice.entities.Credit;
import org.example.credit_microservice.entities.CreditClient;
import org.example.credit_microservice.repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    public Credit saveCredit(Credit credit) {
        if (credit.getClient().getPersonIncome() == 0) { // or null, depending on your logic
            // Automatically calculate personIncome if not provided
            double calculatedIncome = calculateIncome(credit.getClient());
            credit.getClient().setPersonIncome(calculatedIncome);
        }

        // Save the Credit object
        return creditRepository.save(credit);
    }

    // Dummy method to calculate income, replace with your actual logic
    private double calculateIncome(CreditClient client) {
        // Example calculation, you can replace it with your own logic
        return client.getPersonAge() * client.getPersonEmpLength() * 1000;
    }


    // Get Credit by ID
    public Credit getCreditById(int id) {
        return creditRepository.findById((long) id).orElse(null);
    }

    // Get All Credits
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    // Delete Credit by ID
    public void deleteCredit(int id) {
        creditRepository.deleteById((long) id);
    }
}

