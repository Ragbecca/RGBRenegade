package com.ragbecca.rgbmailservice.listener;

import com.ragbecca.rgbmailservice.constants.KafkaConstants;
import com.ragbecca.rgbmailservice.model.EmailVerificationRequest;
import com.ragbecca.rgbmailservice.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EmailVerificationRequestListener {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void processEmail(String verificationRequestString) {
        String[] splitRequest = verificationRequestString.replace("{", "").replace("}", "").split(",");
        Long userId = Long.parseLong((Arrays.stream(splitRequest).filter(p -> p.contains("userId")).findFirst().get().split(":")[1]));
        String username = Arrays.stream(splitRequest).filter(p -> p.contains("username")).findFirst().get().split(":")[1].replace("\"", "");
        String url = Arrays.stream(splitRequest).filter(p -> p.contains("url")).findFirst().get().replace("url\":", "").replace("\"", "");
        String recipient = Arrays.stream(splitRequest).filter(p -> p.contains("recipient")).findFirst().get().split(":")[1].replace("\"", "");
        EmailVerificationRequest emailVerificationRequest = new EmailVerificationRequest(userId, username, url, recipient);
        emailService.sendVerificationMail(emailVerificationRequest);
    }
}
