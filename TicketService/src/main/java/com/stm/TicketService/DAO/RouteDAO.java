package com.stm.TicketService.DAO;

import com.stm.TicketService.models.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RouteDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RouteDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Route route) {
        jdbcTemplate.update("insert into route(departure_point, destination_point, transporter_id, travel_time) values(?, ?, ?, ?)",
                route.getDeparturePoint(), route.getDestinationPoint(), route.getTransporter(), route.getTravelTime());
    }

    public void update(Route route) {
        jdbcTemplate.update("update route set departure_point=?, destination_point=?, transporter_id=?, travel_time=? where id=?",
                route.getDeparturePoint(),
                route.getDestinationPoint(),
                route.getTransporter(),
                route.getTravelTime(),
                route.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("delete from route where id=?", id);
    }

    public Optional<Integer> existById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select id from route where id = ? for update", Integer.class, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
