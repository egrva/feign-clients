package ru.aegorova.eurekanamingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaNamingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaNamingServiceApplication.class, args);
	}

}
