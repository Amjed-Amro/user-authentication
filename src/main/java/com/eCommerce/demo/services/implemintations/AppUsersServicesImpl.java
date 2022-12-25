package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.constants.Constants.RESPONSE_CODE;
import com.eCommerce.demo.intities.AppUserToken;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.handlers.interfaces.AppUsersHandler;
import com.eCommerce.demo.services.handlers.interfaces.EmailsHandler;
import com.eCommerce.demo.services.handlers.interfaces.LoginIpsHandler;
import com.eCommerce.demo.services.handlers.interfaces.TokensHandler;
import com.eCommerce.demo.services.interfaces.AppUsersServices;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.eCommerce.demo.constants.Constants.REQUEST_SUCCESS;
import static com.eCommerce.demo.constants.Constants.RESPONSE_MESSAGE.FAILED;
import static com.eCommerce.demo.constants.Constants.RESPONSE_MESSAGE.SUCCESS;
import static com.eCommerce.demo.constants.Constants.TOKENS.CONFIRMATION_TOKEN;
import static com.eCommerce.demo.constants.Constants.TOKENS.PASSWORD_RESET_TOKEN;
import static com.eCommerce.demo.constants.Constants.UPDATE_HISTORY.*;
import static com.eCommerce.demo.constants.Messages.*;

@Service
@Log4j2
public class AppUsersServicesImpl implements AppUsersServices {

    @Autowired
    private EmailsHandler emailsHandler;
    @Autowired
    private AppUsersHandler appUsersHandler;
    @Autowired
    private TokensHandler tokensHandler;
    @Autowired
    private LoginIpsHandler loginIpsHandler;


    @Override
    public ResponseDto registration(RegistrationDto registrationDto, HttpServletRequest request){
        String email = registrationDto.getEMAIL();
        try {
            appUsersHandler.saveNewAppUser(registrationDto);
            appUsersHandler.addToUpdateHistory(email, USER_CREATED,request.getRemoteAddr(),email);
            AppUserToken token = tokensHandler.createConfirmationToken(email, request.getRemoteAddr());
            tokensHandler.saveToken(token);
            appUsersHandler.addToUpdateHistory(email,ADD_TOKEN, request.getRemoteAddr(), email);
            emailsHandler.sender(email, emailsHandler.buildConfirmationEmail(registrationDto.getFIRST_NAME()
                    , String.format(Constants.TOKENS.CONFIRM_TOKEN_CONFIRMATION_URL, token.getTokenPath())),"Confirm your email");
            loginIpsHandler.addNewIpToEmail(request.getRemoteAddr(),registrationDto.getEMAIL());
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, "confirmation email was sent to your email");
        }catch (Exception exception) {
            log.error(String.format(REGISTRATION_FAILED_FOR_EMAIL_S, email));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, FAILED, exception.getMessage());        }
    }
    @Override
    public ResponseDto activateAccount(String path, HttpServletRequest request){
        try {
            AppUserToken confirmationToken = tokensHandler.verifyToken(path,CONFIRMATION_TOKEN);
            appUsersHandler.activateAppUser(confirmationToken.getUserEmail());
            appUsersHandler.addToUpdateHistory(confirmationToken.getUserEmail(),ACTIVATE,request.getRemoteAddr(),confirmationToken.getUserEmail());
            tokensHandler.confirmToken(confirmationToken);
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, "account activated");
        }catch (Exception exception){
            log.error(FAILED_TO_ACTIVATE_ACCOUNT);
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, FAILED, exception.getMessage());
        }
    }

    @Override
    public ResponseDto requestPasswordReset(String email, HttpServletRequest request){
        try {
            appUsersHandler.isActivatedAppUser(email);
            AppUserToken resetToken = tokensHandler.createPasswordResetToken(email,request.getRemoteAddr());
            tokensHandler.saveToken(resetToken);
            appUsersHandler.addToUpdateHistory(email,ADD_TOKEN, request.getRemoteAddr(),email);
            emailsHandler.sender(email, emailsHandler.buildPasswordResetEmail(appUsersHandler.getFirstName(email)
                    , String.format(Constants.TOKENS.RESET_PASSWORD_TOKEN_URL, resetToken.getTokenPath())), "Reset password");
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, "an email was sent to you");
        }catch (Exception exception){
            log.error(String.format(REQUEST_PASSWORD_RESET_FAILED_FOR_EMAIL_S, email));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }
    @Override
    public ResponseDto resetPassword(String path, String password, String confirmPassword, HttpServletRequest request) {
        try {
            AppUserToken passwordResetToken = tokensHandler.verifyToken(path,PASSWORD_RESET_TOKEN);
            String email = passwordResetToken.getUserEmail();
            appUsersHandler.isActivatedAppUser(email);
            appUsersHandler.changeAppUserPassword(email, password, confirmPassword);
            appUsersHandler.addToUpdateHistory(email,RESET_PASSWORD,request.getRemoteAddr(),email);
            tokensHandler.confirmToken(passwordResetToken);
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, REQUEST_SUCCESS);
        }catch (Exception exception){
            log.error(String.format(PASSWORD_RESET_FAILED_FOR_TOKEN, path));
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }

    @Override
    public ResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        try {
            return new ResponseDto(RESPONSE_CODE.SUCCESS, SUCCESS, tokensHandler.refreshAccessToken(request,response));
        }catch (Exception exception){
            log.error(FAILED_TO_REFRESH_ACCESS_TOKEN);
            log.error(exception.getMessage());
            return new ResponseDto(RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }








}
