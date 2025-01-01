package com.example.compte_service.reposetory;

import com.example.compte_service.entity.Client;
import com.example.compte_service.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    Optional<Compte> findByNumeroCompte(String numeroCompte);
    List<Compte> findByClient(Client client);

}
