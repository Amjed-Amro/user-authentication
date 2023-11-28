package com.eCommerce.demo.services.handlers.implementations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.eCommerce.demo.intities.AppUser;
import com.eCommerce.demo.intities.AppUserRoles;
import com.eCommerce.demo.intities.AppUserToken;
import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.repository.AppUserRepository;
import com.eCommerce.demo.repository.TokensRepository;
import com.eCommerce.demo.services.handlers.interfaces.TokensHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.eCommerce.demo.constants.Constants.RESPONSE_CODE;
import static com.eCommerce.demo.constants.Constants.RESPONSE_MESSAGE;
import static com.eCommerce.demo.constants.Constants.TOKENS.*;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@Log4j2
public class TokensHandlerImpl implements TokensHandler {
    @Autowired
    private Algorithm algorithm;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private TokensRepository tokensRepository;


    @Override
    public AppUserToken createConfirmationToken(String email, String ip) {
        AppUserToken confirmationToken = AppUserToken.builder()
                .tokenType(CONFIRMATION_TOKEN)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(CONFIRM_TOKEN_VALIDITY_MINUTES))
                .tokenPath(UUID.randomUUID().toString().toLowerCase())
                .userEmail(email)
                .creatorIp(ip)
                .build();
        tokensRepository.save(confirmationToken);
        log.info("confirmation token was created");

        return confirmationToken;
    }

    @Override
    public AppUserToken createPasswordResetToken(String email, String ip) {
        AppUserToken passwordResetToken = AppUserToken.builder()
                .tokenType(PASSWORD_RESET_TOKEN)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(RESET_PASSWORD_TOKEN_VALIDITY_MINUTES))
                .tokenPath(UUID.randomUUID().toString().toLowerCase())
                .userEmail(email)
                .creatorIp(ip)
                .build();
        tokensRepository.save(passwordResetToken);
        log.info("password reset token was created");
        return passwordResetToken;
    }

    @Override
    public void saveToken(AppUserToken appUserToken) {
        tokensRepository.save(appUserToken);
        log.info("AppUserToken was saved to database");
    }

    @Override
    public AppUserToken verifyToken(String path, String tokenType) {
        AppUserToken confirmationToken = tokensRepository.findAllByTokenPath(path.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("token not found"));
        if (!confirmationToken.getTokenType().equals(tokenType)) {
            throw new IllegalStateException("token not valid");
        }
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("token already confirmed");
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }
        return confirmationToken;
    }

    @Override
    public Boolean confirmToken(AppUserToken token) {
        token.setConfirmedAt(LocalDateTime.now());
        tokensRepository.save(token);
        log.info("token is now confirmed");
        return TRUE;
    }

    @Override
    public ResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(ACCESS_TOKEN_START_PHRASE)) {
                String expiredAccessToken = authorizationHeader.substring(ACCESS_TOKEN_START_PHRASE.length());
                AppUserToken refreshToken = tokensRepository.findByToken(expiredAccessToken)
                        .orElseThrow(() -> new IllegalArgumentException("token not found"));
                verifyToken(refreshToken.getTokenPath(), REFRESH_TOKEN);
                AppUser appUser = appUserRepository.findAppUserByEmail(refreshToken.getUserEmail()).get();
                String accessToken = JWT.create()
                        .withSubject(appUser.getUserName())
                        .withExpiresAt(new Date((System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY_MIN * 60 * 1000))))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim(TOKEN_CLAIM_ROLES, appUser.getAppUserRole().stream().map(AppUserRoles::getRole).collect(Collectors.toList()))
                        .sign(algorithm);
                refreshToken.setToken(accessToken);
                tokensRepository.save(refreshToken);
                return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, accessToken);
            } else {
                throw new IllegalStateException("refresh token is messing");
            }
        } catch (Exception exception) {
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, String.format("error_message: %s", exception.getMessage()));
        }
    }

    @Override
    public String createAccessToken(AppUserDao user, HttpServletRequest request) {
        AppUserToken refreshToken = AppUserToken.builder()
                .tokenType(REFRESH_TOKEN)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(REFRESH_TOKEN_VALIDITY_MIN))
                .tokenPath(UUID.randomUUID().toString().toLowerCase())
                .creatorIp(request.getRemoteAddr())
                .userEmail(user.getUsername())
                .build();
        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date((System.currentTimeMillis() + (ACCESS_TOKEN_VALIDITY_MIN * 60 * 1000))))
                .withIssuer(request.getRemoteAddr())
                .withClaim(TOKEN_CLAIM_ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim(TOKEN_CLAIM_PATH, refreshToken.getTokenPath())
                .sign(algorithm);
        refreshToken.setToken(accessToken);
        log.info("access token was created");
        tokensRepository.save(refreshToken);
        return accessToken;
    }

    @Override
    public void deleteAppUserTokensByEmail(String email) {
        tokensRepository.deleteAllByUserEmail(email);
        log.info(String.format("AppUserToken token was deleted from database for user with email %s", email));
    }

    @Override
    public Set<AppUserToken> getAppUserTokensByEmail(String email) {
        return tokensRepository.findAllByUserEmail(email);
    }

    @Override
    public String getEmailFromAccessHttpRequest(HttpServletRequest request) {
        return tokensRepository.findByToken(request.getHeader(AUTHORIZATION).substring(ACCESS_TOKEN_START_PHRASE.length())).get().getUserEmail();
    }

}
