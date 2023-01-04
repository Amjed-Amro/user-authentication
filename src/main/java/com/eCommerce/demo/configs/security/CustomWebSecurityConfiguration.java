package com.eCommerce.demo.configs.security;


import com.eCommerce.demo.services.handlers.interfaces.AppUsersHandler;
import com.eCommerce.demo.services.interfaces.InternalServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.eCommerce.demo.constants.Constants.ROLES.*;
import static org.springframework.security.config.http.SessionCreationPolicy.*;


@Configuration
@EnableWebSecurity

public class CustomWebSecurityConfiguration {

    @Autowired
    private AppUsersHandler appUsersHandler;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private InternalServices internalServices;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authenticationProvider(authenticationProvider());
        http.sessionManagement().sessionCreationPolicy(STATELESS);

        http.addFilter(new CustomAuthenticationFilter(authenticationManager(),internalServices));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests().requestMatchers("/home").permitAll();

        http.authorizeHttpRequests().requestMatchers("/appUser/*").permitAll();
        http.authorizeHttpRequests().requestMatchers("/appUser/*/*").permitAll();

        http.authorizeHttpRequests().requestMatchers("/users/*").hasAuthority(USER);
        http.authorizeHttpRequests().requestMatchers("/users/*/*").hasAuthority(USER);

        http.authorizeHttpRequests().requestMatchers("/superUser/*").hasAuthority(SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers("/superUser/*/*").hasAuthority(SUPER_ADMIN);

        http.authorizeHttpRequests().requestMatchers("/auth/user").hasAuthority(USER);
        http.authorizeHttpRequests().requestMatchers("/auth/admin").hasAuthority(ADMIN);
        http.authorizeHttpRequests().requestMatchers("/auth/super").hasAuthority(SUPER_ADMIN);

        http.authorizeHttpRequests().anyRequest().hasAnyAuthority(SUPER_ADMIN);
        return http.formLogin().and().build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUsersHandler);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

}
