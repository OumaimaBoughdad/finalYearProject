package com.example.compte_service.controller;

import com.example.compte_service.entity.Compte;
import com.example.compte_service.entity.Compte.TypeCompte;
import com.example.compte_service.entity.Employee;
import com.example.compte_service.reposetory.CompteRepository;
import com.example.compte_service.reposetory.EmployeeRepository;
import com.example.compte_service.service.CompteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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









}
