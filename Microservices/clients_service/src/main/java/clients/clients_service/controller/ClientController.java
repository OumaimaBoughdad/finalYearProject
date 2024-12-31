package clients.clients_service.controller;

import clients.clients_service.entity.Client;
import clients.clients_service.entity.Employee;
import clients.clients_service.repository.EmployeeRepository;
import clients.clients_service.service.ClientService;
import clients.clients_service.service.impl.ClientServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private final ClientServiceImpl clientServiceImpl;
    private EmployeeRepository employeeRepository;

    public ClientController(ClientService clientService, EmployeeRepository employeeRepository, ClientServiceImpl clientServiceImpl) {
        this.clientService = clientService;
        this.employeeRepository = employeeRepository;
        this.clientServiceImpl = clientServiceImpl;
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client,@RequestHeader("loggedInUser")  String loggedInUser) {

        Employee employee = employeeRepository.findByEmail(loggedInUser);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found for email: " + loggedInUser);
        }

        client.setEmployee(employee);
        return ResponseEntity.ok(clientService.createClient(client));
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
        return ResponseEntity.ok(clientService.updateClient(id, clientDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
    //get the authentificated employee using rest API
    @GetMapping("/{idEmployee}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long idEmployee) {
        return clientServiceImpl.getEmployeeById(idEmployee);
    }
}
