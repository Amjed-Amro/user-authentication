package com.eCommerce.demo.services.handlers.implementations;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.intities.AppUser.AppUser;
import com.eCommerce.demo.intities.AppUser.AppUserRoles;
import com.eCommerce.demo.intities.AppUser.AppUserToken;
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

import java.util.*;
import java.util.stream.Collectors;

import static com.eCommerce.demo.constants.Constants.TOKENS.*;
import static com.eCommerce.demo.constants.ErrorConstants.*;
import static com.eCommerce.demo.constants.Messages.LogsMessages.NEW_CONFIRMATION_TOKEN_WAS_CREATED;
import static com.eCommerce.demo.constants.Messages.LogsMessages.NEW_PASSWORD_RESET_TOKEN_WAS_CREATED;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Service
@Log4j2
public class AppUserTokensHandlerHandlerImpl implements TokensHandler {
    @Autowired
    private Algorithm algorithm;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUserToken createConfirmationToken(String email, String ip) {
        AppUserToken confirmationToken = AppUserToken.builder()
                .tokenType(CONFIRMATION_TOKEN)
                .createdAt(new Date())
                .expiresAt(new Date((System.currentTimeMillis() + CONFIRM_TOKEN_VALIDITY_MINUTES)))
                .path(UUID.randomUUID().toString().toLowerCase())
                .appUserEmail(email)
                .build();
        log.info(NEW_CONFIRMATION_TOKEN_WAS_CREATED);
        return confirmationToken;
    }
    @Override
    public AppUserToken createPasswordResetToken(String email, String ip){
        AppUserToken passwordResetToken= AppUserToken.builder()
                .tokenType(PASSWORD_RESET_TOKEN)
                .createdAt(new Date())
                .expiresAt(new Date((System.currentTimeMillis() + RESET_PASSWORD_TOKEN_VALIDITY_MINUTES)))
                .path(UUID.randomUUID().toString().toLowerCase())
                .appUserEmail(email)
                .build();
        log.info(NEW_PASSWORD_RESET_TOKEN_WAS_CREATED);
        return passwordResetToken;
    }
    @Override
    public void saveToken(AppUserToken appUserToken){tokensRepository.save(appUserToken);}
    /**
     * this method is to confirm the token confirmation
     * @param path is the path to the token to be confirmed
     * @return true if the token was confirmed
     * or throw IllegalStateException if the token was already confirmed, expired or not found.
     */
    @Override
    public AppUserToken verifyToken(String path, String tokenType) {
        AppUserToken confirmationToken =tokensRepository.findAllByPath(path.toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException(TOKEN_NOT_FOUND));
        if (!confirmationToken.getTokenType().equals(tokenType)) {
            throw new IllegalStateException(TOKEN_NOT_VALID);
        }
        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException(TOKEN_ALREADY_CONFIRMED);
        }
        if (confirmationToken.getExpiresAt().before(new Date())) {
            throw new IllegalStateException(TOKEN_EXPIRED);
        }
        return confirmationToken;
    }
    @Override
    public Boolean confirmToken(AppUserToken token) {
        token.setConfirmedAt(new Date());
        tokensRepository.save(token);
        return TRUE;
    }
    /**
     * this method is used to create new access token using refresh token
     * @param request is the Http request that contains the refresh token
     * @param response
     * @return
     */
    @Override
    public ResponseDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response){
        try{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader!= null && authorizationHeader.startsWith("Bearer ")){
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser appUser = appUserRepository.findAppUserByEmail(username).get();
                String accessToken = JWT.create()
                        .withSubject(appUser.getUserName())
                        .withExpiresAt(new Date((System.currentTimeMillis()+ Constants.TOKENS.ACCESS_TOKEN_VALIDITY_MILLI)))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",appUser.getAppUserRole().stream().map(AppUserRoles::getRole).collect(Collectors.toList()))
                        .sign(algorithm);
                return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, accessToken);
            }else{
                throw new IllegalStateException ("refresh token is messing");
            }
        }catch (Exception exception){
            log.error(exception.getMessage());
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED,String.format("error_message: %s",exception.getMessage()));
        }
    }
    @Override
    public String createAccessToken(AppUserDao user, HttpServletRequest request){
        AppUserToken refreshToken= AppUserToken.builder()
                .tokenType(REFRESH_TOKEN)
                .createdAt(new Date())
                .expiresAt(new Date((System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_MILLI)))
                .path(UUID.randomUUID().toString().toLowerCase())
                .appUserEmail(user.getUsername())
                .token(JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date((System.currentTimeMillis() + Constants.TOKENS.REFRESH_TOKEN_VALIDITY_MILLI)))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(algorithm))
                        .build();
        return  JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date((System.currentTimeMillis() + Constants.TOKENS.ACCESS_TOKEN_VALIDITY_MILLI)))
                .withIssuer(request.getRemoteAddr())
                .withClaim(TOKEN_CLAIM_ROLES, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withClaim(TOKEN_CLAIM_PATH, refreshToken.getPath())
                .sign(algorithm);
    }

    @Override
    public void deleteAppUserTokensByEmail(String email){tokensRepository.deleteAllByAppUserEmail(email);}
    @Override
    public Set<AppUserToken> getAppUserTokensByEmail(String email){
        return tokensRepository.findAllByAppUserEmail(email);
    }

}
