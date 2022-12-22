package com.eCommerce.demo.services.handlers.interfaces;

import com.eCommerce.demo.intities.AppUser.AppUser;
import com.eCommerce.demo.intities.AppUser.AppUserToken;
import com.eCommerce.demo.intities.AppUser.AppUserUpdateHistory;
import com.eCommerce.demo.intities.AppUser.LoginIp;
import com.eCommerce.demo.models.dto.RegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

public interface AppUserServices extends UserDetailsService {


    Boolean checkLoginIp(String email, String ip);

    AppUser saveNewAppUser(RegistrationDto registrationDto);

    Boolean addToUpdateHistory(String email, String action, String status, String remoteIpAddress);

    Boolean enableAppUSER(AppUserToken confirmationToken);

    Boolean deleteAppUserAccount(String email);

    Boolean changeAppUserPassword(String email, String password, String confirmPassword);

    Set<AppUserUpdateHistory> getAppUserUpdateHistory(String email);

    Set<LoginIp> getAppUserLoginIpAddresses(String email);

    String getFirstName(String email);

    Boolean isEnabledAppUser(String email);

    Boolean enableAppUser(String email);

    Boolean disableAppUser(String email);

    Boolean unLockAppUser(String email);

    Boolean lockAppUser(String email);

    Boolean setAppUserNonExpired(String email);

    Boolean setAppUserExpired(String email);

    Boolean setAppUserCredentialsNonExpired(String email);

    Boolean setAppUserCredentialsExpired(String email);

    Boolean addRoleToAppUser(String email, String role);

    Boolean removeRoleFromAppUser(String email, String role);
}
