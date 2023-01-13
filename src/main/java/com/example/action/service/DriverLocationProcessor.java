package com.example.action.service;

import com.example.action.dto.Order;
import com.example.action.repository.OrderDriverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.action.config.KafkaConfig.PRODUCER_DRIVER_LOCATION_TO_CLIENT_TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class DriverLocationProcessor {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderDriverRepository orderDriverRepository;

    public void processDriverLocationMessage(String id, String msg) {
        try {
            Order order = orderDriverRepository.findOrderByDriverId(Long.parseLong(id)).getBody();
            if (order != null) {
                sendMessage(order.getPassengerId().toString(), "Driver : " + id + " on the street : " + msg);
            } else log.info("Driver " + id + " dosen't have an active order");
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public void sendMessage(String passengerId, String msg) {
        kafkaTemplate.send(PRODUCER_DRIVER_LOCATION_TO_CLIENT_TOPIC, passengerId, msg);
    }
}