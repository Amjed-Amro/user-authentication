package com.eCommerce.demo.services.handlers.implementations;

import com.eCommerce.demo.services.handlers.interfaces.EmailsHandler;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EmailsHandlerImpl implements EmailsHandler {

    @Autowired
    private JavaMailSender mailSender;


    /**
     * this method is to create new email and send it
     * @param to    is the email receiver
     * @param email is the email sender
     */
    @Override
    @Async
    public void sender(String to, String email, String subject) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("eCommerce@demo.com");
            mailSender.send(mimeMessage);
            log.info(String.format("email was sent to %s", to));
        } catch (MessagingException exception) {
            log.error("failed to send email", exception);
            throw new IllegalStateException("failed to send email");
        }
    }
    @Override
    public String buildPasswordResetEmail (String name, String link){
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset your password</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Please click on the below link to reset your password: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
    @Override
    public String buildConfirmationEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }
    @Override
    public String buildNewIpLoginWarningEmail(String name, String ip){
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "    <link rel=\"icon\" sizes=\"\" href=\"https://yt3.ggpht.com/a-/AN66SAzzGZByUtn6CpHHJVIEOuqQbvAqwgPiKy1RTw=s900-mo-c-c0xffffffff-rj-k-no\" type=\"image/jpg\" />\n" +
                "    <title>SolarWinds Orion Email Alert Template</title>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <style>\n" +
                "        body {\n" +
                "            background-color: #f0f0f0;\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            color: #404040;\n" +
                "        }\n" +
                "\n" +
                "        .center {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        .tight {\n" +
                "            padding: 15px 30px;\n" +
                "        }\n" +
                "\n" +
                "        td {\n" +
                "            padding: 20px 50px 30px 50px;\n" +
                "        }\n" +
                "\n" +
                "        td.notification {\n" +
                "            padding: 10px 50px 30px 50px;\n" +
                "        }\n" +
                "\n" +
                "        small,\n" +
                "        .small {\n" +
                "            font-size: 12px;\n" +
                "        }\n" +
                "\n" +
                "        .footer {\n" +
                "            padding: 15px 30px;\n" +
                "        }\n" +
                "\n" +
                "        .footer p {\n" +
                "            font-size: 12px;\n" +
                "            margin: 0;\n" +
                "            color: #606060;\n" +
                "        }\n" +
                "\n" +
                "        a,\n" +
                "        a:hover,\n" +
                "        a:visited {\n" +
                "            color: #000000;\n" +
                "            text-decoration: underline;\n" +
                "        }\n" +
                "\n" +
                "        h1,\n" +
                "        h2 {\n" +
                "            font-size: 22px;\n" +
                "            color: #404040;\n" +
                "            font-weight: normal;\n" +
                "        }\n" +
                "\n" +
                "        p {\n" +
                "            font-size: 15px;\n" +
                "            color: #606060;\n" +
                "        }\n" +
                "\n" +
                "        .general {\n" +
                "            background-color: white;\n" +
                "        }\n" +
                "\n" +
                "        .notification h1 {\n" +
                "            font-size: 26px;\n" +
                "            color: #000000;\n" +
                "            font-weight: normal;\n" +
                "        }\n" +
                "\n" +
                "        .notification p {\n" +
                "            font-size: 18px;\n" +
                "        }\n" +
                "\n" +
                "        .notification p.small {\n" +
                "            font-size: 14px;\n" +
                "        }\n" +
                "\n" +
                "        .icon {\n" +
                "            width: 32px;\n" +
                "            height: 32px;\n" +
                "            line-height: 32px;\n" +
                "            display: inline-block;\n" +
                "            text-align: center;\n" +
                "            border-radius: 16px;\n" +
                "            margin-right: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .failure {\n" +
                "            border-top: 20px #b02020 solid;\n" +
                "            background-color: #db9c9b;\n" +
                "        }\n" +
                "\n" +
                "        .critical {\n" +
                "            border-top: 20px #c05050 solid;\n" +
                "            background-color: #e2afae;\n" +
                "        }\n" +
                "\n" +
                "        .warning {\n" +
                "            border-top: 20px #c08040 solid;\n" +
                "            background-color: #e0c4aa;\n" +
                "        }\n" +
                "\n" +
                "        .healthy {\n" +
                "            border-top: 20px #80c080 solid;\n" +
                "            background-color: #c6e2c3;\n" +
                "        }\n" +
                "\n" +
                "        .information {\n" +
                "            border-top: 20px #50a0c0 solid;\n" +
                "            background-color: #b5d5e2;\n" +
                "        }\n" +
                "\n" +
                "        .failure p {\n" +
                "            color: #3d120f;\n" +
                "        }\n" +
                "\n" +
                "        .critical p {\n" +
                "            color: #3d211f;\n" +
                "        }\n" +
                "\n" +
                "        .warning p {\n" +
                "            color: #44311c;\n" +
                "        }\n" +
                "\n" +
                "        .healthy p {\n" +
                "            color: #364731;\n" +
                "        }\n" +
                "\n" +
                "        .information p {\n" +
                "            color: #273c47;\n" +
                "        }\n" +
                "\n" +
                "        .failure .icon {\n" +
                "            background-color: #b02020;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "\n" +
                "        .critical .icon {\n" +
                "            background-color: #c05050;\n" +
                "            color: #ffffff;\n" +
                "            font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        .warning .icon {\n" +
                "            background-color: #c08040;\n" +
                "            color: #ffffff;\n" +
                "            font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif;\n" +
                "        }\n" +
                "\n" +
                "        .healthy .icon {\n" +
                "            background-color: #80c080;\n" +
                "            color: #ffffff;\n" +
                "        }\n" +
                "\n" +
                "        .information .icon {\n" +
                "            background-color: #50a0c0;\n" +
                "            color: #ffffff;\n" +
                "            font-family: Georgia, \"Times New Roman\", Times, serif;\n" +
                "            font-style: italic;\n" +
                "        }\n" +
                "\n" +
                "        .content {\n" +
                "            width: 600px;\n" +
                "        }\n" +
                "\n" +
                "        @media only screen and (max-width: 600px) {\n" +
                "            .content {\n" +
                "                width: 100%;\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        @media only screen and (max-width: 400px) {\n" +
                "            td {\n" +
                "                padding: 15px 25px;\n" +
                "            }\n" +
                "\n" +
                "            h1,\n" +
                "            h2 {\n" +
                "                font-size: 20px;\n" +
                "            }\n" +
                "\n" +
                "            p {\n" +
                "                font-size: 13px;\n" +
                "            }\n" +
                "\n" +
                "            small,\n" +
                "            .small {\n" +
                "                font-size: 11px;\n" +
                "            }\n" +
                "\n" +
                "            td.notification {\n" +
                "                text-align: center;\n" +
                "                padding: 10px 25px 15px 25px;\n" +
                "            }\n" +
                "\n" +
                "            .notification h1 {\n" +
                "                font-size: 22px;\n" +
                "            }\n" +
                "\n" +
                "            .notification p {\n" +
                "                font-size: 16px;\n" +
                "            }\n" +
                "\n" +
                "            .notification p.small {\n" +
                "                font-size: 12px;\n" +
                "            }\n" +
                "\n" +
                "            .icon {\n" +
                "                display: block;\n" +
                "                margin: 0 auto 10px auto;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"margin: 0; padding: 0\">\n" +
                "    <table style=\"border: none\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "        <tr>\n" +
                "            <td style=\"padding: 15px 0\">\n" +
                "                <table style=\"border: none; margin-left: auto; margin-right: auto\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"content\">\n" +
                "\n" +
                "                    <!-- Start: Warning Notification -->\n" +
                "                    <tr>\n" +
                "                        <td class=\"warning notification\">\n" +
                "                            <h1><span class=\"icon\">&quest;</span>Warning</h1>\n" +
                "                            <p>New Ip address login warning</p>\n" +
                "                            <p class=\"small\">Dear "+name+" ,there was login to your account from new IP address: "+ip+". If it was not you, please consider changing your password since your account is endangered</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <!-- End: Warning Notification -->\n" +
                "\n" +
                "                    \n" +
                "                    <!-- Start: White block with text content -->\n" +
                "                    <tr>\n" +
                "                        <td class=\"general center\">\n" +
                "                            <h2>eCommerce Company</h2>\n" +
                "                            <p>thank you for being part of our family.</p>\n" +
                "                            <p class=\"small\">stay safe</p>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <!-- End: White block with text content -->\n" +
                "\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "\n" +
                "</html>";
    }

}
