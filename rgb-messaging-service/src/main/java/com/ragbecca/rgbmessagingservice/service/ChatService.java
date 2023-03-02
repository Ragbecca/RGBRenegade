package com.ragbecca.rgbmessagingservice.service;

import com.ragbecca.rgbmessagingservice.model.Message;
import org.springframework.http.ResponseEntity;

public interface ChatService {

    ResponseEntity<?> sendMessage(Message message);
}
