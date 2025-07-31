package org.hulei.springboot.spring.mail;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String host, String port, final String username, final String password, String toAddress,
                                 String subject, String message) throws MessagingException {

        // 设置邮件服务器的属性
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // TLS
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL

        // 创建会话
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // 创建邮件
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(username));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new java.util.Date());
        msg.setText(message);

        // 发送邮件
        Transport.send(msg);
    }

    public static void main(String[] args) {
        // QQ 邮箱服务器配置
        String host = "smtp.qq.com";
        String port = "465"; // 465 for SSL, 587 for TLS
        String username = "1244171986@qq.com";
        String password = "ghehgzvlqptigfjf"; // 需要使用 QQ 邮箱的授权码，而不是密码

        // 接收者邮件
        String toAddress = "154347188@qq.com";

        // 邮件内容
        String subject = "测试邮件";
        String message = "这是一封通过 Jakarta Mail 发送的测试邮件。";

        try {
            sendEmail(host, port, username, password, toAddress, subject, message);
            System.out.println("邮件发送成功。");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
