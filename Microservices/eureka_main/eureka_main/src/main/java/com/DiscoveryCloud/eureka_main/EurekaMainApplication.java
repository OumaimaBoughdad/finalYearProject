package com.DiscoveryCloud.eureka_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaMainApplication.class, args);
	}

}
