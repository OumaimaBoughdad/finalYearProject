package clients.clients_service.service.impl;

import clients.clients_service.entity.Client;
import clients.clients_service.repository.ClientRepository;
import clients.clients_service.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ClientRepository clientRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public ClientServiceImpl(ClientRepository clientRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.clientRepository = clientRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper(); // For serializing events
    }

    private void sendClientEvent(String eventType, Client client) {
        try {
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", eventType);
            event.put("clientId", client.getIdClient());
            event.put("clientData", client);

            // Convert event to JSON string
            String message = objectMapper.writeValueAsString(event);

            // Log the event before sending
            System.out.println("DEBUG: Sending event to Kafka - " + message);

            // Send to Kafka
            kafkaTemplate.send("client-events", client.getIdClient().toString(), message);

            // Log successful send
            System.out.println("DEBUG: Event successfully sent to Kafka for clientId: " + client.getIdClient());
        } catch (JsonProcessingException e) {
            // Log the error
            System.err.println("ERROR: Failed to serialize client event - " + e.getMessage());
            throw new RuntimeException("Failed to serialize client event", e);
        }
    }


    @Override
    public Client createClient(Client client) {
        Client createdClient = clientRepository.save(client);
        sendClientEvent("CREATE", createdClient);
        return createdClient;
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

        Client updatedClient = clientRepository.save(client);
        sendClientEvent("UPDATE", updatedClient);
        return updatedClient;
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.deleteById(id);
        sendClientEvent("DELETE", client);
    }

    @Override
    public Optional<Client> getClientByCne(String cne) {
        return clientRepository.findByCne(cne);
    }


    // function to send the employee object to the kafka broker
    public void sendClient(Client client) {
        Message<Client> message = MessageBuilder
                .withPayload(client)
                .setHeader(KafkaHeaders.TOPIC, "clPost")
                .build();
        kafkaTemplate.send(message);
    }//cldelete

    // function to send the employee object to the kafka broker
    public void sendClientfordeletion(Client client) {
        Message<Client> message = MessageBuilder
                .withPayload(client)
                .setHeader(KafkaHeaders.TOPIC, "TOPICj")
                .build();
        kafkaTemplate.send(message);
    }

    public void sendClientforupdate(Client client) {
        Message<Client> message = MessageBuilder
                .withPayload(client)
                .setHeader(KafkaHeaders.TOPIC, "topicupdate")
                .build();
        kafkaTemplate.send(message);
    }

    @Override
    public Client getClientByIdd(Long idClient) {

        // SQL query to fetch the client details by ID
        String sql = "SELECT id_client, address, email, first_name, last_name, phone_number, employee_id, cne FROM clients WHERE id_client = ?";

        // Execute the query and map the result to a Client object
        Client client = jdbcTemplate.queryForObject(sql, new Object[]{idClient}, (rs, rowNum) -> {
            Client c = new Client();
            c.setIdClient(rs.getLong("id_client"));
            c.setAddress(rs.getString("address"));
            c.setEmail(rs.getString("email"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
            c.setPhoneNumber(rs.getString("phone_number"));
            c.setCne(rs.getString("cne"));
            return c;
        });

        return client;
    }


}
