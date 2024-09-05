package com.cripto.project.datasource.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cripto.project.domain.entities.CredentialsEntity;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeAppEmail(CredentialsEntity credentials, String pass, String token) {
        String htmlMsg = """
                        <h1>Welcome to Our School System</h1>
                        <p>Your login information:</p>

                        <div class="info">
                            <p><strong>Username:</strong> %s</p>
                            <p><strong>Password:</strong> %s</p>
                        </div>
                        <p>To access your account, please follow these steps:</p>
                        <ol>
                            <li>Visit our login page at <a href="http://localhost:3000/auth/verify-account/%s">https://login.schoolsystem.edu</a></li>
                            <li>Enter your username and password</li>
                        </ol>
                        <div class="warning">
                            <p><strong>Important:</strong></p>
                            <ul>
                                <li>For security reasons, please change your password after your first login.</li>
                                <li>If you didn't request this account, please contact our IT support immediately at support@schoolsystem.edu.</li>
                                <li>Never share your login information with anyone.</li>
                            </ul>
                        </div>
                        <p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>
                        <p>Best regards,<br>The School System Team</p>
                """;

        MimeMessage message = mailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(credentials.getUsername());
            helper.setSubject("Welcome to Our School System.");
            helper.setText(String.format(htmlMsg, credentials.getUsername(), pass, token), true); // true indicates HTML
            helper.setFrom("noreply@schoolsystem.edu");

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Maneja la excepción adecuadamente en tu aplicación
        }
    }

    public void sendRecupereAccountEmail(String email, String token) {
        String htmlMsg = """
                        <h1>Password Reset Request</h1>
                        <p>We received a request to reset your password for your account associated with this email address.</p>

                        <div class="info">
                            <p><strong>Email:</strong> %s</p>
                        </div>
                        <p>To reset your password, please click the link below:</p>
                        <p><a href="http://localhost:3000/auth/reset-password/%s">Reset Your Password</a></p>
                        <p>If you didn't request a password reset, you can safely ignore this email.</p>

                        <div class="warning">
                            <p><strong>Important:</strong></p>
                            <ul>
                                <li>For security reasons, this link will expire in 24 hours.</li>
                                <li>If you continue having issues, please contact our IT support at support@schoolsystem.edu.</li>
                            </ul>
                        </div>
                        <p>If you have any questions or need further assistance, feel free to contact our support team.</p>
                        <p>Best regards,<br>The School System Team</p>
                """;

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Password Reset Request");
            helper.setText(String.format(htmlMsg, email, token), true); // true indicates HTML
            helper.setFrom("noreply@schoolsystem.edu");

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Maneja la excepción adecuadamente en tu aplicación
        }
    }

}
