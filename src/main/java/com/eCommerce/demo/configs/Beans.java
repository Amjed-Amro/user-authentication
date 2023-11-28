package com.eCommerce.demo.configs;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.eCommerce.demo.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Beans {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(Constants.ALGORITHM_SECRET_CODE.getBytes());
    }

    @Bean
    public JWTVerifier verifier() {
        return JWT.require(algorithm()).build();
    }


}
