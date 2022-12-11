package com.eCommerce.demo.configs.security;


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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static com.eCommerce.demo.constants.Constants.ROLES.*;


@Configuration
@EnableWebSecurity

public class CustomWebSecurityConfiguration {

    @Autowired
    private AppUserServices appUserServices;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authenticationProvider(authenticationProvider());
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/registration").permitAll();
//        http.authorizeHttpRequests().requestMatchers("/users/getAllUsers").authenticated();
//
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/getUserByEmail/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/confirmToken/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/enableAccount/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/disAbleAccountAccount/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/unlockAccount/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/lockAccount/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserExpired/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserNonExpired/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserCredentialsExpired/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserCredentialsNonExpired/**").permitAll();
//
//
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByRatingGreaterThanEqual/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByBrand/**").permitAll();
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByCategory/**").permitAll();
//
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/addSuperAdminRole/**").hasAnyRole(SUPER_ADMIN);
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/removeSuperAdminRole/**").hasAnyRole(SUPER_ADMIN);
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/addAdminRole/**").hasAnyRole(SUPER_ADMIN);
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/removeAdminRole/**").hasAnyRole(SUPER_ADMIN);
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/addUserRole/**").hasAnyRole(SUPER_ADMIN,ADMIN);
//        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/removeUserRole/**").hasAnyRole(SUPER_ADMIN,ADMIN);
//
//        http.authorizeHttpRequests().requestMatchers("/logout").permitAll();
//        http.formLogin().loginPage("/login").permitAll();
//        http.logout().logoutUrl("/logout").permitAll().clearAuthentication(Boolean.TRUE).invalidateHttpSession(Boolean.TRUE).deleteCookies().logoutSuccessUrl("/home");

        http.authorizeHttpRequests().anyRequest().permitAll();

        return http.build();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(appUserServices);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }

    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

}
