package com.eCommerce.demo.services.implemintations;


import com.eCommerce.demo.intities.AppUser.AppUserToken;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.handlers.interfaces.LoginIpsHandler;
import com.eCommerce.demo.services.interfaces.AppUserBusiness;
import com.eCommerce.demo.services.handlers.interfaces.AppUserHandler;
import com.eCommerce.demo.services.handlers.interfaces.EmailSenderHandler;
import com.eCommerce.demo.services.handlers.interfaces.TokensHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.eCommerce.demo.constants.Constants.*;
import static com.eCommerce.demo.constants.Constants.RESPONSE_MESSAGE.*;
import static com.eCommerce.demo.constants.Constants.TOKENS.CONFIRMATION_TOKEN;
import static com.eCommerce.demo.constants.Constants.TOKENS.PASSWORD_RESET_TOKEN;
import static com.eCommerce.demo.constants.Constants.UPDATE_HISTORY.*;
import static com.eCommerce.demo.constants.Messages.LogsMessages.*;
import static com.eCommerce.demo.constants.Messages.LogsMessages.REGISTRATION_FAILED;
import static com.eCommerce.demo.constants.Messages.ResponseMessages.*;

@Log4j2
@Service
public class AppUserBusinessImpl implements AppUserBusiness {
    @Autowired
    private EmailSenderHandler emailSenderHandler;
    @Autowired
    private AppUserHandler appUserHandler;
    @Autowired
    private TokensHandler tokensHandler;
    @Autowired
    private LoginIpsHandler loginIpsHandler;


    @Override
    public ResponseDto registration(RegistrationDto registrationDto, HttpServletRequest request){
        String email = registrationDto.getEMAIL();
        try {
            appUserHandler.saveNewAppUser(registrationDto);
            appUserHandler.addToUpdateHistory(email, USER_CREATED, SUCCESS_STATUS,request.getRemoteAddr());
            AppUserToken token = tokensHandler.createConfirmationToken(email, request.getRemoteAddr());
            tokensHandler.saveToken(token);
            appUserHandler.addToUpdateHistory(email,ADD_TOKEN, SUCCESS_STATUS,request.getRemoteAddr());
            emailSenderHandler.sender(email, emailSenderHandler.buildConfirmationEmail(registrationDto.getFIRST_NAME()
                    , String.format(TOKENS.CONFIRM_TOKEN_CONFIRMATION_URL, token.getPath())),"Confirm your email");
            loginIpsHandler.addNewIpToEmail(request.getRemoteAddr(),registrationDto.getEMAIL());
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, EMAIL_WAS_SENT);
        }catch (Exception exception) {
            log.error(String.format(REGISTRATION_FAILED,email));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto activateAccount(String path, HttpServletRequest request){
        try {
            AppUserToken confirmationToken = tokensHandler.verifyToken(path,CONFIRMATION_TOKEN);
            appUserHandler.enableAppUSER(confirmationToken);
            appUserHandler.addToUpdateHistory(confirmationToken.getAppUserEmail(),ENABLE,SUCCESS_STATUS,request.getRemoteAddr());
            tokensHandler.confirmToken(confirmationToken);
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, ACCOUNT_ACTIVATED);
        }catch (Exception exception){
            log.error(TOKEN_CONFIRM_FAILED);
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto changePassword(String password, String confirmPassword, HttpServletRequest request){
        String email = tokensHandler.getEmailFromAccessToken(request);
        try {
            appUserHandler.isEnabledAppUser(email);
            appUserHandler.changeAppUserPassword(email, password, confirmPassword);
            appUserHandler.addToUpdateHistory(email,CHANGE_PASSWORD,SUCCESS_STATUS,request.getRemoteAddr());
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(String.format(PASSWORD_CHANGE_FAILED_FOR_USER, email));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto requestPasswordReset(String email, HttpServletRequest request){
        try {
            appUserHandler.isEnabledAppUser(email);
            AppUserToken resetToken = tokensHandler.createPasswordResetToken(email,request.getRemoteAddr());
            tokensHandler.saveToken(resetToken);
            appUserHandler.addToUpdateHistory(email,ADD_TOKEN, SUCCESS_STATUS,request.getRemoteAddr());
            emailSenderHandler.sender(email, emailSenderHandler.buildPasswordResetEmail(appUserHandler.getFirstName(email)
                    , String.format(TOKENS.RESET_PASSWORD_TOKEN_URL, resetToken.getPath())), "Reset password");
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, EMAIL_WAS_SENT);
        }catch (Exception exception){
            log.error(String.format(PASSWORD_CHANGE_FAILED_FOR_USER, email));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto resetPassword (String path, String password, String confirmPassword, HttpServletRequest request) {
        try {
            AppUserToken passwordResetToken = tokensHandler.verifyToken(path,PASSWORD_RESET_TOKEN);
            String email = passwordResetToken.getAppUserEmail();
            appUserHandler.isEnabledAppUser(email);
            appUserHandler.changeAppUserPassword(email, password, confirmPassword);
            appUserHandler.addToUpdateHistory(email,CHANGE_PASSWORD,SUCCESS_STATUS,request.getRemoteAddr());
            tokensHandler.confirmToken(passwordResetToken);
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(String.format(PASSWORD_RESET_FAILED_FOR_TOKEN, path));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto deleteAppUserAccount(HttpServletRequest request){
        try {
            String email =tokensHandler.getEmailFromAccessToken(request);
            tokensHandler.deleteAppUserTokensByEmail(email);
            appUserHandler.deleteAppUserAccount(email);
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        try {
            log.info(ACCESS_TOKEN_WAS_REFRESHED);
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, tokensHandler.refreshAccessToken(request,response));
        }catch (Exception exception){
            log.error(FAILED_TO_REFRESH_ACCESS_TOKEN);
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }







}
