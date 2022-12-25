package com.eCommerce.demo.configs.security;

import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.services.interfaces.InternalServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Log4j2
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private InternalServices internalServices;
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, InternalServices internalServices) {
        this.authenticationManager = authenticationManager;
        this.internalServices = internalServices;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("username").toLowerCase();
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, password);
        return authenticationManager.authenticate(authenticationToken);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AppUserDao user = (AppUserDao) authentication.getPrincipal();
        log.info(String.format("User with email %s successfully authenticated",user.getUsername()));
        internalServices.checkLoginIpForEmail(user.getUsername(),request.getRemoteAddr());
        String accessToken = internalServices.createAccessToken(user,request);response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), accessToken);

    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException failed) throws IOException, ServletException {
        if (failed.getMessage().equals("User is disabled")){
            String s= "you need to activate your account";
            new ObjectMapper().writeValue(response.getOutputStream(), s);
        }else {
            new ObjectMapper().writeValue(response.getOutputStream(), "failed");
        }

    }
}
