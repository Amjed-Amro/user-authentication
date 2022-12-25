package com.eCommerce.demo.services.handlers.interfaces;

import com.eCommerce.demo.intities.AppUser;
import com.eCommerce.demo.intities.AppUserRoles;
import com.eCommerce.demo.intities.AppUserUpdateHistory;
import com.eCommerce.demo.models.dao.UserInfoDao;
import com.eCommerce.demo.models.dto.RegistrationDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface AppUsersHandler extends UserDetailsService {
    UserInfoDao getUserInfo(String email);

    Set<UserDetails> loadAllAppUsers();

    Set<UserDetails> loadAllAppUsersByRole(String role);

    Boolean checkLoginIp(String email, String ip);

    AppUser saveNewAppUser(RegistrationDto registrationDto);

    Boolean addToUpdateHistory(String email, String action, String remoteIpAddress, String changerEmail);

    void deleteAppUserAccount(String email);

    Boolean changeAppUserPassword(String email, String password, String confirmPassword);

    String getFirstName(String email);

    Boolean isActivatedAppUser(String email);

    void activateAppUser(String email);

    void deactivateAppUser(String email);

    void unLockAppUser(String email);

    void lockAppUser(String email);

    void setAppUserNonExpired(String email);

    void setAppUserExpired(String email);

    void setAppUserCredentialsNonExpired(String email);

    void setAppUserCredentialsExpired(String email);

    void addRoleToAppUser(String email, String role);

    Boolean removeRoleFromAppUser(String email, String role);

    void changeFirstName(String email, String firstName);

    void changeLastName(String email, String lastName);

    void changeUserName(String email, String userName);

    void changeGender(String email, String gender);

    void changeAge(String email, Integer age);

    Set<AppUserUpdateHistory> getUserUpdateHistory(String email);

    Set<AppUserRoles> getAppUserRoles(String email);
}
