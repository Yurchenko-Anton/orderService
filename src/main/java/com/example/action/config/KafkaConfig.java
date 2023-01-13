package com.example.action.config;

import com.example.action.service.DriverLocationProcessor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    public static final String CONSUMER_DRIVER_LOCATION_TOPIC = "consumer-driver-location-topic";
    public static final String GROUP_ID = "group";
    public static final String PRODUCER_DRIVER_LOCATION_TO_CLIENT_TOPIC = "producer-driver-location-to-client-topic";

    private final DriverLocationProcessor driverLocationProcessor;

    @Bean
    public NewTopic consumerDriverLocation() {
        return TopicBuilder
                .name(CONSUMER_DRIVER_LOCATION_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic producerDriverLocationToClient() {
        return TopicBuilder
                .name(PRODUCER_DRIVER_LOCATION_TO_CLIENT_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @KafkaListener(groupId = GROUP_ID, topics = CONSUMER_DRIVER_LOCATION_TOPIC)
    public void driverLocationMessageProcessor(
            @Header(name = KafkaHeaders.RECEIVED_MESSAGE_KEY) String id,
            @Payload String msgAsString) {
        driverLocationProcessor.processDriverLocationMessage(id, msgAsString);
    }
}