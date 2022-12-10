package com.eCommerce.demo.controllers;


import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.AppUserServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping (path = "users")
public class UsersController {

    @Autowired
    private AppUserServices appUserServices;


    @PostMapping(path = "registration")
    public ResponseEntity<ResponseDto> registration(@RequestBody RegistrationDto registrationDto, HttpServletRequest httpRequest){
        registrationDto.setPort(httpRequest.getRemotePort());
        registrationDto.setIpAddress(httpRequest.getRemoteAddr());
        return new ResponseEntity<>( appUserServices.saveNewAppUser(registrationDto), HttpStatus.OK);
    }
    @GetMapping (path = "getAllUsers")
    public ResponseEntity<ResponseDto> getAllUsers (){
        return new ResponseEntity<>(appUserServices.loadAllAppUsers(),HttpStatus.OK);
    }
    @GetMapping (path = "confirmToken/{token}")
    public ResponseEntity<ResponseDto> confirm (@PathVariable String token, HttpServletRequest httpRequest){
        return new ResponseEntity<>(appUserServices.confirmToken(token,httpRequest),HttpStatus.OK);
    }
    @PostMapping (path = "enableAccount")
    public ResponseEntity<ResponseDto> enableAccount (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.setAppUserEnabledStatus(email,Boolean.TRUE,request),HttpStatus.OK);
    }
    @PostMapping (path = "disAbleAccountAccount")
    public ResponseEntity<ResponseDto> disAbleAccount (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.setAppUserEnabledStatus(email,Boolean.FALSE,request),HttpStatus.OK);
    }
    @PostMapping (path = "unlockAccount")
    public ResponseEntity<ResponseDto> lockAccount (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.setAppUserLockedStatus(email,Boolean.TRUE,request),HttpStatus.OK);    }
    @PostMapping (path = "lockAccount")
    public ResponseEntity<ResponseDto> unlockAccount (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.setAppUserLockedStatus(email,Boolean.FALSE,request),HttpStatus.OK);    }
    @PostMapping (path = "changePassword")
    public ResponseEntity<ResponseDto> changePassword (@RequestBody String email,String password,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.changePassword(email,password,request),HttpStatus.OK);
    }
    @PostMapping (path = "setAdmin")
    public ResponseEntity<ResponseDto> setAdmin (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.addRoleToAppUser(email, Constants.ROLES.ADMIN,request),HttpStatus.OK);
    }
    @PostMapping (path = "setUser")
    public ResponseEntity<ResponseDto> setUser (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.addRoleToAppUser(email, Constants.ROLES.USER,request),HttpStatus.OK);
    }
    @PostMapping (path = "setSuperAdmin")
    public ResponseEntity<ResponseDto> setSuperAdmin (@RequestBody String email,HttpServletRequest request){
        return new ResponseEntity<>(appUserServices.addRoleToAppUser(email, Constants.ROLES.SUPER_ADMIN,request),HttpStatus.OK);
    }
    @PostMapping (path = "deleteAccount")
    public ResponseEntity<ResponseDto> deleteAccount (@RequestBody String email){
        return new ResponseEntity<>(appUserServices.deleteAppUserAccount(email),HttpStatus.OK);
    }

}
