import clients.clients_service.entity.Client;
import clients.clients_service.repository.ClientRepository;
import clients.clients_service.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(clientRepository.save(client)).thenReturn(client);

        // Act
        Client createdClient = clientService.createClient(client);

        // Assert
        assertNotNull(createdClient, "Created client should not be null");
        assertEquals("John", createdClient.getFirstName(), "First name should match");
        assertEquals("Doe", createdClient.getLastName(), "Last name should match");
        assertEquals("john.doe@example.com", createdClient.getEmail(), "Email should match");

        // Verify that clientRepository.save() was called once
        verify(clientRepository, times(1)).save(client);

        // Verify that sendClientEvent() was called with the correct arguments
        verify(kafkaTemplate, times(1)).send(eq("client-events"), eq(client.getIdClient().toString()), anyString());
    }
}
