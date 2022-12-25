package com.eCommerce.demo.services.handlers.interfaces;

import com.eCommerce.demo.intities.LoginIp;

import java.util.Set;

public interface LoginIpsHandler {
    void addNewIpToEmail(String ip, String email);
    Set<LoginIp> findAllLoginIpsByEmail(String email);
}
