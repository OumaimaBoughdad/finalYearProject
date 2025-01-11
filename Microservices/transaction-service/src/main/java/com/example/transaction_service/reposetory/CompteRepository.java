package com.example.transaction_service.reposetory;

import com.example.transaction_service.entity.Compte;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompteRepository extends JpaRepository<Compte, Long > {
    Optional<Compte> findById(Long id);
}
