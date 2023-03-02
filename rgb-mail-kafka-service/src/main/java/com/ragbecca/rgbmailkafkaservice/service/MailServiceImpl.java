package com.ragbecca.rgbmailkafkaservice.service;

import com.ragbecca.rgbmailkafkaservice.constants.KafkaConstants;
import com.ragbecca.rgbmailkafkaservice.model.EmailVerificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private KafkaTemplate<String, EmailVerificationRequest> kafkaTemplate;

    @Override
    public ResponseEntity<?> sendMessageMail(EmailVerificationRequest emailVerificationRequest) {
        try {
            //Sending the message to kafka topic queue
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, UUID.randomUUID().toString(), emailVerificationRequest).get();
            return ResponseEntity.ok().body("Mail send to server.");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.internalServerError().body("Mail couldn't be send to the server.");
        }
    }
}
