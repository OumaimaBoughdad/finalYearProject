package clients.clients_service.service;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Client createClient(Client client);
    List<Client> getAllClients();
    Optional<Client> getClientById(Long id);
    Client updateClient(Long id, Client clientDetails);
    void deleteClient(Long id);
    ResponseEntity<Employee> getEmployeeById(Long id );
}
