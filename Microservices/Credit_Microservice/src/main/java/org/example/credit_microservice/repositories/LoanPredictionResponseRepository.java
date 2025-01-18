package org.example.credit_microservice.repositories;

import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanPredictionResponseRepository extends JpaRepository<LoanPredictionResponse, Long> {}
