package com.eCommerce.demo.configs.security;


import com.eCommerce.demo.services.AppUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/registration").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.POST,"/users/changePassword").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/getUserByEmail/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/confirmToken/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/enableAccount/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/disAbleAccountAccount/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/unlockAccount/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/lockAccount/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/refreshToken").permitAll();

        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserExpired/**").denyAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserNonExpired/**").denyAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserCredentialsExpired/**").denyAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/setAppUserCredentialsNonExpired/**").denyAll();


        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByRatingGreaterThanEqual/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByBrand/**").permitAll();
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/products/loadAllByCategory/**").permitAll();

        http.authorizeHttpRequests().requestMatchers("/users/getAllUsers").hasAuthority(SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/addSuperAdminRole/**").hasAuthority(SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/removeSuperAdminRole/**").hasAuthority(SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/addAdminRole/**").hasAuthority(SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/removeAdminRole/**").hasAuthority(SUPER_ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/addUserRole/**").hasAnyAuthority(SUPER_ADMIN,ADMIN);
        http.authorizeHttpRequests().requestMatchers(HttpMethod.GET,"/users/removeUserRole/**").hasAnyAuthority(SUPER_ADMIN,ADMIN);
//        http.authorizeHttpRequests().requestMatchers("/logout").permitAll();
//        http.formLogin().loginPage("/login").permitAll();
//        http.logout().logoutUrl("/logout").permitAll().clearAuthentication(Boolean.TRUE).invalidateHttpSession(Boolean.TRUE).deleteCookies().logoutSuccessUrl("/home");

        http.authorizeHttpRequests().anyRequest().hasAnyAuthority(USER);
        http.addFilter(new CustomAuthenticationFilter(authenticationManager()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.formLogin().and().build();
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
