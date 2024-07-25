package com.uce.edu.service.emailService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/**
 * EmailService
 */
@Service
public class EmailService {

    /**
     * Dependencias
     */
    @Autowired
    private JavaMailSender mailSender;

    /**
     * Método para enviar un email con un archivo adjunto
     * @param to
     * @param subject
     * @param text
     * @param pdfOutputStream
     * @param filename
     * @throws MessagingException
     * @throws IOException
     */
    public void sendEmailWithPdf(String to, String subject, String text, ByteArrayOutputStream pdfOutputStream,
            String filename) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("ucepruebas@gmail.com");
        helper.setSubject(subject);
        helper.setText(text);

        // Convertir ByteArrayOutputStream a ByteArrayResource para el adjunto
        ByteArrayResource pdfResource = new ByteArrayResource(pdfOutputStream.toByteArray());

        helper.addAttachment(filename, pdfResource);

        mailSender.send(message);

        System.out.println("Email enviado con éxito!");
    }

    /**
     * Método para enviar un email con dos archivos adjuntos
     * @param to
     * @param subject
     * @param text
     * @param pdfOutputStream
     * @param filename
     * @return
     */
    public boolean sendEmailWithPdfBoolean(String to, String subject, String text,
            ByteArrayOutputStream pdfOutputStream, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setFrom("ucepruebas@gmail.com");
            helper.setSubject(subject);
            helper.setText(text);

            // Convertir ByteArrayOutputStream a ByteArrayResource para el adjunto
            ByteArrayResource pdfResource = new ByteArrayResource(pdfOutputStream.toByteArray());

            helper.addAttachment(filename, pdfResource);

            mailSender.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Método para enviar un email con dos archivos adjuntos
     * @param to
     * @param subject
     * @param text
     * @param pdfOutputStream1
     * @param filename1
     * @param pdfOutputStream2
     * @param filename2
     * @return
     */
    public boolean sendEmailWithPdfBoolean(String to, String subject, String text,
            ByteArrayOutputStream pdfOutputStream1, String filename1, ByteArrayOutputStream pdfOutputStream2,
            String filename2) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setFrom("ucepruebas@gmail.com");
            helper.setSubject(subject);
            helper.setText(text);

            // Convertir ByteArrayOutputStream a ByteArrayResource para los adjuntos
            ByteArrayResource pdfResource1 = new ByteArrayResource(pdfOutputStream1.toByteArray());
            ByteArrayResource pdfResource2 = new ByteArrayResource(pdfOutputStream2.toByteArray());

            // Añadir los adjuntos
            helper.addAttachment(filename1, pdfResource1);
            helper.addAttachment(filename2, pdfResource2);

            mailSender.send(message);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendEmailWithCode(String to, String subject, String codigo, LocalDateTime fechaCaducidad) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setFrom("ucepruebas@gmail.com");
        helper.setSubject(subject);
        helper.setText("Código: " + codigo +"\nEste código es válido hasta: " + fechaCaducidad);

        mailSender.send(message);

        System.out.println("Email con código enviado con éxito!");
    }

}
