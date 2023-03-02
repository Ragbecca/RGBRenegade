package com.ragbecca.rgbmessagingservice.listener;

import com.ragbecca.rgbmessagingservice.constants.KafkaConstants;
import com.ragbecca.rgbmessagingservice.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    @Autowired
    SimpMessagingTemplate template;

    @KafkaListener(topics = KafkaConstants.KAFKA_TOPIC, groupId = KafkaConstants.GROUP_ID)
    public void processMessage(Message message) {
        this.template.convertAndSend("/topic/group", message);
    }
}
