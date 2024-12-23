package clients.clients_service.service.impl;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import clients.clients_service.repository.ClientRepository;
import clients.clients_service.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private RestTemplate restTemplate;

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /*
    Posts posts = restTemplate.getForObject(
                "http://SERVICE_POST/post/2"
                ,Posts.class);
    * */
    @Override
    public Client createClient(Client client) {
        //Employee emp =restTemplate.getForObject("http://localhost:8080/api/employees/1", Employee.class);

        //client.setEmployee(emp);

        try {
            Employee emp = restTemplate.getForObject("http://localhost:8081/api/employees/1", Employee.class);

            if (emp == null) {
                throw new RuntimeException("Employee not found or service unavailable");
            }

            client.setEmployee(emp);
            return clientRepository.save(client);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to retrieve employee: " + ex.getMessage());
        }
       // return clientRepository.save(client);
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
