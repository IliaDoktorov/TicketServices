package com.stm.repository;

import com.stm.models.Ticket;

public interface RedisRepository {
    public void add(Ticket ticket);
}
