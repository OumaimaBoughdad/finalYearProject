package org.example.credit_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

public class CreditMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditMicroserviceApplication.class, args);
    }

}
