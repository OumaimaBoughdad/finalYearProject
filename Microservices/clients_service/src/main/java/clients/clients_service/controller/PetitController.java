package clients.clients_service.controller;


import clients.clients_service.entity.Client;
import clients.clients_service.entity.Petit;
import clients.clients_service.events.ClientPlacedEvent;
import clients.clients_service.events.PetitAdded;
import clients.clients_service.service.PetitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/petit")
public class PetitController {





    @Autowired
    private PetitService petitService;


    @PostMapping
    public ResponseEntity<Petit> createPetit(@RequestBody Petit petit) {
        return ResponseEntity.ok(petitService.addPetit(petit));
    }

    @GetMapping
    public ResponseEntity<List<Petit>> getAllPetits() {
        return ResponseEntity.ok(petitService.getPetit());
    }



}
