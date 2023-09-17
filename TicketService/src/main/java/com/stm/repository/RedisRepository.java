package com.stm.repository;

import com.stm.models.Ticket;

import java.util.Map;

public interface RedisRepository {
    public Map<Object, Ticket> findAllByCustomerId(int customerId);
}
