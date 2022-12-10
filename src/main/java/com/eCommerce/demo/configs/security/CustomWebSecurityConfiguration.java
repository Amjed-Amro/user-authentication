package com.eCommerce.demo.configs.security;


import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.intities.AppUser.AppUserRoles;
import com.eCommerce.demo.services.AppUserServices;
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
        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests().requestMatchers("/home").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/registration").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/confirmToken/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByRatingGreaterThanEqual/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByBrand/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByCategory/**").permitAll();

        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/setAdmin").hasAnyRole(Constants.ROLES.SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/setUser").hasAnyRole(Constants.ROLES.ADMIN,Constants.ROLES.SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/setSuperAdmin").hasAnyRole(Constants.ROLES.SUPER_ADMIN);



        http.authorizeHttpRequests().requestMatchers("/logout").permitAll();


        http.formLogin().loginPage("/login")
                .permitAll();

        http.logout().logoutUrl("/logout").permitAll()
                .clearAuthentication(Boolean.TRUE)
                .invalidateHttpSession(Boolean.TRUE)
                .deleteCookies().logoutSuccessUrl("/home");

        http.authorizeHttpRequests().anyRequest().authenticated();
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
