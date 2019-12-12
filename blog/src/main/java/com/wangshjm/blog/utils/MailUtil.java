package com.wangshjm.blog.utils;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
public class MailUtil {
    public static void sendEmailMessage(String email, String validateCode) {
        try {
            String host = "smtp.qq.com";   //发件人使用发邮件的电子信箱服务器
            String from = "910744380@qq.com";    //发邮件的出发地（发件人的信箱）
            String to = email;   //发邮件的目的地（收件人信箱）
            // Get system properties
            Properties props = System.getProperties();

            // Setup mail server
            props.put("mail.smtp.host", host);

            // Get session
            props.put("mail.smtp.auth", "true"); //这样才能通过验证

            MyAuthenticator myauth = new MyAuthenticator(from, "hyzncuxfcjntbced");
            Session session = Session.getDefaultInstance(props, myauth);

            //    session.setDebug(true);

            // Define message
            MimeMessage message = new MimeMessage(session);


            // Set the from address
            message.setFrom(new InternetAddress(from));

            // Set the to address
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set the subject
            message.setSubject("激活邮件通知");

            // Set the content
            message.setContent("<a href=\"http://localhost:8080/activecode?email=" + email + "&validateCode=" + validateCode + "\" target=\"_blank\">请于24小时内点击激活</a>", "text/html;charset=utf-8");
            message.saveChanges();

            Transport.send(message);

            log.info("send validateCode to " + email);
        } catch (Exception e) {

            log.info("Send Email Exception:" + e.getMessage());
        }
    }
}

class MyAuthenticator extends javax.mail.Authenticator {
    private String strUser;
    private String strPwd;

    public MyAuthenticator(String user, String password) {
        this.strUser = user;
        this.strPwd = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(strUser, strPwd);
    }
}
