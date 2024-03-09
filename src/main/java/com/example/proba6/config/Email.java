package com.example.proba6.config;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.internet.MimeMessage;

@Configuration
public class Email {


    public static int generateRandomNumber() {
        // Создајте објект од класата Random
        Random random = new Random();

        // Генерирајте случаен четирицифрен број (од 1000 до 9999)
        int randomNumber = random.nextInt(9000) + 1000;

        return randomNumber;
    }

    private static String kod;

    public String getcode()
    {
        return kod;
    }
    public void sendemail(String email) {


        // Одредете ги конфигурациските поставки за праќање на маил
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); // Сменете го со вашиот SMTP сервер
        properties.put("mail.smtp.port", "587"); // Сменете го ако е потребно
        properties.put("mail.smtp.auth", "true"); // За SMTP автентикација
        properties.put("mail.smtp.starttls.enable", "true"); // Додадете ова
        properties.put("mail.smtp.ssl.trust", "*"); // за игнорирање на сертифкат.

        // Корисничко име и лозинка за SMTP автентикација
        String username = "informaciskabezbednost830@gmail.com";
        String password = "xdlchewnvyleczni";

        // Создадете Authenticator објект за SMTP автентикација
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        // Создадете објект за сесија со поставките и Authenticator
        Session session = Session.getInstance(properties, authenticator);

        try {
            // Создадете објект за содржина на пораката
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("saranikolova54@gmail.com")); // Поставете го вашиот е-маил
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // Поставете го е-маилот на примачот
            message.setSubject("Pin: ");
            // konvetiraj vo string
            kod =String.valueOf(generateRandomNumber());
            // prati go kodot
            message.setText(kod);

            // Пратете го маилот
            Transport.send(message);

            System.out.println("Email sent successfully.");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

