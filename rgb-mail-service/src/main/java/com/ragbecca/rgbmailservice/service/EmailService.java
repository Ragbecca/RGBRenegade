package com.ragbecca.rgbmailservice.service;

import com.ragbecca.rgbmailservice.model.EmailDetails;
import com.ragbecca.rgbmailservice.model.EmailVerificationRequest;

public interface EmailService {

    void sendSimpleMail(EmailDetails details);

    void sendVerificationMail(EmailVerificationRequest verificationRequest);

    void sendMailWithAttachment(EmailDetails details);
}
