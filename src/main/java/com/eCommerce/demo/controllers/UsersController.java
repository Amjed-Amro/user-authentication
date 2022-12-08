package com.eCommerce.demo.controllers;


import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.AppUserServices;
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
    public ResponseEntity<ResponseDto> registration(@RequestBody RegistrationDto registrationDto){
        return new ResponseEntity<>( appUserServices.saveNewAppUser(registrationDto), HttpStatus.OK);
    }
    @GetMapping (path = "getAllUsers")
    public ResponseEntity<ResponseDto> getAllUsers (){
        return new ResponseEntity<>(appUserServices.loadAllUsers(),HttpStatus.OK);
    }
    @GetMapping (path = "confirmToken/{token}")
    public ResponseEntity<ResponseDto> confirm (@PathVariable String token){
        return new ResponseEntity<>(appUserServices.confirmToken(token),HttpStatus.OK);
    }
    @GetMapping (path = "good")
    public ResponseEntity<String> good (){
        return new ResponseEntity<>("good",HttpStatus.OK);
    }
}
