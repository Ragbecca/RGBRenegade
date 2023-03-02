package com.ragbecca.rgbmailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerificationRequest {
    private Long userId;
    private String username;
    private String url;
    private String recipient;

    @Override
    public String toString() {
        return "EmailVerificationRequest{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", url='" + url + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }
}