package com.jangbee.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by test on 2015-12-20.
 */
@Service
public class EmailSender {
    private final static String sender  =   "jb9229@gmail.com";



    @Autowired JavaMailSender mailSender;

    public void sendMail(Email email) throws MessagingException {
        MimeMessage message     =   mailSender.createMimeMessage();


        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
        messageHelper.setSubject(email.getSubject());
        messageHelper.setTo(email.getReceiver());
        messageHelper.setFrom(sender);
        messageHelper.setText(email.getContent(), true);
        mailSender.send(message);

    }
}
