package clients.clients_service.service.impl;


import clients.clients_service.entity.Petit;
import clients.clients_service.events.ClientPlacedEvent;
import clients.clients_service.events.PetitAdded;
import clients.clients_service.repository.PetitRepository;
import clients.clients_service.service.PetitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetitServiceImp  implements PetitService {


    //inject kafka service
    private final KafkaTemplate<String, PetitAdded> kafkaTemplate;

    public PetitServiceImp(KafkaTemplate<String, PetitAdded> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Autowired
    private PetitRepository petitRepository;

    @Override
    public List<Petit> getPetit() {
        return petitRepository.findAll();
    }

    @Override
    public Petit addPetit(Petit petit) {
        petitRepository.save(petit);
        kafkaTemplate.send("employeeService", new PetitAdded(petit.getIdPetit()));
        return petit;
    }
}
