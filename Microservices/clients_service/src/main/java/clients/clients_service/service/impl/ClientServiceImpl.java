package clients.clients_service.service.impl;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import clients.clients_service.repository.ClientRepository;
import clients.clients_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {


    @Autowired
    private RestTemplate restTemplate;

    private final ClientRepository clientRepository;
    @Autowired
    private WebClient webclient;

    public ClientServiceImpl(ClientRepository clientRepository) {

        this.clientRepository = clientRepository;

    }

    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
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

    @Override
    public ResponseEntity<Employee> getEmployeeById(Long id) {
      Employee employee=  webclient.get()
                .uri("/api/employees/" + id)
                .retrieve()
                .bodyToMono(Employee.class)
                .block();//async
        return ResponseEntity.ok(employee);
    }
}
