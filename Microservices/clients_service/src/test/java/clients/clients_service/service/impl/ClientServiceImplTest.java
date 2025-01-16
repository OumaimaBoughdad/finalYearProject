package clients.clients_service.service.impl;

import clients.clients_service.entity.Client;
import clients.clients_service.repository.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class) // Enable Mockito for this test class
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository; // Mock the ClientRepository

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate; // Mock the KafkaTemplate

    @InjectMocks
    private ClientServiceImpl clientService; // Inject mocks into ClientServiceImpl

    @Test
    void testCreateClient() {
        // Arrange
        Client client = new Client();
        client.setFirstName("John");
        client.setLastName("Doe");
        client.setEmail("john.doe@example.com");

        // Mock the behavior of clientRepository.save()
        Mockito.when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client createdClient = clientService.createClient(client);

        // Assert
        Assertions.assertNotNull(createdClient, "Created client should not be null");
        Assertions.assertEquals("John", createdClient.getFirstName(), "First name should match");
        Assertions.assertEquals("Doe", createdClient.getLastName(), "Last name should match");
        Assertions.assertEquals("john.doe@example.com", createdClient.getEmail(), "Email should match");

        // Verify that clientRepository.save() was called once
        Mockito.verify(clientRepository, Mockito.times(1)).save(client);

        // Verify that sendClientEvent() was called with the correct arguments
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send(ArgumentMatchers.eq("client-events"), ArgumentMatchers.eq(client.getIdClient().toString()), ArgumentMatchers.anyString());
    }
}
