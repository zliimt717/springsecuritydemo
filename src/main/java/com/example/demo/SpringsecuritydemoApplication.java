package com.example.demo;

import com.example.demo.entity.Customer;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringsecuritydemoApplication {

	@Autowired
	private UserRepository repository;

	@PostConstruct
	public void initUsers(){
		List<Customer> customers = Stream.of(
				new Customer(101,"SpringSecurity","123456","xin.gu1707@gmail.com"),
				new Customer(102,"user1","pwd1","us1@gmail.com"),
				new Customer(103,"user2","pwd2","us2@gmail.com"),
				new Customer(104,"user3","pwd3","us3@gmail.com")
		).collect(Collectors.toList());
		repository.saveAll(customers);
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringsecuritydemoApplication.class, args);
	}

}
