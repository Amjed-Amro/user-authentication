package com.eCommerce.demo.services.handlers.implementations;

import com.eCommerce.demo.constants.Messages;
import com.eCommerce.demo.intities.AppUser.*;
import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.repository.AppUserRepository;
import com.eCommerce.demo.repository.LoginIpsRepository;
import com.eCommerce.demo.services.handlers.interfaces.AppUserServices;
import com.eCommerce.demo.utils.EmailValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.eCommerce.demo.constants.ErrorConstants.*;
import static com.eCommerce.demo.constants.Messages.LogsMessages.*;
import static java.lang.Boolean.*;


@Log4j2
@Service
public class AppUserServicesImpl implements AppUserServices {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private LoginIpsRepository loginIpsRepository;

    @Override
    /**
     * this method is required by UserDetailsService interface
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            log.info(String.format(USER_SUCCESSFULLY_LOADED_WITH_EMAIL, username));
            return convertAppUserToDao(appUserRepository.findAppUserByEmail(username));
        } catch (Exception exception) {
            log.error(String.format(FAILED_TO_RETRIEVE_USER_WITH_EMAIL, username));
            throw new UsernameNotFoundException(String.format("user with email %s was not found", username));
        }
    }
    @Override
    public Boolean checkLoginIp(String email, String ip){
        Set<LoginIp> userIps = loginIpsRepository.findAllByIpAddressAndEmail(ip,email);
        if (userIps.isEmpty()){
            loginIpsRepository.save(LoginIp.builder().ipAddress(ip).email(email).loginDate(new Date()).build());
            return FALSE;
        }
        return TRUE;
    }
    /**
     * this method is to register new AppUser with USER Rule and add it to database
     * and create new confirmation token and save it to database then
     * send confirmation email
     *
     * @param registrationDto is an object contains user information
     * @return  the created AppUser
     */
    @Override
    public AppUser saveNewAppUser(RegistrationDto registrationDto) {

        if (!emailValidator.isValidEmailAddress(registrationDto.getEMAIL())) {
            log.error(EMAIL_NOT_VALID);
            throw new IllegalStateException(EMAIL_NOT_VALID);
        }
        if (appUserRepository.findAppUserByEmail(registrationDto.getEMAIL()).isPresent()) {
            log.error(String.format(Messages.LogsMessages.EMAIL_ALREADY_IN_USE,registrationDto.getEMAIL()));
            throw new IllegalStateException("email already in use");
        }
        if (!registrationDto.getPASSWORD().equals(registrationDto.getCONFIRM_PASSWORD())) {
            log.error("passwords does not match");
            throw new IllegalStateException("passwords does not match");
        }
        AppUser appUser = createAppUserFormRegistrationDto(registrationDto);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s was created and added to database waiting activation", registrationDto.getEMAIL()));
        return appUser;
    }

