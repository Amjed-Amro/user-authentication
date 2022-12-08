package com.eCommerce.demo;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.intities.Product;
import com.eCommerce.demo.repository.AppUserRepository;
import com.eCommerce.demo.repository.ProductsRepository;
import com.eCommerce.demo.services.ProductsServices;
import com.eCommerce.demo.services.implemintations.ProductsServicesImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private ProductsRepository productsRepository;
	@Autowired
	private ProductsServices productsServices;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner() throws JsonProcessingException {
		List<Product> s= productsServices.parseProducts(Constants.PRODUCTS_URL);
		productsServices.saveProducts(s);
		productsServices.getAllProducts();
		return null;
	}

}
