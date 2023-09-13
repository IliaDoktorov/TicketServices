package com.stm.config;

import com.stm.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
@EnableKafka
public class KafkaConfig {
    // org.springframework.kafka.core.DefaultKafkaConsumerFactory will be injected
    private final ConsumerFactory<String, Ticket> consumerFactory;

    @Autowired
    public KafkaConfig(ConsumerFactory<String, Ticket> consumerFactory) {
        this.consumerFactory = consumerFactory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Ticket> kafkaListenerContainerFactory(){
        // Allows to create consumers which will be process messages from several Kafka partitions simultaneously
        ConcurrentKafkaListenerContainerFactory<String, Ticket> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // number of threads(consumers) should be == number of partitions in kafka for specific topic
        factory.setConcurrency(4);
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
