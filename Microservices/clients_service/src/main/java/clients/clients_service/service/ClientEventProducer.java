package clients.clients_service.service;

import clients.clients_service.entity.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ClientEventProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendClientEvent(String eventType, Client client) {
        String topic = "client-events";
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", eventType);
        event.put("clientId", client.getIdClient());
        event.put("clientData", client);

        try {

            String message = new ObjectMapper().writeValueAsString(event);

            RecordHeaders headers = new RecordHeaders();
            headers.add(new RecordHeader("__TypeId__", "com.example.compte_service.entity.Client".getBytes()));

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    null,
                    client.getIdClient().toString(),
                    message,
                    headers
            );

            kafkaTemplate.send(topic, client.getIdClient().toString(), message);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize client event", e);
        }
    }
}


