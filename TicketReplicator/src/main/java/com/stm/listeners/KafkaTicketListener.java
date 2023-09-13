package com.stm.listeners;

import com.stm.models.Ticket;
import com.stm.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaTicketListener {
    private final RedisRepository redisRepository;

    @Autowired
    public KafkaTicketListener(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @KafkaListener(
            id = "consumer-group-1",
            topics = "${kafka.topics.ticket-topic}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void processMessage(@Payload Ticket ticket){
        System.out.println(Thread.currentThread().getName() + " Got message from Kafka: " + ticket);
        redisRepository.add(ticket);
        System.out.println(Thread.currentThread().getName() + " Message saved to Redis: " + ticket);
    }
}
