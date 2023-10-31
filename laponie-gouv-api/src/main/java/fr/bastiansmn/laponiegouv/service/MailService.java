package fr.bastiansmn.laponiegouv.service;

import fr.bastiansmn.laponiegouv.configuration.MailConfig;
import fr.bastiansmn.laponiegouv.exception.TechnicalException;
import fr.bastiansmn.laponiegouv.exception.TechnicalRule;
import jakarta.annotation.Nullable;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final MailConfig mailConfig;

    public void sendMail(String from, List<String> to, String subject, String body, @Nullable MultipartFile[] files)
            throws TechnicalException {
        long totalSize;
        if (files != null) {
            totalSize = Arrays.stream(files).reduce(0L, (a, b) -> a + b.getSize(), Long::sum);
            if (totalSize > 4.5 * 1024 * 1024)
                throw new TechnicalException(
                        TechnicalRule.EMAIL_0001
                );
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfig.getHost());
        mailSender.setPort(mailConfig.getPort());
        mailSender.setUsername(mailConfig.getUsername());
        mailSender.setPassword(mailConfig.getPassword());
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.toString());
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setText(body, true);
            StringBuilder sEmail = new StringBuilder();
            for (String s : to) {
                sEmail.append(s);
                sEmail.append(",");
            }
            mimeMessageHelper.setTo(InternetAddress.parse(sEmail.toString()));
            mimeMessageHelper.setSubject(subject);
            if (files != null) {
                Arrays.stream(files).forEach(attachement -> {
                    try {
                        var filename = attachement.getOriginalFilename() == null ? "fichier_inconnu" : attachement.getOriginalFilename();
                        mimeMessageHelper.addAttachment(filename, attachement);
                    } catch (MessagingException e) {
                        LOGGER.error("Error when adding attachement to mail", e);
                    }
                });
            }
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new TechnicalException(
                    TechnicalRule.EMAIL_0001
            );
        }
    }
}
