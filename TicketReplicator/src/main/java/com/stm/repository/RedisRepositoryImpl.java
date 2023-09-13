package com.stm.repository;

import com.stm.models.Ticket;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepositoryImpl implements RedisRepository{
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    private static final String KEY = "Ticket";

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void add(Ticket ticket) {
        hashOperations.put(KEY, ticket.getId(), ticket);
    }
}
