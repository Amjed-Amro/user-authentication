package com.eCommerce.demo.services.handlers.implementations;

import com.eCommerce.demo.intities.*;
import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.models.dao.UserInfoDao;
import com.eCommerce.demo.models.dto.RegistrationDto;
import com.eCommerce.demo.repository.AppUserRepository;
import com.eCommerce.demo.repository.LoginIpsRepository;
import com.eCommerce.demo.services.handlers.interfaces.AppUsersHandler;
import com.eCommerce.demo.utils.EmailValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static com.eCommerce.demo.constants.Constants.ROLES.USER;
import static com.eCommerce.demo.constants.ErrorConstants.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
@Log4j2
@Service
public class AppUserHandlerImpl implements AppUsersHandler {

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
            return convertAppUserToDao(appUserRepository.findAppUserByEmail(username));
        } catch (Exception exception) {
            throw new UsernameNotFoundException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND, username));
        }
    }
    @Override
    public UserInfoDao getUserInfo(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        return UserInfoDao.builder()
                .firstName(appUser.getFirstName())
                .lastName(appUser.getLastName())
                .userName(appUser.getUserName())
                .email(appUser.getEmail())
                .gender(appUser.getGender())
                .age(appUser.getAge()).build();
    }
    @Override
    public Set<UserDetails> loadAllAppUsers(){
        List<AppUser> appUsers = appUserRepository.findAll();
        Set<UserDetails> userDetails = new HashSet<>();
        appUsers.forEach(appUser -> {
            userDetails.add(convertAppUserToDao(Optional.ofNullable(appUser)));
        });
        return userDetails;
    }
    @Override
    public Set<UserDetails> loadAllAppUsersByRole(String role){
        List<AppUser> appUsers = appUserRepository.findAll();
        Set<UserDetails> userDetails = new HashSet<>();
        appUsers.forEach(appUser -> {
            appUser.getAppUserRole().forEach(appUserRoles -> {
                if (appUserRoles.getRole().equals(role)){
                    userDetails.add(convertAppUserToDao(Optional.ofNullable(appUser)));
                };
            });
        });
        return userDetails;
    }
    @Override
    public Boolean checkLoginIp(String email, String ip){
        Set<LoginIp> userIps = loginIpsRepository.findAllByIpAddressAndEmail(ip,email);
        if (userIps.isEmpty()){
            loginIpsRepository.save(LoginIp.builder().ipAddress(ip).email(email).loginAt(LocalDateTime.now()).build());
            log.info(String.format("login from new IP address for user with email %s",email));
            return FALSE;
        }
        return TRUE;
    }
    @Override
    public AppUser saveNewAppUser(RegistrationDto registrationDto) {

        if (!emailValidator.isValidEmailAddress(registrationDto.getEMAIL())) {
            throw new IllegalStateException("email not valid");
        }
        if (appUserRepository.findAppUserByEmail(registrationDto.getEMAIL()).isPresent()) {
            throw new IllegalStateException("email already in use");
        }
        if (!registrationDto.getPASSWORD().equals(registrationDto.getCONFIRM_PASSWORD())) {
            throw new IllegalStateException("passwords does not match");
        }
        AppUser appUser = createAppUserFormRegistrationDto(registrationDto);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s was saved to database",registrationDto.getEMAIL()));

        return appUser;
    }
    @Override
    public Boolean addToUpdateHistory(String email, String action, String remoteIpAddress, String changerEmail){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.getAppUserUpdateHistories().add(AppUserUpdateHistory.builder().action(action).changedAt(LocalDateTime.now()).changerIpAddress(remoteIpAddress)
                .changerEmail(changerEmail).build());
        appUserRepository.save(appUser);
        log.info(String.format("new update history for user with email %s, action: %s", email, action));

        return TRUE;
    }
    @Override
    public void deleteAppUserAccount(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUserRepository.deleteById(appUser.getId());
        log.info(String.format("user with email %s was deleted from database", email));

    }
    @Override
    public Boolean changeAppUserPassword(String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)){
            throw new IllegalStateException("passwords dont mach");
        }
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.getOldPasswords().forEach(oldPasswords -> {
            if (passwordEncoder.matches(password, oldPasswords.getPassword())) {
                throw new IllegalStateException("password was used before");}});
        appUser.setPassword(passwordEncoder.encode(password));
        appUser.getOldPasswords().add(OldPasswords.builder().password(passwordEncoder.encode(password)).changedAt(LocalDateTime.now()).build());
        appUserRepository.save(appUser);
        log.info(String.format("password was changed for user with email %s", email));

        return TRUE;
    }
    @Override
    public String getFirstName(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        return appUser.getFirstName();
    }
    @Override
    public Boolean isActivatedAppUser(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (!appUser.getIsActivated()){
            throw new IllegalStateException("account is not activated");
        }
        return TRUE;
    }
    @Override
    public Boolean isNotActivatedAppUser(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (appUser.getIsActivated()){
            throw new IllegalStateException("account is  activated");
        }
        return TRUE;
    }
    @Override
    public void activateAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (appUser.getIsActivated()){
            throw new IllegalStateException("account already activated");
        }
        appUser.setIsActivated(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s is now activated", email));

    }
    @Override
    public void deactivateAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (!appUser.getIsActivated()){
            throw new IllegalStateException("account already deactivated");
        }
        appUser.setIsActivated(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s is now deactivated", email));
    }
    @Override
    public void unLockAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (appUser.getIsAccountNonLocked()){
            throw new IllegalStateException("account already unlocked");
        }
        appUser.setIsAccountNonLocked(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s is now unlocked", email));
    }
    @Override
    public void lockAppUser(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (!appUser.getIsAccountNonLocked()){
            throw new IllegalStateException("account already locked");
        }
        appUser.setIsAccountNonLocked(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s is now locked", email));
    }
    @Override
    public void setAppUserNonExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (appUser.getIsAccountNonExpired()){
            throw new IllegalStateException("account already not expired");
        }
        appUser.setIsAccountNonExpired(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s is now set to non expired", email));
    }
    @Override
    public void setAppUserExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (!appUser.getIsAccountNonExpired()){
            throw new IllegalStateException("account already expired");
        }
        appUser.setIsAccountNonExpired(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format("user with email %s is now set to expired", email));
    }
    @Override
    public void setAppUserCredentialsNonExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (appUser.getIsCredentialsNonExpired()){
            throw new IllegalStateException("credentials already not expired");
        }
        appUser.setIsCredentialsNonExpired(TRUE);
        appUserRepository.save(appUser);
        log.info(String.format("credentials of user with email %s is now set to non expired", email));
    }
    @Override
    public void setAppUserCredentialsExpired(String email) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        if (!appUser.getIsCredentialsNonExpired()){
            throw new IllegalStateException("credentials already expired");
        }
        appUser.setIsCredentialsNonExpired(FALSE);
        appUserRepository.save(appUser);
        log.info(String.format("credentials of user with email %s is now set to expired", email));
    }
    @Override
    public void addRoleToAppUser(String email, String role) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.getAppUserRole().forEach(appUserRoles -> {
            if (appUserRoles.getRole().equals(role)){
                throw new IllegalStateException(String.format(USER_WITH_EMAIL_S_ALREADY_HAS_S_ROLE,email,role));}});
        appUser.getAppUserRole().add(AppUserRoles.builder().role(role).build());
        appUserRepository.save(appUser);
        log.info(String.format("role %s, was added to user with email %s",role, email));
    }
    @Override
    public Boolean removeRoleFromAppUser(String email, String role) {
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        Set<AppUserRoles> roles = appUser.getAppUserRole();
        for (AppUserRoles r : roles){
            if (r.getRole().equals(role)){
                appUser.getAppUserRole().remove(r);
                appUserRepository.save(appUser);
                log.info(String.format("role %s, was removed from user with email %s",role, email));
                return TRUE;
            }
        }
        throw new IllegalStateException(String.format(USER_WITH_EMAIL_S_DONT_HAVE_S_ROLE,email,role));
    }
    @Override
    public void changeFirstName(String email, String firstName){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.setFirstName(firstName);
        appUserRepository.save(appUser);
        log.info(String.format("first name of user with email %s was changed", email));
    }
    @Override
    public void changeLastName(String email, String lastName){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.setLastName(lastName);
        appUserRepository.save(appUser);
        log.info(String.format("last name of user with email %s was changed", email));
    }
    @Override
    public void changeUserName(String email, String userName){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.setUserName(userName);
        appUserRepository.save(appUser);
        log.info(String.format("user name of user with email %s was changed", email));
    }
    @Override
    public void changeGender(String email, String gender){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.setGender(gender);
        appUserRepository.save(appUser);
        log.info(String.format("gender name of user with email %s was changed", email));
    }
    @Override
    public void changeAge(String email, Integer age){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        appUser.setAge(age);
        appUserRepository.save(appUser);
        log.info(String.format("age name of user with email %s was changed", email));
    }
    @Override
    public Set<AppUserUpdateHistory> getUserUpdateHistory(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        return appUser.getAppUserUpdateHistories();
    }
    @Override
    public Set<AppUserRoles> getAppUserRoles(String email){
        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(()-> new IllegalStateException(String.format(USER_WITH_EMAIL_S_WAS_NOT_FOUND,email)));
        return appUser.getAppUserRole();
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
        appUserDao.setEmail(appUser.get().getEmail());
        appUserDao.setPassword(appUser.get().getPassword());
        appUserDao.setAppUserRole(appUser.get().getAppUserRole());
        appUserDao.setAccountNonLocked(appUser.get().getIsAccountNonLocked());
        appUserDao.setEnabled(appUser.get().getIsActivated());
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
        HashSet<AppUserRoles> appUserRoles = new HashSet<>();
        HashSet<AppUserUpdateHistory> appUserUpdateHistories = new HashSet<>();
        appUserRoles.add(AppUserRoles.builder().role(USER).build());
        oldPasswords.add(OldPasswords.builder().password(passwordEncoder.encode(registrationDto.getPASSWORD())).changedAt(LocalDateTime.now()).build());
        loginIpsRepository.save(LoginIp.builder().loginAt(LocalDateTime.now()).ipAddress(registrationDto.getIpAddress()).email(registrationDto.getEMAIL()).build());
        return AppUser.builder().firstName(registrationDto.getFIRST_NAME().toLowerCase())
                .lastName(registrationDto.getLAST_NAME().toLowerCase())
                .email(registrationDto.getEMAIL().toLowerCase())
                .age(registrationDto.getAGE())
                .gender(registrationDto.getGENDER().toLowerCase())
                .appUserRole(registrationDto.getRoles())
                .password(passwordEncoder.encode(registrationDto.getPASSWORD()))
                .oldPasswords(oldPasswords)
                .userName(registrationDto.getUSER_NAME())
                .isActivated(FALSE)
                .isAccountNonLocked(TRUE)
                .isCredentialsNonExpired(TRUE)
                .isAccountNonExpired(TRUE)
                .appUserRole(appUserRoles)
                .appUserUpdateHistories(appUserUpdateHistories)
                .build();
    }
}
