package com.eCommerce.demo.controllers.appuser;

import com.eCommerce.demo.models.dto.ResetPasswordDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.interfaces.UsersServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "v1/auth/users")
public class UsersController {


    @Autowired
    private UsersServices usersServices;

    @DeleteMapping(path = "deleteAccount")
    public ResponseEntity<ResponseDto> deleteAccount(HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.deleteAccount(request), OK);
    }

    @GetMapping(path = "getUserInfo")
    public ResponseEntity<ResponseDto> getUserInfo(HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.getUserInfo(request), OK);
    }

    @PutMapping(path = "changeFirstName/{firstName}")
    public ResponseEntity<ResponseDto> changeFirstName(@PathVariable String firstName, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changeFirstName(firstName, request), OK);
    }

    @PutMapping(path = "changeLastName/{lastName}")
    public ResponseEntity<ResponseDto> changeLastName(@PathVariable String lastName, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changeLastName(lastName, request), OK);
    }

    @PutMapping(path = "changeUserName/{userName}")
    public ResponseEntity<ResponseDto> changeUserName(@PathVariable String userName, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changeUserName(userName, request), OK);
    }

    @PutMapping(path = "changeGender/{gender}")
    public ResponseEntity<ResponseDto> changeGender(@PathVariable String gender, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changeGender(gender, request), OK);
    }

    @PutMapping(path = "changeAge/{age}")
    public ResponseEntity<ResponseDto> changeAge(@PathVariable Integer age, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changeAge(age, request), OK);
    }

    @PutMapping(path = "changePassword")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody ResetPasswordDto dto, HttpServletRequest request) {
        return new ResponseEntity<>(usersServices.changePassword(dto.getPassword(), dto.getConfirmPassword(), request), OK);
    }


}