    /**
     * add an action to the update history of a user
     * @param action is the update action done to user
     * @param status is the status of the action done
     * @param remoteIpAddress is the ip address of the person did the change
     * @return true if the action was added to history
     */
    @Override
    public Boolean addToUpdateHistory(String email, String action, String status, String remoteIpAddress){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException(String.format(USER_NOT_FOUND,email)));
        appUser.getAppUserUpdateHistories().add(AppUserUpdateHistory.builder().action(action).changedAt(new Date()).remoteIpAddress(remoteIpAddress)
                .status(status).build());
        appUserRepository.save(appUser);
        log.info(String.format(UPDATE_HISTORY_UPDATED_FOR_USER,email));
        return TRUE;
    }
    /**
     * change the status of the app user to enable when user is confirmed
     * @param confirmationToken is the confirmation token that contains the user email
     * @return true if the user is enabled
     */
    @Override
    public Boolean enableAppUSER(AppUserToken confirmationToken){
        String email = confirmationToken.getAppUserEmail();
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsEnabled(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_ENABLED,email));
        return TRUE;
    }
    /**
     *this method is used to delete app user
     * @param email is the user email
     * @return true if the user was deleted
     */
    @Override
    public Boolean deleteAppUserAccount(String email) {
        appUserRepository.deleteById(appUserRepository.findAppUserByEmail(email).get().getId());
        log.info(String.format(USER_WITH_EMAIL_DELETED, email));
        return TRUE;
    }
    @Override
    public Boolean changeAppUserPassword(String email, String password,String confirmPassword) {
        if (!password.equals(confirmPassword)){
            throw new IllegalStateException(PASSWORDS_DONT_MATCH);
        }
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.getOldPasswords().forEach(oldPasswords -> {
            if (passwordEncoder.matches(password, oldPasswords.getPassword())) {
                throw new IllegalStateException(PASSWORD_USED_BEFORE);}});
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.getOldPasswords().add(OldPasswords.builder().password(passwordEncoder.encode(password)).changedAt(LocalDateTime.now()).build());
        appUserRepository.save(appUser);
        log.info(String.format(PASSWORD_CHANGED_FOR_USER, email));
        return TRUE;
    }
    @Override
    public Set<AppUserUpdateHistory> getAppUserUpdateHistory(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        log.info(String.format(USER_UPDATE_HISTORY_LOADED,email));
        return appUser.getAppUserUpdateHistories();
    }
    @Override
    public Set<LoginIp> getAppUserLoginIpAddresses(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        log.info(String.format(USER_UPDATE_HISTORY_LOADED,email));
        return appUser.getLoginIps();
    }
    @Override
    public String getFirstName(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        return appUser.getFirstName();
    }
    @Override
    public Boolean isEnabledAppUser(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        if (!appUser.getIsEnabled()){
            throw new IllegalStateException(ACCOUNT_IS_DISABLED);
        }
        return TRUE;
    }
    @Override
    public Boolean enableAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsEnabled(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_WAS_ENABLED, email));
        return TRUE;
    }
    @Override
    public Boolean disableAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsEnabled(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_WAS_DISABLED, email));
        return TRUE;
    }
    @Override
    public Boolean unLockAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsAccountNonLocked(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_WAS_UNLOCKED, email));
        return TRUE;
    }
    @Override
    public Boolean lockAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsAccountNonLocked(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_WAS_LOCKED, email));
        return TRUE;
    }
    @Override
    public Boolean setAppUserNonExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsAccountNonExpired(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_WAS_SET_TO_NON_EXPIRED, email));
        return TRUE;
    }
    @Override
    public Boolean setAppUserExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsAccountNonExpired(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_WAS_SET_TO_EXPIRED, email));
        return TRUE;
    }
    @Override
    public Boolean setAppUserCredentialsNonExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsCredentialsNonExpired(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_CREDENTIALS_WAS_SET_TO_NON_EXPIRED, email));
        return TRUE;
    }
    @Override
    public Boolean setAppUserCredentialsExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.setIsCredentialsNonExpired(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format(USER_WITH_EMAIL_CREDENTIALS_WAS_SET_TO_EXPIRED, email));
        return TRUE;
    }
    @Override
    public Boolean addRoleToAppUser(String email, String role) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        appUser.getAppUserRole().forEach(appUserRoles -> {
            if (appUserRoles.getRole().equals(role)){
                throw new IllegalStateException(String.format(USER_WITH_EMAIL_S_ALREADY_HAS_S_ROLE,email,role));}});
        appUser.getAppUserRole().add(AppUserRoles.builder().role(role).build());
        appUserRepository.save(appUser);
        log.info(String.format(RULE_S_WAS_ADDED_SUCCESSFULLY_FOR_USER_WITH_EMAIL_S, role, email));
        return TRUE;
    }
    @Override
    public Boolean removeRoleFromAppUser(String email, String role) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_NOT_FOUND,email)));
        Set<AppUserRoles> roles = appUser.getAppUserRole();
        for (AppUserRoles r : roles){
            if (r.getRole().equals(role)){
                appUser.getAppUserRole().remove(r);
                appUserRepository.save(appUser);
                log.info(String.format(RULE_S_WAS_ADDED_SUCCESSFULLY_FOR_USER_WITH_EMAIL_S, role, email));
                return TRUE;
        }
    }
        throw new IllegalStateException(String.format(USER_WITH_EMAIL_S_DONT_HAVE_S_ROLE,email,role));
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


    /**
     * this method is to convert RegistrationDto instance to AppUser
     *
     * @param registrationDto is the instance to be converted
     * @return a new AppUser object from the registrationDto
     */
    private AppUser createAppUserFormRegistrationDto(RegistrationDto registrationDto) {
        HashSet<OldPasswords> oldPasswords = new HashSet<>();
        HashSet<AppUserUpdateHistory> appUserUpdateHistories = new HashSet<>();
        oldPasswords.add(OldPasswords.builder().password(passwordEncoder.encode(registrationDto.getPASSWORD())).changedAt(LocalDateTime.now()).build());
        loginIpsRepository.save(LoginIp.builder().loginDate(new Date()).ipAddress(registrationDto.getIpAddress()).email(registrationDto.getEMAIL()).build());
        return AppUser.builder().firstName(registrationDto.getFIRST_NAME().toLowerCase())
                .lastName(registrationDto.getLAST_NAME().toLowerCase())
                .email(registrationDto.getEMAIL().toLowerCase())
                .age(registrationDto.getAGE())
                .gender(registrationDto.getGENDER().toLowerCase())
                .appUserRole(registrationDto.getRoles())
                .password(passwordEncoder.encode(registrationDto.getPASSWORD()))
                .oldPasswords(oldPasswords)
                .userName(registrationDto.getUSER_NAME())
                .isEnabled(FALSE)
                .isAccountNonLocked(TRUE)
                .isCredentialsNonExpired(TRUE)
                .isAccountNonExpired(TRUE)
                .appUserUpdateHistories(appUserUpdateHistories)
                .build();
    }

}
