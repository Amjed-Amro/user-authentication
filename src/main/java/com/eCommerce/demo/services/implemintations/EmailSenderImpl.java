package com.eCommerce.demo.services.implemintations;

import com.eCommerce.demo.services.EmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {

    private final static Logger LOGGER = LoggerFactory.getLogger(JavaMailSender.class);
    @Autowired
    private JavaMailSender mailSender;


    /**
     * this method is to create new email and send it
     *
     * @param to    is the email receiver
     * @param email is the email sender
     */
    @Override
    @Async
    public void sender(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("eCommerce@demo.com");
            mailSender.send(mimeMessage);
            LOGGER.info(String.format("confirmation email was sent to %s", to));
        } catch (MessagingException exception) {
            LOGGER.error("failed to send email", exception);
            throw new IllegalStateException("failed to send email");
        }
    }

}
