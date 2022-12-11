package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.intities.AppUser.AppUser;
import com.eCommerce.demo.intities.AppUser.AppUserRoles;
import com.eCommerce.demo.intities.AppUser.AppUserUpdateHistory;
import com.eCommerce.demo.intities.ConfirmationToken;
import com.eCommerce.demo.intities.AppUser.OldPasswords;
import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.repository.AppUserRepository;
import com.eCommerce.demo.repository.ConfirmationTokenRepository;
import com.eCommerce.demo.services.AppUserServices;
import com.eCommerce.demo.services.EmailSender;
import com.eCommerce.demo.utils.EmailValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.eCommerce.demo.constants.Constants.*;
import static java.lang.Boolean.*;


@Log4j2
@Service
public class AppUserServicesImpl implements AppUserServices {
    public static final Integer TOKEN_VALIDITY_MINUTES = 15;
    public static final String SUCCESS = "success";
    public static final String TOKEN_CONFIRMATION_URL = "http://localhost:8080/users/confirmToken/%S";
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private EmailSender emailSender;

    @Override
    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    @Override
    public ConfirmationToken createNewToken(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now()
                , LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES), appUser);
        saveConfirmationToken(confirmationToken);
        return confirmationToken;
    }

    /**
     * this method is to confirm the token confirmation
     * and enable the confirmed AppUser
     *
     * @param token is the token to be confirmed
     * @return ResponseDto to the controller if the token was confirmed
     * or throw IllegalStateException if the token was already confirmed or
     * it was expired.
     */
    @Override
    public ResponseDto confirmToken(String token, HttpServletRequest httpRequest) {
        try {
            ConfirmationToken confirmationToken = findConfirmationTokenByToken(token.toLowerCase())
                    .orElseThrow(() -> new IllegalArgumentException("token not found"));
            if (confirmationToken.getConfirmedAt() != null) {
                throw new IllegalStateException("email already confirmed");
            }

            if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("token expired");
            }
            confirmationToken.setConfirmedAt(LocalDateTime.now());
            enableAppUser(confirmationToken.getAppUser().getEmail(), httpRequest);
            return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS, Constants.RESPONSE_MESSAGE.SUCCESS, "confirmed");
        } catch (Exception exception) {
            log.error(String.format("exception: %s", exception.getMessage()));
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED, Constants.RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }


    @Override
    /**
     * this method is required by UserDetailsService
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            log.info(String.format("user with email %s was loaded", username));
            return convertAppUserToDao(appUserRepository.findAppUserByEmail(username));
        } catch (Exception exception) {
            log.error(String.format("failed to load user with email %s ", username));
            log.error(String.format(USERNAME_NOT_FOUND_MSG, username));
            throw new UsernameNotFoundException(String.format(USERNAME_NOT_FOUND_MSG, username));
        }
    }

    @Override
    public ResponseDto deleteAppUserAccount(String email) {
        try {
            appUserRepository.deleteById(appUserRepository.findAppUserByEmail(email).get().getId());
            log.info(String.format("user with email %s was deleted", email));
        } catch (Exception exception) {
            log.error(String.format("failed to delete user with email %s ", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto loadAllAppUsers() {
        try {
            log.info("all users were loaded from database");
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, appUserRepository.findAll());
        } catch (Exception exception) {
            log.error("failed to load all app users");
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
    }

    @Override
    public ResponseDto addRoleToAppUser(String email, String role, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().getAppUserRole().stream().forEach(appUserRoles -> {
                if (appUserRoles.getRule().equals(role)){
                    appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("add role", "FAILED", httpRequest));
                    throw new IllegalStateException(String.format("user with email %s already has %s role ",email,role));
                }
            });
            appUser.get().getAppUserRole().add(new AppUserRoles(role));
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("add role", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to add role to user %s with email %s ", role, email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        log.info(String.format("user with email %s role was changed", email));
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }
    @Override
    public ResponseDto removeRoleFromAppUser(String email, String role, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            Set<AppUserRoles> roles = appUser.get().getAppUserRole();
            for (AppUserRoles appUserRoles : roles){
                if (appUserRoles.getRule().equals(role)){
                    appUser.get().getAppUserRole().remove(appUserRoles);
                    appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("remove role", SUCCESS, httpRequest));
                    appUserRepository.save(appUser.get());
                    log.info(String.format("user with email %s role was removed", email));
                    return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
                }
            }
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("remove role", "FAILED", httpRequest));
            throw new IllegalStateException(String.format("user with email %s dont' have %s role ",email,role));
            
        } catch (Exception exception) {
            log.error(String.format("failed to remove role from user %s with email %s ", role, email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        
    }

    @Override
    public ResponseDto unLockAppUser(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsAccountNonLocked(TRUE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("unLocked", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s was unlocked", email));
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        } catch (Exception exception) {
            log.error(String.format("failed to unlock user with email %s ", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
    }

    @Override
    public ResponseDto lockAppUser(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsAccountNonLocked(FALSE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("locked", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s was locked", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to lock user with email %s ", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto enableAppUser(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsEnabled(TRUE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("enabled", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s was enabled", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to enabled user with email %s ", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto disableAppUser(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsEnabled(FALSE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("disabled", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s was disabled", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to disabled user with email %s ", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto setAppUserNonExpired(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsAccountNonExpired(TRUE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("set account non expired", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s was was set to non expired", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to set user with email %s to non expired", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto setAppUserExpired(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsAccountNonExpired(FALSE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("set account expired", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s was was set to expired", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to set user with email %s to expired", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto setAppUserCredentialsNonExpired(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsCredentialsNonExpired(TRUE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("set credentials non expired", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s credentials was set to non expired", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to set user with email %s credentials to non expired", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto setAppUserCredentialsExpired(String email, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().setIsCredentialsNonExpired(FALSE);
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("set credentials expired", SUCCESS, httpRequest));
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s credentials was set to expired", email));
            appUserRepository.save(appUser.get());
        } catch (Exception exception) {
            log.error(String.format("failed to set user with email %s credentials to expired", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
        return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
    }

    @Override
    public ResponseDto changeAppUserPassword(String email, String password, HttpServletRequest httpRequest) {
        try {
            Optional<AppUser> appUser = appUserRepository.findAppUserByEmail(email);
            appUser.get().getOldPasswords().forEach(oldPasswords -> {
                if (passwordEncoder.matches(password, oldPasswords.getPassword())) {
                    appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("change password", "failed", httpRequest));
                    throw new IllegalStateException(" password was used before ");
                }
            });
            appUser.get().setPassword(passwordEncoder.encode(password));
            appUser.get().getAppUserUpdateHistories().add(buildUpdateHistory("change password", SUCCESS, httpRequest));
            appUser.get().getOldPasswords().add(OldPasswords.builder().password(passwordEncoder.encode(password)).changedAt(LocalDateTime.now()).build());
            appUserRepository.save(appUser.get());
            log.info(String.format("user with email %s password was changed", email));
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, REQUEST_SUCCESS);
        } catch (Exception exception) {
            log.error(String.format("failed to change password for user with email %s ", email));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, APPLICATION_CONTROLLER_ERROR);
        }
    }

    /**
     * this method is to register new AppUser with USER Rule and add it to database
     * and create new confirmation token and save it to database then
     * send confirmation email
     *
     * @param registrationDto
     * @return
     */
    @Override
    public ResponseDto saveNewAppUser(RegistrationDto registrationDto) {
        final String FAILED_MESSAGE = "failed to create new user with email %s ";
        try {
            if (!emailValidator.isValidEmailAddress(registrationDto.getEMAIL())) {
                log.error(String.format(FAILED_MESSAGE, registrationDto.getEMAIL()));
                log.error("email not valid");
                throw new IllegalStateException("email not valid");
            }
            if (appUserRepository.findAppUserByEmail(registrationDto.getEMAIL()).isPresent()) {
                log.error(String.format(FAILED_MESSAGE, registrationDto.getEMAIL()));
                log.error("email already in use");
                throw new IllegalStateException("email already in use");
            }
            if (!registrationDto.getPASSWORD().equals(registrationDto.getCONFIRM_PASSWORD())) {
                log.error(String.format(FAILED_MESSAGE, registrationDto.getEMAIL()));
                log.error("passwords does not match");
                throw new IllegalStateException("passwords does not match");
            }
            AppUser appUser = createAppUserFormRegistrationDto(registrationDto);
            appUserRepository.save(appUser);
            log.info(String.format("user with email %s was created and added to database waiting activation", registrationDto.getEMAIL()));
            ConfirmationToken token = createNewToken(appUser);
            emailSender.sender(registrationDto.getEMAIL(), buildEmail(registrationDto.getFIRST_NAME()
                    , String.format(TOKEN_CONFIRMATION_URL, token.getToken())));
            return new ResponseDto(RESPONSE_CODE.SUCCESS, RESPONSE_MESSAGE.SUCCESS, token.getToken());
        } catch (Exception exception) {
            log.error(String.format(FAILED_MESSAGE, registrationDto.getEMAIL()));
            log.error(APPLICATION_CONTROLLER_ERROR.concat(exception.getMessage()));
            return new ResponseDto(RESPONSE_CODE.FAILED, RESPONSE_MESSAGE.FAILED, exception.getMessage());
        }
    }


    /**
     * this method is to convert the app user from database to
     * AppUserDao instance
     *
     * @param appUser is the user from the database to be converted
     * @return a new instance of AppUserDao from the appUser parameter
     */
    private AppUserDao convertAppUserToDao(Optional<AppUser> appUser) {
        AppUserDao appUserDao = new AppUserDao();
        appUserDao.setUserName(appUser.get().getEmail());
        appUserDao.setPassword(appUser.get().getPassword());
        appUserDao.setAppUserRole(appUser.get().getAppUserRole());
        appUserDao.setAccountNonLocked(appUser.get().getIsAccountNonLocked());
        appUserDao.setEnabled(appUser.get().getIsEnabled());
        appUserDao.setAccountNonExpired(appUser.get().getIsCredentialsNonExpired());
        appUserDao.setCredentialsNonExpired(appUser.get().getIsCredentialsNonExpired());
        return appUserDao;
    }


    private AppUserUpdateHistory buildUpdateHistory(String action, String status, HttpServletRequest httpRequest) {
        return AppUserUpdateHistory.builder().action(action)
                .changedAt(LocalDateTime.now()).changerIpAddress(httpRequest.getRemoteAddr())
                .port(httpRequest.getRemotePort()).status(status).build();
    }

    /**
     * this method is to convert RegistrationDto instance to AppUser
     *
     * @param registrationDto is the instance to be converted
     * @return a new AppUser object from the registrationDto
     */
    private AppUser createAppUserFormRegistrationDto(RegistrationDto registrationDto) {
        HashSet<OldPasswords> oldPasswords = new HashSet<>();
        oldPasswords.add(OldPasswords.builder().password(passwordEncoder.encode(registrationDto.getPASSWORD())).changedAt(LocalDateTime.now())
                .changerIpAddress(registrationDto.getIpAddress()).port(registrationDto.getPort()).build());
        return AppUser.builder().firstName(registrationDto.getFIRST_NAME().toLowerCase())
                .lastName(registrationDto.getLAST_NAME().toLowerCase())
                .email(registrationDto.getEMAIL().toLowerCase())
                .age(registrationDto.getAGE())
                .gender(registrationDto.getGENDER().toLowerCase())
                .appUserRole(registrationDto.getRoles())
                .password(passwordEncoder.encode(registrationDto.getPASSWORD()))
                .oldPasswords(oldPasswords)
                .userName(registrationDto.getUSER_NAME())
                .createdAt(LocalDateTime.now())
                .createdPort(registrationDto.getPort())
                .createdIpAddress(registrationDto.getIpAddress())
                .isEnabled(FALSE)
                .isAccountNonLocked(TRUE)
                .isCredentialsNonExpired(TRUE)
                .isAccountNonExpired(TRUE).build();
    }

    //TODO : change this buildEmail location
    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
}
