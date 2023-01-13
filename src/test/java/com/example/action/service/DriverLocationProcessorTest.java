package com.example.action.service;

import com.example.action.dto.Order;
import com.example.action.repository.OrderDriverRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static com.example.action.config.KafkaConfig.PRODUCER_DRIVER_LOCATION_TO_CLIENT_TOPIC;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@RequiredArgsConstructor
@Testcontainers
public class DriverLocationProcessorTest {

    private static final DockerImageName myImage = DockerImageName.parse("confluentinc/cp-kafka:7.3.1")
            .asCompatibleSubstituteFor("confluentinc/cp-kafka");

    @Container
    public static final KafkaContainer kafka = new KafkaContainer(myImage);

    public static final String TEST_TOPIC = PRODUCER_DRIVER_LOCATION_TO_CLIENT_TOPIC;
    public static final String GROUP = "test-group";
    public static final String OFFSET = "earliest";

    private static final Long DRIVER_ID = 2L;
    private static final Long PASSENGER_ID = 1L;
    private static final String STREET_NAME = "test";

    @Mock
    private OrderDriverRepository orderDriverRepository;

    @InjectMocks
    private DriverLocationProcessor driverLocationProcessor;

    @Test
    public void shouldSendAndGetMessage() {
        Order order = new Order();
        order.setDriverId(DRIVER_ID);
        order.setPassengerId(PASSENGER_ID);

        kafka.start();

        String bootstrapService = kafka.getBootstrapServers();

        Properties sendProperties = new Properties();
        sendProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        sendProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        sendProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        Properties getProperties = new Properties();
        getProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapService);
        getProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        getProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        getProperties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP);
        getProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OFFSET);

        when(orderDriverRepository.findOrderByDriverId(DRIVER_ID)).thenReturn(ResponseEntity.ok(order));

        ProducerFactory producer = new DefaultKafkaProducerFactory(sendProperties);

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producer);
        kafkaTemplate.setDefaultTopic(TEST_TOPIC);

        driverLocationProcessor = new DriverLocationProcessor(kafkaTemplate, orderDriverRepository);
        driverLocationProcessor.processDriverLocationMessage(DRIVER_ID.toString(), STREET_NAME);

        producer.closeThreadBoundProducer();

        Consumer consumer = new KafkaConsumer<>(getProperties);
        consumer.subscribe(Arrays.asList(TEST_TOPIC));
        ConsumerRecords records = consumer.poll(Duration.ofMillis(1000));
        consumer.close();

        assertEquals(1, records.count());
    }
}