package com.ragbecca.rgbmailkafkaservice.service;

import com.ragbecca.rgbmailkafkaservice.model.EmailVerificationRequest;
import org.springframework.http.ResponseEntity;

public interface MailService {

    ResponseEntity<?> sendMessageMail(EmailVerificationRequest emailVerificationRequest);

}
