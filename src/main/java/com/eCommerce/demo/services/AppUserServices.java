package com.eCommerce.demo.services;

import com.eCommerce.demo.intities.AppUser.AppUserRoles;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserServices extends UserDetailsService {

    public ResponseDto loadAllAppUsers();


    public ResponseDto setAppUserLockedStatus(String email, Boolean isNonLocked, HttpServletRequest httpRequest);
    public ResponseDto setAppUserEnabledStatus(String email, Boolean isEnabled, HttpServletRequest httpRequest);
    public ResponseDto confirmToken(String token, HttpServletRequest httpRequest);

    ResponseDto setAppUserExpiredStatus(String email, Boolean isNonExpired, HttpServletRequest httpRequest);

    ResponseDto setAppUserCredentialsExpiredStatus(String email, Boolean isCredentialsNonExpired, HttpServletRequest httpRequest);

    ResponseDto changePassword(String email, String password, HttpServletRequest httpRequest);

    public ResponseDto saveNewAppUser(RegistrationDto registrationDto);
    public ResponseDto addRoleToAppUser (String email, String role, HttpServletRequest httpRequest);
    public ResponseDto deleteAppUserAccount (String email);
}
