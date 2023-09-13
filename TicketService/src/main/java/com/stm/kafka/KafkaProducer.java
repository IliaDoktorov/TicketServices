package com.stm.kafka;

import com.stm.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Ticket> kafkaTemplate;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final String topicName;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, Ticket> kafkaTemplate, @Value("${kafka.topics.ticket-topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void notifyKafka(Ticket ticket){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                kafkaTemplate.send(
                        topicName,
                        2, // partition can be any, since event ordering is not important
                        UUID.randomUUID().toString(),
                        ticket);
                System.out.println("Kafka notified!");
            }
        });
    }
}
