package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.intities.AppUser;
import com.eCommerce.demo.intities.AppUserRoles;
import com.eCommerce.demo.intities.ConfirmationToken;
import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.repository.AppUserRepository;
import com.eCommerce.demo.services.AppUserServices;
import com.eCommerce.demo.services.ConfirmationTokenService;
import com.eCommerce.demo.services.EmailSender;
import com.eCommerce.demo.utils.EmailValidator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;


@Service
public class AppUserServicesImpl implements AppUserServices {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return convertAppUserToDao( appUserRepository.findAppUserByEmail(username));
        }catch (Exception exception){
            throw new UsernameNotFoundException(String.format(Constants.USERNAME_NOT_FOUND_MSG,username));
        }
    }
    @Override
    public ResponseDto loadAllUsers (){
        return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS,Constants.RESPONSE_MESSAGE.SUCCESS,appUserRepository.findAll());
    }

    @Override
    @Transactional
    public void enableAppUser (String email){
        appUserRepository.findAppUserByEmail(email).get().setIsEnabled(Boolean.TRUE);
    }
    @Override
    @Transactional
    public void unLockAppUser (String email){
        appUserRepository.findAppUserByEmail(email).get().setIsAccountNonLocked(Boolean.TRUE);
    }

    /**
     * this method is to register new AppUser with USER Rule and add it to database
     * and create new confirmation token and save it to database then
     * send confirmation email
     * @param registrationDto
     * @return
     */
    @Override
    public ResponseDto saveNewAppUser(RegistrationDto registrationDto){
        try{
        if (!emailValidator.isValidEmailAddress(registrationDto.getEMAIL())){
            throw new IllegalStateException("email not valid");
        }
        if (appUserRepository.findAppUserByEmail(registrationDto.getEMAIL()).isPresent()){
            throw new IllegalStateException("email already in use");
        }
        if (!registrationDto.getPASSWORD().equals(registrationDto.getCONFIRM_PASSWORD())){
            throw new IllegalStateException("passwords does not match");
        }
        AppUser appUser = createAppUserFormDto(registrationDto);
        appUserRepository.save(appUser);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now()
                ,LocalDateTime.now().plusMinutes(15),appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        emailSender.sender(registrationDto.getEMAIL(),buildEmail(registrationDto.getFIRST_NAME()
                ,String.format("http://localhost:8080/users/confirmToken/%S",token)));
        return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS,Constants.RESPONSE_MESSAGE.SUCCESS,token);
        }catch (Exception exception){
            return new ResponseDto(Constants.RESPONSE_CODE.FAILED,Constants.RESPONSE_MESSAGE.FAILED,exception.getMessage());
        }
    }

    /**
     * this method is to confirm the token confirmation
     * and enable the confirmed AppUser
     * @param token is the token to be confirmed
     * @return ResponseDto to the controller if the token was confirmed
     * or throw IllegalStateException if the token was already confirmed or
     * it was expired.
     */
    @Override
    @Transactional
    public ResponseDto confirmToken(String token){
        ConfirmationToken confirmationToken = confirmationTokenService.findConfirmationTokenByToken(token.toLowerCase())
                .orElseThrow(()-> new IllegalArgumentException("token not found"));
        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("email already confirmed");
        }
        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new IllegalStateException("token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        enableAppUser(confirmationToken.getAppUser().getEmail());
        unLockAppUser(confirmationToken.getAppUser().getEmail());

        return new ResponseDto(Constants.RESPONSE_CODE.SUCCESS,Constants.RESPONSE_MESSAGE.SUCCESS,"confirmed");
    }


    /**
     * this method is to convert the app user from database to
     * AppUserDao instance
     * @param appUser is the user from the database to be converted
     * @return a new instance of AppUserDao from the appUser parameter
     */
    private AppUserDao convertAppUserToDao(Optional<AppUser> appUser){
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

    /**
     * this method is to convert RegistrationDto instance to AppUser
     * @param registrationDto is the instance to be converted
     * @return a new AppUser object from the registrationDto
     */
    private AppUser createAppUserFormDto (RegistrationDto registrationDto){
        AppUser appUser = new AppUser();
        appUser.setFirstName(registrationDto.getFIRST_NAME());
        appUser.setLastName(registrationDto.getLAST_NAME());
        appUser.setEmail(registrationDto.getEMAIL());
        appUser.setAge(registrationDto.getAGE());
        appUser.setGender(registrationDto.getGENDER());
        appUser.setAppUserRole(registrationDto.getRoles());
        appUser.setPassword(passwordEncoder.encode(registrationDto.getPASSWORD()));
        appUser.setUserName(registrationDto.getUSER_NAME());
        return appUser;
    }

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
