package com.example.transaction_service.reposetory;

import com.example.transaction_service.entity.Compte;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompteRepository extends JpaRepository<Compte, Long > {


}
