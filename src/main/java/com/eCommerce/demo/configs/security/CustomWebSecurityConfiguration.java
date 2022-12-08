package com.eCommerce.demo.configs.security;


import com.eCommerce.demo.services.AppUserServices;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity

public class CustomWebSecurityConfiguration {

    @Autowired
    private AppUserServices appUserServices;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/registration").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/**").permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated()
                .and().addFilter(new CustomAuthenticationFilter(authenticationManager()))
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class).formLogin();
        return http.build();
    }



    @Bean
    public DaoAuthenticationProvider authenticationProvider (){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUserServices);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    public AuthenticationManager authenticationManager(){
        return new ProviderManager(authenticationProvider());
    }

}
