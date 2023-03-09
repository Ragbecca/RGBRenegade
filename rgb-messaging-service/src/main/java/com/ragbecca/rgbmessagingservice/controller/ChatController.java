package com.ragbecca.rgbmessagingservice.controller;

import com.ragbecca.rgbmessagingservice.model.Message;
import com.ragbecca.rgbmessagingservice.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(value = "/send", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        return chatService.sendMessage(message);
    }

    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getAllMessages() {
        return chatService.getAllMessages();
    }
}
