package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.models.dao.AppUserDao;
import com.eCommerce.demo.services.handlers.interfaces.AppUsersHandler;
import com.eCommerce.demo.services.handlers.interfaces.EmailsHandler;
import com.eCommerce.demo.services.handlers.interfaces.TokensHandler;
import com.eCommerce.demo.services.interfaces.InternalServices;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class InternalServicesImpl implements InternalServices {
    @Autowired
    private TokensHandler tokensHandler;
    @Autowired
    private AppUsersHandler appUsersHandler;
    @Autowired
    private EmailsHandler emailsHandler;

    @Override
    public String createAccessToken(AppUserDao appUser, HttpServletRequest request){
        try {

            return  tokensHandler.createAccessToken(appUser, request);
        }catch (Exception exception){
            log.error(exception.getMessage());
            return "failed";
        }
    }
    @Override
    public void checkLoginIpForEmail (String email, String ip) {
        if (!appUsersHandler.checkLoginIp(email, ip)){
            emailsHandler.sender(email,emailsHandler.buildNewIpLoginWarningEmail(email,ip),"new login to your account");
        }
    }

}
