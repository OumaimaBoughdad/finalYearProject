package org.example.credit_microservice.repositories;

import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPredictionRepository extends JpaRepository<LoanPredictionRequest, Long> {
}
