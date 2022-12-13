package com.eCommerce.demo.controllers;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.models.dto.LoginDto;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.AppUserServices;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "users")
public class UsersController {

    @Autowired
    private AppUserServices appUserServices;

    @GetMapping(path = "getAllUsers")
    public ResponseEntity<ResponseDto> getAllUsers() {
        return new ResponseEntity<>(appUserServices.loadAllAppUsers(), HttpStatus.OK);}
    @PostMapping(path ="registration")
    public ResponseEntity<ResponseDto> registration(@RequestBody RegistrationDto registrationDto, HttpServletRequest httpRequest) {
        registrationDto.setPort(httpRequest.getRemotePort());
        registrationDto.setIpAddress(httpRequest.getRemoteAddr());
        return new ResponseEntity<>(appUserServices.saveNewAppUser(registrationDto), HttpStatus.OK);}
    @PostMapping(path = "changePassword")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody LoginDto dto, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.changeAppUserPassword(dto.getUsername(), dto.getPassword(), request), HttpStatus.OK);}
    @GetMapping(path = "getUserByEmail/{email}")
    public ResponseEntity<UserDetails> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(appUserServices.loadUserByUsername(email), HttpStatus.OK);}
    @GetMapping(path = "confirmToken/{token}")
    public ResponseEntity<ResponseDto> confirm(@PathVariable String token, HttpServletRequest httpRequest) {
        return new ResponseEntity<>(appUserServices.confirmToken(token, httpRequest), HttpStatus.OK);}
    @GetMapping(path = "enableAccount/{email}")
    public ResponseEntity<ResponseDto> enableAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.enableAppUser(email, request), HttpStatus.OK);}
    @GetMapping(path = "disAbleAccountAccount/{email}")
    public ResponseEntity<ResponseDto> disAbleAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.disableAppUser(email, request), HttpStatus.OK);}
    @GetMapping(path = "unlockAccount/{email}")
    public ResponseEntity<ResponseDto> lockAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.unLockAppUser(email, request), HttpStatus.OK);}
    @GetMapping(path = "lockAccount/{email}")
    public ResponseEntity<ResponseDto> unlockAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.lockAppUser(email, request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.setAppUserExpired(email, request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserNonExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserNonExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.setAppUserNonExpired(email, request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserCredentialsExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserCredentialsExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.setAppUserCredentialsExpired(email, request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserCredentialsNonExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserCredentialsNonExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.setAppUserCredentialsNonExpired(email, request), HttpStatus.OK);}
    @GetMapping(path = "addSuperAdminRole/{email}")
    public ResponseEntity<ResponseDto> addSuperAdminRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.addRoleToAppUser(email, Constants.ROLES.SUPER_ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "removeSuperAdminRole/{email}")
    public ResponseEntity<ResponseDto> removeSuperAdminRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.removeRoleFromAppUser(email, Constants.ROLES.SUPER_ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "addAdminRole/{email}")
    public ResponseEntity<ResponseDto> addAdminRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.addRoleToAppUser(email, Constants.ROLES.ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "removeAdminRole/{email}")
    public ResponseEntity<ResponseDto> removeAdminRole (@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.removeRoleFromAppUser(email, Constants.ROLES.ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "addUserRole/{email}")
    public ResponseEntity<ResponseDto> addUserRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.addRoleToAppUser(email, Constants.ROLES.USER, request), HttpStatus.OK);}
    @GetMapping(path = "removeUserRole/{email}")
    public ResponseEntity<ResponseDto> removeUserRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(appUserServices.removeRoleFromAppUser(email, Constants.ROLES.USER, request), HttpStatus.OK);}
    @GetMapping(path = "deleteAccount/{email}")
    public ResponseEntity<ResponseDto> deleteAccount(@PathVariable String email) {
        return new ResponseEntity<>(appUserServices.deleteAppUserAccount(email), HttpStatus.OK);}

    @GetMapping(path = "refreshToken")
    public ResponseEntity<ResponseDto> refreshToken (HttpServletRequest request, HttpServletResponse response) throws IOException {
        return new ResponseEntity<>(appUserServices.refreshAccessToken(request,response), HttpStatus.OK);}

}
