package clients.clients_service.service;


import clients.clients_service.entity.Petit;

import java.util.List;

public interface PetitService {
    List<Petit> getPetit();
    Petit addPetit(Petit petit);
}
