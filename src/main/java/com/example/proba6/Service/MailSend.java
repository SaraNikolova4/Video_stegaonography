package com.example.proba6.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service

public class MailSend {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MailProperties mailProperties;
    public void mailWithAttachment(String to, String body,String subject,String filePath) throws MessagingException {
        MimeMessage mimeMessage= mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
        helper.setSubject(subject);
        helper.setFrom(mailProperties.getUsername());
        helper.setText(body);
        helper.setTo(to);
        System.out.println(to);
        FileSystemResource file= new FileSystemResource(new File(filePath));
        helper.addAttachment(file.getFilename(),file);
        mailSender.send(mimeMessage);
        System.out.println("Mail was sent ");
    }

}
