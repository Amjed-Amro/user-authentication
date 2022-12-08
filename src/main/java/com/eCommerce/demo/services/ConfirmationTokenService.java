package com.eCommerce.demo.services;

import com.eCommerce.demo.intities.ConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {

    public void saveConfirmationToken (ConfirmationToken token);
    public Optional<ConfirmationToken> findConfirmationTokenByToken (String token);
}
