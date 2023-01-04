package com.eCommerce.demo.controllers;

import com.eCommerce.demo.models.dto.ResetPasswordDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.interfaces.UsersServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "users")
public class UsersController {


    @Autowired
    private UsersServices usersServices;


    @GetMapping(path = "getUserInfo")
    public ResponseEntity<ResponseDto> getUserInfo (HttpServletRequest request){
        return new ResponseEntity<>(usersServices.getUserInfo(request), OK);}
    @GetMapping(path = "changeFirstName/{firstName}")
    public ResponseEntity<ResponseDto> changeFirstName (@PathVariable String firstName, HttpServletRequest request){
        return new ResponseEntity<>(usersServices.changeFirstName(firstName,request), OK);}
    @GetMapping(path = "changeLastName/{lastName}")
    public ResponseEntity<ResponseDto> changeLastName (@PathVariable String lastName, HttpServletRequest request){
        return new ResponseEntity<>(usersServices.changeLastName(lastName,request), OK);}
    @GetMapping(path = "changeUserName/{userName}")
    public ResponseEntity<ResponseDto> changeUserName (@PathVariable String userName, HttpServletRequest request){
        return new ResponseEntity<>(usersServices.changeUserName(userName,request), OK);}
    @GetMapping(path = "changeGender/{gender}")
    public ResponseEntity<ResponseDto> changeGender (@PathVariable String gender, HttpServletRequest request){
        return new ResponseEntity<>(usersServices.changeGender(gender,request), OK);}
    @GetMapping(path = "changeAge/{age}")
    public ResponseEntity<ResponseDto> changeAge (@PathVariable Integer age, HttpServletRequest request){
        return new ResponseEntity<>(usersServices.changeAge(age,request), OK);}
    @GetMapping(path = "deleteAccount")
    public ResponseEntity<ResponseDto> deleteAccount ( HttpServletRequest request){
        return new ResponseEntity<>(usersServices.deleteAccount(request), OK);}
    @PostMapping(path = "changePassword")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody ResetPasswordDto dto, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changePassword(dto.getPassword(), dto.getConfirmPassword(), request), OK);}




}
