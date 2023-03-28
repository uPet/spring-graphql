package com.example.edge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class EdgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeApplication.class, args);
	}

	@Bean
	WebClient http(WebClient.Builder builder){
		return builder.build();
	}
}

@Controller
class CustomerGraphqlController {
	private final WebClient client;

	CustomerGraphqlController(WebClient client) {
		this.client = client;
	}
	@SchemaMapping(typeName = "Customer")
	Profile profile(Customer customer) {
		return new Profile(customer.id());
	}

	// @SchemaMapping(typeName = "Query", field = "customers")
	@QueryMapping()
	Flux<Customer> customers(){
		return this.client.get()
				.uri("http://localhost:8080/customers")
				.retrieve()
				.bodyToFlux(Customer.class);
	}
}

record Profile(Integer id){}

record Customer(Integer id, String name) {}