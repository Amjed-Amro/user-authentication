package com.eCommerce.demo.services.handlers.interfaces;

import org.springframework.scheduling.annotation.Async;

public interface EmailsHandler {
    @Async
    void sender(String to, String email, String subject);

    String buildPasswordResetEmail(String name, String link);

    String buildConfirmationEmail(String name, String link);

    String buildNewIpLoginWarningEmail(String name, String ip);
}
