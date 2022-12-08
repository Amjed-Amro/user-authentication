package com.eCommerce.demo.services.implemintations;


import com.eCommerce.demo.intities.ConfirmationToken;
import com.eCommerce.demo.repository.ConfirmationTokenRepository;
import com.eCommerce.demo.services.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken (ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationTokenByToken (String token){
        return confirmationTokenRepository.findByToken(token);
    }
}
