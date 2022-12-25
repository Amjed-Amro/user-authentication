package com.eCommerce.demo.services.interfaces;

import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UsersServices {
    ResponseDto getUserInfo(HttpServletRequest request);

    ResponseDto changeFirstName(String firstName, HttpServletRequest request);

    ResponseDto changeLastName(String lastName, HttpServletRequest request);

    ResponseDto changeUserName(String userName, HttpServletRequest request);

    ResponseDto changeGender(String gender, HttpServletRequest request);

    ResponseDto changeAge(Integer age, HttpServletRequest request);

    ResponseDto changePassword(String password, String confirmPassword, HttpServletRequest request);

    ResponseDto deleteAccount(HttpServletRequest request);
}
