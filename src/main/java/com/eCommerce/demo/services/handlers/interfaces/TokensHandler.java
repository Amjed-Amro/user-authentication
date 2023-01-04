package com.eCommerce.demo.services.handlers.interfaces;

import com.eCommerce.demo.intities.AppUserToken;
import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;

public interface TokensHandler {
    AppUserToken createConfirmationToken(String email, String ip);

    AppUserToken createPasswordResetToken(String email, String ip);

    void saveToken(AppUserToken appUserToken);

    AppUserToken verifyToken(String path, String tokenType);

    Boolean confirmToken(AppUserToken token);

    ResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response);

    String createAccessToken(AppUserDao user, HttpServletRequest request);

    void deleteAppUserTokensByEmail(String email);

    Set<AppUserToken> getAppUserTokensByEmail(String email);

    String getEmailFromAccessHttpRequest(HttpServletRequest request);
}
