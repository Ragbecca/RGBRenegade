package com.ragbecca.rgbmailkafkaservice.controller;

import com.ragbecca.rgbmailkafkaservice.model.EmailVerificationRequest;
import com.ragbecca.rgbmailkafkaservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @CrossOrigin("http://localhost:8090")
    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendMessage(@RequestBody EmailVerificationRequest emailVerificationRequest) {
        return mailService.sendMessageMail(emailVerificationRequest);
    }
}
