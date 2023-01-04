package com.eCommerce.demo.services.handlers.implementations;

import com.eCommerce.demo.intities.LoginIp;
import com.eCommerce.demo.repository.LoginIpsRepository;
import com.eCommerce.demo.services.handlers.interfaces.LoginIpsHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@Log4j2
public class LoginIpsHandlerImpl implements LoginIpsHandler {
    @Autowired
    private LoginIpsRepository loginIpsRepository;
    @Override
    public void addNewIpToEmail(String ip, String email){
        loginIpsRepository.save(LoginIp.builder().loginAt(LocalDateTime.now()).email(email).ipAddress(ip).build());
        log.info(String.format("new LoginIP was added to user with email %s", email));
    }
    @Override
    public Set<LoginIp> findAllLoginIpsByEmail(String email){
        log.info(String.format("new LoginIP was added to user with email %s", email));
        return loginIpsRepository.findAllByEmail(email); 
    }
}
