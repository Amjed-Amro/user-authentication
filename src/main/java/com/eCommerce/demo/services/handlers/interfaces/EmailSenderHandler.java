package com.eCommerce.demo.services.handlers.interfaces;

import org.springframework.scheduling.annotation.Async;

public interface EmailSender {


    @Async
    void sender(String to, String email, String subject);

    String buildPasswordResetEmail(String name, String link);

    String buildConfirmationEmail(String name, String link);
}
