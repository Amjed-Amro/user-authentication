package com.eCommerce.demo.services.interfaces;

import com.eCommerce.demo.models.dao.AppUserDao;
import jakarta.servlet.http.HttpServletRequest;

public interface InternalServices {
    String createAccessToken (AppUserDao appUser, HttpServletRequest request);

    void checkLoginIpForEmail(String email, String ip);
}
