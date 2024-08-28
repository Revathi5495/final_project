package com.sails.client_connect.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailService {

    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

//    public EmailService() {
//        mailSender = new JavaMailSenderImpl();
//        ((JavaMailSenderImpl) mailSender).setHost("smtp.gmail.com");
//        ((JavaMailSenderImpl) mailSender).setPort(587);
//        ((JavaMailSenderImpl) mailSender).setUsername("vinay121v@gmail.com"); // Your email here
//        ((JavaMailSenderImpl) mailSender).setPassword("vinay91413"); // Your email password here
//
//        Properties props = ((JavaMailSenderImpl) mailSender).getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//    }


//    public void sendCredentials(String toEmail, String username, String password) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(toEmail);
//            helper.setSubject("Your Account Credentials");
//            helper.setText("Dear " + username + ",\n\nYour account has been created.\n\nUsername: " + username + "\nPassword: " + password + "\n\nPlease log in and update your password.\n\nBest regards,\nYour Company");
//
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Failed to send email", e);
//        }
//    }

    public void sendDynamicEmail(String fromEmail, String toEmail, String subject, String body) throws MessagingException {


//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setFrom(fromEmail);
//        helper.setTo(toEmail);
//        helper.setSubject(subject);
//        helper.setText(body, true);
//
//        mailSender.send(message);
//        System.out.println("Email sent successfully");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        System.out.println("Mail Sent...");
    }

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
        System.out.println("Otp Sent...");
    }
}
