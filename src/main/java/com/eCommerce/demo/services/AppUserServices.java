package com.eCommerce.demo.services;

import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserServices extends UserDetailsService {

    public ResponseDto saveNewAppUser(RegistrationDto registrationDto);
    public ResponseDto loadAllUsers ();
    public void enableAppUser (String email);
    public ResponseDto confirmToken(String token);
    public void unLockAppUser (String email);
}
