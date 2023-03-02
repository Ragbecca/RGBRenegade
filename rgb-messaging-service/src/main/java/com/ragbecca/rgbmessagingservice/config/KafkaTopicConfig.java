package com.ragbecca.rgbmessagingservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.ragbecca.rgbmessagingservice.constants.KafkaConstants.KAFKA_TOPIC;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic rgbChatTopic() {
        return TopicBuilder.name(KAFKA_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }
}
