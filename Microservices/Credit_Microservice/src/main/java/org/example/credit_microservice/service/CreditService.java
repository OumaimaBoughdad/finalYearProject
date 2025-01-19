
package org.example.credit_microservice.service;

import org.example.credit_microservice.entities.Credit;
import org.example.credit_microservice.entities.CreditClient;
import org.example.credit_microservice.repositories.CreditRepository;
import org.example.credit_microservice.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ClientRepository creditClientRepository;

    public Credit createCredit(Long cni, double loanAmnt, String loanIntent) {
        // Fetch the client details based on the provided client ID (cni)
        CreditClient client = creditClientRepository.findById(cni)
                .orElseThrow(() -> new RuntimeException("Client not found with CNI: " + cni));

        // Create a new Credit entity
        Credit credit = new Credit();
        credit.setLoanIntent(loanIntent);
        credit.setLoanAmnt(loanAmnt);
        credit.setClient(client);
        credit.setLoanGrade(calculateLoanGrade(client.getPersonIncome()));

        // Save the credit entity (loanPercentIncome is calculated internally)
        return creditRepository.save(credit);
    }

    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    public Optional<Credit> getCreditById(Long creditId) {
        return creditRepository.findById(creditId);
    }

    public Credit updateCredit(Long creditId, Credit updatedCredit) {
        return creditRepository.findById(creditId)
                .map(credit -> {
                    CreditClient client = creditClientRepository.findById(updatedCredit.getClient().getCni())
                            .orElseThrow(() -> new RuntimeException("Client not found with CNI: " + updatedCredit.getClient().getCni()));

                    credit.setClient(client);
                    credit.setLoanIntent(updatedCredit.getLoanIntent());
                    credit.setLoanAmnt(updatedCredit.getLoanAmnt());
                    credit.setLoanGrade(calculateLoanGrade(client.getPersonIncome()));

                    return creditRepository.save(credit);
                })
                .orElseThrow(() -> new RuntimeException("Credit not found with ID: " + creditId));
    }

    public void deleteCredit(Long creditId) {
        creditRepository.deleteById(creditId);
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
}
