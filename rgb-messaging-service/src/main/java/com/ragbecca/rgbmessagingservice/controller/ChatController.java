package com.ragbecca.rgbmessagingservice.controller;

import com.ragbecca.rgbmessagingservice.model.Message;
import com.ragbecca.rgbmessagingservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        return chatService.sendMessage(message);
    }
}
