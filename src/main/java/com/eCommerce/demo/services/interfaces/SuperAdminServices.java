package com.eCommerce.demo.services.interfaces;

import com.eCommerce.demo.models.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface SuperAdminServices {

    ResponseDto changeFirstNameOfUser(String email, String firstName, HttpServletRequest request);

    ResponseDto changeLastNameOfUser(String email, String lastName, HttpServletRequest request);

    ResponseDto changeUserNameOfUser(String email, String userName, HttpServletRequest request);

    ResponseDto changeGenderOfUser(String email, String gender, HttpServletRequest request);

    ResponseDto changeAgeOfUser(String email, Integer age, HttpServletRequest request);

    ResponseDto changePasswordOfUser(String email, String password, String confirmPassword, HttpServletRequest request);

    ResponseDto removeAppUser(String email);

    ResponseDto activateAppUser(String email, HttpServletRequest request);

    ResponseDto deactivateAppUser(String email, HttpServletRequest request);

    ResponseDto unlockAppUser(String email, HttpServletRequest request);

    ResponseDto lockAppUser(String email, HttpServletRequest request);

    ResponseDto setAppUserExpired(String email, HttpServletRequest request);

    ResponseDto setAppUserNonExpired(String email, HttpServletRequest request);

    ResponseDto setAppUserCredentialsNonExpired(String email, HttpServletRequest request);

    ResponseDto setAppUserCredentialsExpired(String email, HttpServletRequest request);

    ResponseDto addRoleToAppUser(String email, String role, HttpServletRequest request);

    ResponseDto removeRoleFromAppUser(String email, String role, HttpServletRequest request);

    ResponseDto getAppUserInfoByEmail(String email);

    ResponseDto getAllAppUsers();

    ResponseDto getAppUserTokens(String email);

    ResponseDto getAppUserUpdateHistory(String email);

    ResponseDto getAppUserRoles(String email);

    ResponseDto getAppUserLoginIps(String email);

    ResponseDto getAllAppUsersByRole(String role);
}
