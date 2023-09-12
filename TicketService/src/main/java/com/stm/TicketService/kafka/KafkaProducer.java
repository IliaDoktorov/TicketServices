package com.stm.TicketService.kafka;

import com.stm.TicketService.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, Ticket> kafkaTemplate;
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final String topicName;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, Ticket> kafkaTemplate, @Value("${kafka.topics.message-topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public synchronized void notifyKafka(Ticket ticket){
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                kafkaTemplate.send(topicName, UUID.randomUUID().toString(), ticket);
            }
        });
    }
}
