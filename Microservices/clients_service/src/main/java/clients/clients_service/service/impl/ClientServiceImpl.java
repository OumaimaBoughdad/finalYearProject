package clients.clients_service.service.impl;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import clients.clients_service.events.ClientPlacedEvent;
import clients.clients_service.repository.ClientRepository;
import clients.clients_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    //inject kafka service
    private final KafkaTemplate<String,ClientPlacedEvent> kafkaTemplate;

    @Autowired
    private RestTemplate restTemplate;

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository, KafkaTemplate kafkaTemplate) {

        this.clientRepository = clientRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public Client createClient(Client client) {
        //Employee emp =restTemplate.getForObject("http://localhost:8080/api/employees/1", Employee.class);
        clientRepository.save(client);
        kafkaTemplate.send("notificationTopic", new ClientPlacedEvent(client.getIdClient()));
        return  client;
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Client updateClient(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setFirstName(clientDetails.getFirstName());
        client.setLastName(clientDetails.getLastName());
        client.setEmail(clientDetails.getEmail());
        client.setPhoneNumber(clientDetails.getPhoneNumber());
        client.setAddress(clientDetails.getAddress());

        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
