package com.example.compte_service.reposetory;

import com.example.compte_service.entity.CarteBancaire;
import com.example.compte_service.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarteBancaireRepository extends JpaRepository<CarteBancaire, Long> {

}
