package org.example.credit_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Add this annotation
public class CreditMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditMicroserviceApplication.class, args);
    }

}
