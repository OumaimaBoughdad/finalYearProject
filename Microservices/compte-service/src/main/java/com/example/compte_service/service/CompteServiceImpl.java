package com.example.compte_service.service;

import com.example.compte_service.entity.CarteBancaire;
import com.example.compte_service.entity.Client;
import com.example.compte_service.entity.Compte;
import com.example.compte_service.reposetory.CarteBancaireRepository;
import com.example.compte_service.reposetory.ClientRepository;
import com.example.compte_service.reposetory.CompteRepository;
import com.example.compte_service.reposetory.EmployeeRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class CompteServiceImpl implements CompteService {
    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private CarteBancaireRepository carteBancaireRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
@Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Instancier le BCryptPasswordEncoder


    public CompteServiceImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Compte createCompte(String numeroCompte, Compte.TypeCompte typeCompte, double solde, long clientId, long employeeId) {
        Compte compte = new Compte();
        compte.setNumeroCompte(numeroCompte);
        compte.setTypeCompte(typeCompte);
        compte.setSolde(solde);
        compte.setDateOuverture(LocalDate.now());

        // Associer le client
        compte.setClient(clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found")));


        // Associer l'employé
        compte.setEmployee(employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found")));

        // Sauvegarder le compte
        Compte savedCompte = compteRepository.save(compte);

        // Générer une carte bancaire
        generateCarteBancaire(savedCompte);


        return savedCompte;
    }



    private void generateCarteBancaire(Compte compte) {
        CarteBancaire carteBancaire = new CarteBancaire();
        carteBancaire.setNumeroCarte(UUID.randomUUID().toString()); // Générer un numéro unique
        carteBancaire.setDateExpiration(LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("MM/yy")));

        // Générer un code PIN aléatoire à 4 chiffres
        String codePin = String.valueOf((int) (Math.random() * 9000) + 1000);

        // Hacher le code PIN avant de le stocker
        String hashedPin = passwordEncoder.encode(codePin); // Hachage avec BCrypt

        carteBancaire.setCodePin(hashedPin); // Stocker le code PIN haché
        carteBancaire.setLimiteCarte(10000.0); // Limite exemple
        carteBancaire.setCompte(compte);

        // Sauvegarder la carte bancaire dans la base de données
        carteBancaireRepository.save(carteBancaire);
    }

    @Override
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    @Override
    public Compte getCompteByNumero(String numeroCompte) {
        return compteRepository.findByNumeroCompte(numeroCompte)
                .orElseThrow(() -> new RuntimeException("Compte avec le numéro " + numeroCompte + " introuvable"));
    }

    @Override
    public List<Compte> getComptesByClientCne(String cne) {
        // Vérifier si un client avec le CNE existe
        Client client = clientRepository.findByCne(cne)
                .orElseThrow(() -> new RuntimeException("Client avec le CNE " + cne + " introuvable"));

        // Récupérer les comptes associés au client
        return compteRepository.findByClient(client);
    }


    public void sendClientfordeletion(Client client) {
        Message<Client> message = MessageBuilder
                .withPayload(client)
                .setHeader(KafkaHeaders.TOPIC, "tpc")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendcompteforadd(Compte compte) {
        Message<Compte> message = MessageBuilder
                .withPayload(compte)
                .setHeader(KafkaHeaders.TOPIC, "comptadd")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendComptefodelet(Compte compte) {
        Message<Compte> message = MessageBuilder
                .withPayload(compte)
                .setHeader(KafkaHeaders.TOPIC, "comptdelet")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendCompteforupdate(Compte compte) {
        Message<Compte> message = MessageBuilder
                .withPayload(compte)
                .setHeader(KafkaHeaders.TOPIC, "compteup")
                .build();
        kafkaTemplate.send(message);
    }

}
