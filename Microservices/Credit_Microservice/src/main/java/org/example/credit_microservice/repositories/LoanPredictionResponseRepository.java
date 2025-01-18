package org.example.credit_microservice.repositories;

import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanPredictionResponseRepository extends JpaRepository<LoanPredictionResponse, Long> {

    // Custom query to find LoanPredictionResponse by LoanPredictionRequest
    @Query("SELECT r FROM LoanPredictionResponse r WHERE r.loanPredictionRequest = :request")
    Optional<LoanPredictionResponse> findByLoanPredictionRequest(@Param("request") LoanPredictionRequest request);
}