package org.example.credit_microservice.service;


import org.example.credit_microservice.entities.Credit;
import org.example.credit_microservice.entities.CreditClient;
import org.example.credit_microservice.repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;
import org.example.credit_microservice.repositories.ClientRepository;


@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ClientRepository creditClientRepository;

    // Create a new credit
    public Credit createCredit(Credit credit) {
        // Fetch the client details based on the provided client ID (cni)
        CreditClient client = creditClientRepository.findById(credit.getClient().getCni())
                .orElseThrow(() -> new RuntimeException("Client not found with CNI: " + credit.getClient().getCni()));

        // Set the client details in the credit object
        credit.setClient(client);

        // Calculate loanGrade based on client's income
        credit.setLoanGrade(calculateLoanGrade(client.getPersonIncome()));

        // Save the credit entity (loanPercentIncome is calculated internally)
        return creditRepository.save(credit);
    }

    // Get all credits
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    // Get a credit by ID
    public Optional<Credit> getCreditById(Long creditId) {
        return creditRepository.findById(creditId);
    }

    // Update a credit
    public Credit updateCredit(Long creditId, Credit updatedCredit) {
        return creditRepository.findById(creditId)
                .map(credit -> {
                    // Fetch the client details based on the provided client ID (cni)
                    CreditClient client = creditClientRepository.findById(updatedCredit.getClient().getCni())
                            .orElseThrow(() -> new RuntimeException("Client not found with CNI: " + updatedCredit.getClient().getCni()));

                    // Set the client details in the credit object
                    credit.setClient(client);

                    // Update the loan intent and amount
                    credit.setLoanIntent(updatedCredit.getLoanIntent());
                    credit.setLoanAmnt(updatedCredit.getLoanAmnt());

                    // Recalculate loanGrade (loanPercentIncome is calculated internally)
                    credit.setLoanGrade(calculateLoanGrade(client.getPersonIncome()));

                    // Save the updated credit entity
                    return creditRepository.save(credit);
                })
                .orElseThrow(() -> new RuntimeException("Credit not found with ID: " + creditId));
    }

    // Delete a credit
    public void deleteCredit(Long creditId) {
        creditRepository.deleteById(creditId);
    }

    // Helper method to calculate loan grade based on income
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
}