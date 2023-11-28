package com.eCommerce.demo.controllers.appuser;

import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResetPasswordDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.interfaces.AppUsersServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "v1/appuser")
@RequiredArgsConstructor
public class AppUserController {
    @Autowired
    private AppUsersServices appUsersServices;

    @PutMapping(path = "registration")
    public ResponseEntity<ResponseDto> registration(@RequestBody RegistrationDto registrationDto, HttpServletRequest request) {
        registrationDto.setPort(request.getRemotePort());
        registrationDto.setIpAddress(request.getRemoteAddr());
        return new ResponseEntity<>(appUsersServices.registration(registrationDto, request), HttpStatus.OK);
    }

    @PutMapping(path = "activation/{token}")
    public ResponseEntity<ResponseDto> activation(@PathVariable String token, HttpServletRequest request) {
        return new ResponseEntity<>(appUsersServices.activateAccount(token, request), HttpStatus.OK);
    }

    @PutMapping(path = "resetPassword/{token}")
    public ResponseEntity<ResponseDto> resetPassword(@PathVariable String token, @RequestBody ResetPasswordDto dto, HttpServletRequest request) {
        return new ResponseEntity<>(appUsersServices.resetPassword(token, dto.getPassword(), dto.getConfirmPassword(), request), HttpStatus.OK);
    }


    @GetMapping(path = "resendConfirmationEmail/{email}")
    public ResponseEntity<ResponseDto> resendConfirmationEmail(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUsersServices.resendConfirmationEmail(email, request), HttpStatus.OK);
    }

    @GetMapping(path = "requestPasswordReset/{email}")
    public ResponseEntity<ResponseDto> requestPasswordReset(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUsersServices.requestPasswordReset(email, request), HttpStatus.OK);
    }

    @GetMapping(path = "refreshToken")
    public ResponseEntity<ResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(appUsersServices.refreshAccessToken(request, response), HttpStatus.OK);
    }


}
