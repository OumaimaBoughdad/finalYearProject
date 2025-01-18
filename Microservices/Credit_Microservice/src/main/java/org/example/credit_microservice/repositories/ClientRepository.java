package org.example.credit_microservice.repositories;

import org.example.credit_microservice.entities.CreditClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<CreditClient, Long> {
    Optional<CreditClient> findByCni(Long cni);

}

