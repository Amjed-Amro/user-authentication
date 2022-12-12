package com.eCommerce.demo;import com.eCommerce.demo.constants.Constants;


import com.eCommerce.demo.services.ProductsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication {
    @Autowired
    private ProductsServices productsServices;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
