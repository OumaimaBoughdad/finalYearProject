package com.example.compte_service.config;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configkafka {
    public NewTopic createTopic5(){
        return new NewTopic("tpc", 5, (short) 1);
    }

}
