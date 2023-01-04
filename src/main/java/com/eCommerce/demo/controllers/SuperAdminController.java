package com.eCommerce.demo.controllers;

import com.eCommerce.demo.constants.Constants;
import com.eCommerce.demo.models.dto.ResponseDto;
import com.eCommerce.demo.services.interfaces.SuperAdminServices;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.eCommerce.demo.constants.Constants.ROLES.*;


@Controller
@RequestMapping (path = "superUser")
public class SuperAdminController {
    @Autowired
    private SuperAdminServices superAdminServices;

    @GetMapping(path = "getAllAppUsers")
    public ResponseEntity<ResponseDto> getAllAppUsers() {
        return new ResponseEntity<>(superAdminServices.getAllAppUsers(), HttpStatus.OK);}
    @GetMapping(path = "getUserByEmail/{email}")
    public ResponseEntity<ResponseDto> getUserByEmail(@PathVariable String email) {
        return new ResponseEntity<>(superAdminServices.getAppUserInfoByEmail(email.toLowerCase()), HttpStatus.OK);}
    @GetMapping(path = "removeAppUser/{email}")
    public ResponseEntity<ResponseDto> removeAppUser(@PathVariable String email) {
        return new ResponseEntity<>(superAdminServices.removeAppUser(email.toLowerCase()), HttpStatus.OK);}
    @GetMapping(path = "activateAccount/{email}")
    public ResponseEntity<ResponseDto> activateAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.activateAppUser(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "deactivateAccount/{email}")
    public ResponseEntity<ResponseDto> deactivateAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.deactivateAppUser(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "unlockAccount/{email}")
    public ResponseEntity<ResponseDto> lockAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.unlockAppUser(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "lockAccount/{email}")
    public ResponseEntity<ResponseDto> unlockAccount(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.lockAppUser(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.setAppUserExpired(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserNonExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserNonExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.setAppUserNonExpired(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserCredentialsExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserCredentialsExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.setAppUserCredentialsExpired(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "setAppUserCredentialsNonExpired/{email}")
    public ResponseEntity<ResponseDto> setAppUserCredentialsNonExpired(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.setAppUserCredentialsNonExpired(email.toLowerCase(), request), HttpStatus.OK);}
    @GetMapping(path = "addSuperAdminRole/{email}")
    public ResponseEntity<ResponseDto> addSuperAdminRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.addRoleToAppUser(email.toLowerCase(), SUPER_ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "removeSuperAdminRole/{email}")
    public ResponseEntity<ResponseDto> removeSuperAdminRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.removeRoleFromAppUser(email.toLowerCase(), SUPER_ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "addAdminRole/{email}")
    public ResponseEntity<ResponseDto> addAdminRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.addRoleToAppUser(email.toLowerCase(), Constants.ROLES.ADMIN, request), HttpStatus.OK);}
    @GetMapping(path = "addUserRole/{email}")
    public ResponseEntity<ResponseDto> addUserRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.addRoleToAppUser(email.toLowerCase(), Constants.ROLES.USER, request), HttpStatus.OK);}
    @GetMapping(path = "removeUserRole/{email}")
    public ResponseEntity<ResponseDto> removeUserRole(@PathVariable String email, HttpServletRequest request) {
        return new ResponseEntity<>(superAdminServices.removeRoleFromAppUser(email.toLowerCase(), Constants.ROLES.USER, request), HttpStatus.OK);}
    @GetMapping(path = "getAppUserTokens/{email}")
    public ResponseEntity<ResponseDto> getAppUserTokens(@PathVariable String email) {
        return new ResponseEntity<>(superAdminServices.getAppUserTokens(email.toLowerCase()), HttpStatus.OK);}
    @GetMapping(path = "getAppUserUpdateHistory/{email}")
    public ResponseEntity<ResponseDto> getAppUserUpdateHistory(@PathVariable String email) {
        return new ResponseEntity<>(superAdminServices.getAppUserUpdateHistory(email.toLowerCase()), HttpStatus.OK);}
    @GetMapping(path = "getAppUserRoles/{email}")
    public ResponseEntity<ResponseDto> getAppUserRoles(@PathVariable String email) {
        return new ResponseEntity<>(superAdminServices.getAppUserRoles(email.toLowerCase()), HttpStatus.OK);}
    @GetMapping(path = "getAppUserLoginIps/{email}")
    public ResponseEntity<ResponseDto> getAppUserLoginIps(@PathVariable String email) {
        return new ResponseEntity<>(superAdminServices.getAppUserLoginIps(email.toLowerCase()), HttpStatus.OK);}
    @GetMapping(path = "getAllSuperAdmins")
    public ResponseEntity<ResponseDto> getAllSuperAdmins() {
        return new ResponseEntity<>(superAdminServices.getAllAppUsersByRole(SUPER_ADMIN), HttpStatus.OK);}
    @GetMapping(path = "getAllAdmins")
    public ResponseEntity<ResponseDto> getAllAdmins() {
        return new ResponseEntity<>(superAdminServices.getAllAppUsersByRole(ADMIN), HttpStatus.OK);}
    @GetMapping(path = "getAllUsers")
    public ResponseEntity<ResponseDto> getAllUsers() {
        return new ResponseEntity<>(superAdminServices.getAllAppUsersByRole(USER), HttpStatus.OK);}


}
