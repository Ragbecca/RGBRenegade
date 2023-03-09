package com.ragbecca.rgbmessagingservice.service;

import com.ragbecca.rgbmessagingservice.constants.KafkaConstants;
import com.ragbecca.rgbmessagingservice.model.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private KafkaTemplate<String, Message> kafkaTemplate;
    @Autowired
    private KafkaConsumer<String, Message> consumer;

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

    @Override
    public ResponseEntity<?> getAllMessages() {
        List<TopicPartition> partitions = consumer.partitionsFor(KafkaConstants.KAFKA_TOPIC).stream()
                .map(p -> new TopicPartition(KafkaConstants.KAFKA_TOPIC, p.partition()))
                .collect(Collectors.toList());
        consumer.assign(partitions);
        consumer.poll(Duration.ofMillis(0));
        consumer.seekToBeginning(consumer.assignment());
        ConsumerRecords<String, Message> records = consumer.poll(Duration.ofMillis(100));
        List<Message> messages = new ArrayList<>();
        for (ConsumerRecord<String, Message> record : records) {
            messages.add(record.value());
        }
        return ResponseEntity.ok(messages);
    }
}
