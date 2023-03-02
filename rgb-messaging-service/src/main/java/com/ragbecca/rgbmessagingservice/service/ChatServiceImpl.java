package com.ragbecca.rgbmessagingservice.service;

import com.ragbecca.rgbmessagingservice.constants.KafkaConstants;
import com.ragbecca.rgbmessagingservice.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;

    @Override
    public ResponseEntity<?> sendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now().toString());
        try {
            //Sending the message to kafka topic queue
            kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, UUID.randomUUID().toString(), message).get();
            return ResponseEntity.ok().body("Message send to server.");
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.internalServerError().body("Message couldn't be send to the server.");
        }
    }
}
