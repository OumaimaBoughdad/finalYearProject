package clients.clients_service.controller;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import clients.clients_service.repository.ClientRepository;
import clients.clients_service.repository.EmployeeRepository;
import clients.clients_service.service.ClientService;
import clients.clients_service.service.impl.ClientServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    Logger log = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    private ClientServiceImpl clientServiceImpl;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client,@RequestHeader("loggedInUser")  String loggedInUser) {

        Employee employee = employeeRepository.findByEmail(loggedInUser);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for email: " + loggedInUser);
        }
        client.setEmployee(employee);
        clientService.createClient(client);
        clientService.sendClient(client);
        return ResponseEntity.ok(client);


    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client clientDetails) {

        clientService.updateClient(id, clientDetails);
        Client client = clientServiceImpl.getClientByIdd(id);
        if (client != null) {
            clientServiceImpl.sendClientforupdate(client);
            log.info("client was sent to be update "+client.getIdClient());
        }
        clientService.updateClient(id, clientDetails);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {

        Client client = clientServiceImpl.getClientByIdd(id);
        clientService.deleteClient(id);

        if (client != null) {
            clientServiceImpl.sendClientfordeletion(client);
            log.info("client was sent to be deleted "+client.getIdClient());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cne/{cne}")
    public ResponseEntity<Client> getClientByCne(@PathVariable String cne) {
        Optional<Client> client = clientService.getClientByCne(cne);
        return client.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
