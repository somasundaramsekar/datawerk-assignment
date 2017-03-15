package me.soma.datawerks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class FileProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessingServiceApplication.class, args);
	}
}
