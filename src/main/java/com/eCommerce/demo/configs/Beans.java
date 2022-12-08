package com.eCommerce.demo.configs;

import com.eCommerce.demo.services.AppUserServices;
import com.eCommerce.demo.services.EmailSender;
import com.eCommerce.demo.services.ProductsServices;
import com.eCommerce.demo.services.implemintations.AppUserServicesImpl;
import com.eCommerce.demo.services.implemintations.EmailSenderImpl;
import com.eCommerce.demo.services.implemintations.ProductsServicesImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Bean
    public ProductsServices productsServices (){return new ProductsServicesImpl();}
    @Bean
    public AppUserServices appUserServices(){return new AppUserServicesImpl(); }
    @Bean
    public EmailSender emailSender(){
        return new EmailSenderImpl();
    }


    @Bean
    public RestTemplate restTemplate (){
        return new RestTemplate();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder(10);
    }

}
