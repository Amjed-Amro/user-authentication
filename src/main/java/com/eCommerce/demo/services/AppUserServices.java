package com.eCommerce.demo.services;

import com.eCommerce.demo.intities.AppUser.AppUser;
import com.eCommerce.demo.intities.ConfirmationToken;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AppUserServices extends UserDetailsService {


    void saveConfirmationToken(ConfirmationToken token);

    Optional<ConfirmationToken> findConfirmationTokenByToken(String token);

    ConfirmationToken createNewToken(AppUser appUser);

    ResponseDto confirmToken(String token, HttpServletRequest httpRequest);

    ResponseDto refreshAccessToken (HttpServletRequest request, HttpServletResponse response);

    ResponseDto updateAppUser(AppUser appUser, HttpServletRequest httpRequest);

    ResponseDto deleteAppUserAccount(String email);

    ResponseDto loadAllAppUsers();

    ResponseDto addRoleToAppUser(String email, String role, HttpServletRequest httpRequest);

    ResponseDto removeRoleFromAppUser(String email, String role, HttpServletRequest httpRequest);

    ResponseDto unLockAppUser(String email, HttpServletRequest httpRequest);

    ResponseDto lockAppUser(String email, HttpServletRequest httpRequest);

    ResponseDto enableAppUser(String email, HttpServletRequest httpRequest);

    ResponseDto disableAppUser(String email, HttpServletRequest httpRequest);

    ResponseDto setAppUserNonExpired(String email, HttpServletRequest httpRequest);

    ResponseDto setAppUserExpired(String email, HttpServletRequest httpRequest);

    ResponseDto setAppUserCredentialsNonExpired(String email, HttpServletRequest httpRequest);

    ResponseDto setAppUserCredentialsExpired(String email, HttpServletRequest httpRequest);

    ResponseDto changeAppUserPassword(String email, String password, HttpServletRequest httpRequest);

    ResponseDto saveNewAppUser(RegistrationDto registrationDto);
}
