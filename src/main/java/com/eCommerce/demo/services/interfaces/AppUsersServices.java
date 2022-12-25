package com.eCommerce.demo.services.interfaces;

import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AppUsersServices {
    ResponseDto registration(RegistrationDto registrationDto, HttpServletRequest request);

    ResponseDto activateAccount(String path, HttpServletRequest request);

    ResponseDto resendConfirmationEmail(String email, HttpServletRequest request);

    ResponseDto requestPasswordReset(String email, HttpServletRequest request);

    ResponseDto resetPassword(String path, String password, String confirmPassword, HttpServletRequest request);

    ResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response);
}
