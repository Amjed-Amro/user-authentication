package com.eCommerce.demo.controllers;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.lang.Boolean.*;

@Controller
@RequestMapping(path = "auth")
public class AppUserAuthenticationController {

    @GetMapping(path = "user")
    public ResponseEntity<Boolean> authenticateUser() {
        return new ResponseEntity<>(TRUE,HttpStatus.OK);
    }
    @GetMapping(path = "admin")
    public ResponseEntity<Boolean> authenticateAdmin() {
        return new ResponseEntity<>(TRUE,HttpStatus.OK);
    }
    @GetMapping(path = "super")
    public ResponseEntity<Boolean> authenticateSuperAdmin() {
        return new ResponseEntity<>(TRUE,HttpStatus.OK);
    }
}
