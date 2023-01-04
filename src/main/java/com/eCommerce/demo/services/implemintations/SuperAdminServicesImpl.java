package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.handlers.interfaces.AppUsersHandler;
import com.eCommerce.demo.services.handlers.interfaces.LoginIpsHandler;
import com.eCommerce.demo.services.handlers.interfaces.TokensHandler;
import com.eCommerce.demo.services.interfaces.SuperAdminServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.eCommerce.demo.constants.Constants.REQUEST_SUCCESS;
import static com.eCommerce.demo.constants.Constants.UPDATE_HISTORY.*;
import static com.eCommerce.demo.constants.Messages.*;

@Service
@Log4j2
public class SuperAdminServicesImpl implements SuperAdminServices {
    @Autowired
    private AppUsersHandler appUsersHandler;
    @Autowired
    private TokensHandler tokensHandler;
    @Autowired
    private LoginIpsHandler loginIpsHandler;
    @Override
    public ResponseDto changeFirstNameOfUser(String email,String firstName, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.changeFirstName(email,firstName);
            appUsersHandler.addToUpdateHistory(email,CHANGE_FIRST_NAME,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_FIRST_NAME);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeLastNameOfUser(String email,String lastName, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.changeLastName(email,lastName);
            appUsersHandler.addToUpdateHistory(email,CHANGE_LAST_NAME,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_LAST_NAME);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeUserNameOfUser(String email,String userName, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.changeUserName(email,userName);
            appUsersHandler.addToUpdateHistory(email,CHANGE_USER_NAME,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_USER_NAME);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeGenderOfUser(String email,String gender, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.changeGender(email,gender);
            appUsersHandler.addToUpdateHistory(email,CHANGE_GENDER,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_GENDER);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changeAgeOfUser(String email,Integer age, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.changeAge(email,age);
            appUsersHandler.addToUpdateHistory(email,CHANGE_AGE,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_AGE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changePasswordOfUser(String email,String password, String confirmPassword, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.changeAppUserPassword(email,password,confirmPassword);
            appUsersHandler.addToUpdateHistory(email, CHANGE_PASSWORD,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_CHANGE_PASSWORD);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto removeAppUser(String email){
        try {
            appUsersHandler.deleteAppUserAccount(email);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_DELETE_APP_USER_FROM_DATABASE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }

    @Override
    public ResponseDto activateAppUser(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.activateAppUser(email);
            appUsersHandler.addToUpdateHistory(email,ACTIVATE,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_ACTIVATE_ACCOUNT);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto deactivateAppUser(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.deactivateAppUser(email);
            appUsersHandler.addToUpdateHistory(email,DEACTIVATE_USER,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_DEACTIVATE_ACCOUNT);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto unlockAppUser(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.unLockAppUser(email);
            appUsersHandler.addToUpdateHistory(email,UNLOCK_USER,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_UNLOCK_ACCOUNT);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto lockAppUser(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.lockAppUser(email);
            appUsersHandler.addToUpdateHistory(email,LOCK_USER,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_LOCK_ACCOUNT);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto setAppUserExpired(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.setAppUserExpired(email);
            appUsersHandler.addToUpdateHistory(email,SET_ACCOUNT_EXPIRED,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_SET_ACCOUNT_EXPIRED);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }

    @Override
    public ResponseDto setAppUserNonExpired(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.setAppUserNonExpired(email);
            appUsersHandler.addToUpdateHistory(email,SET_ACCOUNT_NON_EXPIRED,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_SET_ACCOUNT_NON_EXPIRED);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }

    @Override
    public ResponseDto setAppUserCredentialsNonExpired(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.setAppUserCredentialsNonExpired(email);
            appUsersHandler.addToUpdateHistory(email,SET_CREDENTIALS_NON_EXPIRED,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_SET_CREDENTIALS_NON_EXPIRED);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto setAppUserCredentialsExpired(String email, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.setAppUserCredentialsExpired(email);
            appUsersHandler.addToUpdateHistory(email,SET_CREDENTIALS_EXPIRED,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_SET_CREDENTIAL_EXPIRED);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto addRoleToAppUser(String email,String role, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.addRoleToAppUser(email,role);
            appUsersHandler.addToUpdateHistory(email,ADD_ROLE,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_ADD_ROLE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto removeRoleFromAppUser(String email,String role, HttpServletRequest request){
        String editorEmail = tokensHandler.getEmailFromAccessHttpRequest(request);
        try {
            appUsersHandler.removeRoleFromAppUser(email,role);
            appUsersHandler.addToUpdateHistory(email,REMOVE_ROLE,request.getRemoteAddr(),editorEmail);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(FAILED_TO_REMOVE_ROLE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto getAppUserInfoByEmail(String email){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, appUsersHandler.loadUserByUsername(email));
        }catch (Exception exception){
            log.error(FAILED_TO_GET_APP_USER_INFO);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto getAllAppUsers(){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, appUsersHandler.loadAllAppUsers());
        }catch (Exception exception){
            log.error(FAILED_TO_GET_ALL_APP_USERS);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto getAppUserTokens(String email){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, tokensHandler.getAppUserTokensByEmail(email));
        }catch (Exception exception){
            log.error(TO_GET_APP_USER_TOKENS);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto getAppUserUpdateHistory(String email){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, appUsersHandler.getUserUpdateHistory(email));
        }catch (Exception exception){
            log.error(FAILED_TO_GET_APP_USER_UPDATE_HISTORY);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto getAppUserRoles(String email){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, appUsersHandler.getAppUserRoles(email));
        }catch (Exception exception){
            log.error(FAILED_TO_GET_APP_USER_ROLES);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto getAppUserLoginIps(String email){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, loginIpsHandler.findAllLoginIpsByEmail(email));
        }catch (Exception exception){
            log.error(FAILED_TO_GET_APP_USER_LOGIN_IP);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }

    @Override
    public ResponseDto getAllAppUsersByRole(String role){
        try {
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, appUsersHandler.loadAllAppUsersByRole(role));
        }catch (Exception exception){
            log.error(FAILED_TO_GET_ALL_APP_USER_BY_ROLE);
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    












    
}
