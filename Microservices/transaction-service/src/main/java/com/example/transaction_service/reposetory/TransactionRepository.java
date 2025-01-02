package com.example.transaction_service.reposetory;


import com.example.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByComptes_IdCompte(Long idCompte);

}
