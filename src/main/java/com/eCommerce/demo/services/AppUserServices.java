package com.eCommerce.demo.services;

import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserServices extends UserDetailsService {

    public ResponseDto loadAllAppUsers();


    @Transactional
    public ResponseDto setAppUserLockedStatus(String email, Boolean isNonLocked, HttpServletRequest httpRequest);
    @Transactional
    public ResponseDto setAppUserEnabledStatus(String email, Boolean isEnabled, HttpServletRequest httpRequest);
    @Transactional
    public ResponseDto confirmToken(String token, HttpServletRequest httpRequest);

    @Transactional
    ResponseDto setAppUserExpiredStatus(String email, Boolean isNonExpired, HttpServletRequest httpRequest);

    @Transactional
    ResponseDto setAppUserCredentialsExpiredStatus(String email, Boolean isCredentialsNonExpired, HttpServletRequest httpRequest);

    @Transactional
    ResponseDto changePassword(String email, String password, HttpServletRequest httpRequest);

    public ResponseDto saveNewAppUser(RegistrationDto registrationDto);
}
