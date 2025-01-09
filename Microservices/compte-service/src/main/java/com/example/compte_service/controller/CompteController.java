package com.example.compte_service.controller;

import com.example.compte_service.entity.CarteBancaire;
import com.example.compte_service.entity.Client;
import com.example.compte_service.entity.Compte;
import com.example.compte_service.entity.Compte.TypeCompte;
import com.example.compte_service.entity.Employee;
import com.example.compte_service.reposetory.CarteBancaireRepository;
import com.example.compte_service.reposetory.ClientRepository;
import com.example.compte_service.reposetory.CompteRepository;
import com.example.compte_service.reposetory.EmployeeRepository;
import com.example.compte_service.service.CompteService;
import com.example.compte_service.service.CompteServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/compte")
public class CompteController {


    Logger log = LoggerFactory.getLogger(CompteController.class);

    @Autowired
    private CompteService compteService;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CarteBancaireRepository carteBancaireRepository;

    @Autowired
    private CompteServiceImpl compteserviceimpl;




    @PostMapping("/{clientId}")
    public Compte createCompte(@PathVariable long clientId,
                               @RequestBody CompteRequest compteRequest,
                               @RequestHeader("loggedInUser") String loggedInUser) {
        // Récupérer l'employé authentifié à partir de l'email
        Employee employee = employeeRepository.findEmployeeByEmail(loggedInUser);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found for the provided email.");
        }

        Compte compte = compteService.createCompte(
                compteRequest.getNumeroCompte(),
                compteRequest.getTypeCompte(),
                compteRequest.getSolde(),
                clientId,
                employee.getIdEmployee() // Utilisation de l'ID de l'employé authentifié
        );
        return compte;
    }



    @GetMapping
    public ResponseEntity<List<Compte>> getAllComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/{numeroCompte}")
    public ResponseEntity<Compte> getCompteByNumero(@PathVariable String numeroCompte) {
        Compte compte = compteService.getCompteByNumero(numeroCompte);
        return ResponseEntity.ok(compte);
    }

    @GetMapping("/cne/{cne}")
    public ResponseEntity<List<Compte>> getComptesByClientCne(@PathVariable String cne) {
        List<Compte> comptes = compteService.getComptesByClientCne(cne);
        return ResponseEntity.ok(comptes);
    }



    @PutMapping("/put/{id}")
    public ResponseEntity<Compte> updateCompte(@PathVariable Long id, @RequestBody Compte updatedCompte) {
        Optional<Compte> compteOptional = compteRepository.findById(id);
        if (compteOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Compte compte = compteOptional.get();
        compte.setSolde(updatedCompte.getSolde());
        // Mettez à jour d'autres champs si nécessaire
        Compte savedCompte = compteRepository.save(compte);
        return ResponseEntity.ok(savedCompte);
    }


    @DeleteMapping("/{idCompte}")
    public ResponseEntity<String> deleteCompte(@PathVariable long idCompte) {
        // Rechercher le compte
        Optional<Compte> optionalCompte = compteRepository.findById(idCompte);
        if (optionalCompte.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Compte avec l'ID " + idCompte + " introuvable");
        }

        Compte compte = optionalCompte.get();
        Client client = compte.getClient();

        // Supprimer les cartes bancaires associées au compte
        Set<CarteBancaire> cartesBancaires = compte.getCartes();
        if (cartesBancaires != null) {
            for (CarteBancaire carte : cartesBancaires) {
                carteBancaireRepository.delete(carte);
            }
        }

        // Supprimer le compte
        compteRepository.delete(compte);

        // Vérifier si le client n'a plus de comptes
        if (client != null && client.getComptes().isEmpty()) {
            clientRepository.delete(client);
            if(client!=null){
                compteserviceimpl.sendClientfordeletion(client);
                log.info("cchhchc");

            }

            return ResponseEntity.ok("Compte et cartes bancaires supprimés, client associé supprimé car il n'avait plus de comptes");
        }

        return ResponseEntity.ok("Compte et cartes bancaires supprimés avec succès");
    }


}
