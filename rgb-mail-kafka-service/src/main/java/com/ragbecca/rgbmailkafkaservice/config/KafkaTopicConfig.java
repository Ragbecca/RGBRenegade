package com.ragbecca.rgbmailkafkaservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.ragbecca.rgbmailkafkaservice.constants.KafkaConstants.KAFKA_TOPIC;

//Automatically create the Kafka Topic
@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic rgbEmailVerificationTopic() {
        return TopicBuilder.name(KAFKA_TOPIC)
                .partitions(1)
                .replicas(1)
                .compact()
                .build();
    }
}
