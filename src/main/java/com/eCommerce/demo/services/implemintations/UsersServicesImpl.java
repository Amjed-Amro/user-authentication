package com.eCommerce.demo.services.implemintations;


import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.handlers.interfaces.AppUsersHandler;
import com.eCommerce.demo.services.handlers.interfaces.TokensHandler;
import com.eCommerce.demo.services.interfaces.UsersServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.eCommerce.demo.constants.Constants.REQUEST_SUCCESS;
import static com.eCommerce.demo.constants.Constants.UPDATE_HISTORY.*;
import static com.eCommerce.demo.constants.Messages.*;

@Service
@Log4j2

public class UsersServicesImpl implements UsersServices {
    @Autowired
    private AppUsersHandler appUsersHandler;
    @Autowired
    private TokensHandler tokensHandler;

    @Override
    public ResponseDto getUserInfo(HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, appUsersHandler.getUserInfo(email));
        }catch (Exception exception){
            log.error(FAILED_TO_GET_APP_USER_INFO);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeFirstName(String firstName, HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeFirstName(email,firstName);
            appUsersHandler.addToUpdateHistory(email,CHANGE_FIRST_NAME,request.getRemoteAddr(),email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_FIRST_NAME);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeLastName(String lastName, HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeFirstName(email,lastName);
            appUsersHandler.addToUpdateHistory(email,CHANGE_LAST_NAME,request.getRemoteAddr(),email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_LAST_NAME);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeUserName(String userName, HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeUserName(email,userName);
            appUsersHandler.addToUpdateHistory(email,CHANGE_USER_NAME,request.getRemoteAddr(),email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_USER_NAME);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeGender(String gender, HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeGender(email,gender);
            appUsersHandler.addToUpdateHistory(email,CHANGE_GENDER,request.getRemoteAddr(),email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_GENDER);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeAge(Integer age, HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeAge(email,age);
            appUsersHandler.addToUpdateHistory(email,CHANGE_AGE,request.getRemoteAddr(),email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_AGE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changePassword(String password, String confirmPassword, HttpServletRequest request){
        try {
            String email = tokensHandler.getEmailFromAccessHttpRequest(request);
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeAppUserPassword(email, password, confirmPassword);
            appUsersHandler.addToUpdateHistory(email, CHANGE_PASSWORD,request.getRemoteAddr(),email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_PASSWORD);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto deleteAccount(HttpServletRequest request){
        try {
            String email =tokensHandler.getEmailFromAccessHttpRequest(request);
            tokensHandler.deleteAppUserTokensByEmail(email);
            appUsersHandler.deleteAppUserAccount(email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_DELETE_APP_USER_FROM_DATABASE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
}
