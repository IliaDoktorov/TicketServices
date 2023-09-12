package com.stm.TicketService.DAO;

import com.stm.TicketService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    public void register(User user) {
        jdbcTemplate.update("insert into person(initials, username, password, role) values(?, ?, ?, ?)",
                user.getInitials(),
                user.getUsername(),
                user.getPassword(),
                user.getRole());
    }

    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("select * from person where username = ?", userMapper, username).stream().findFirst();
    }

    public Optional<User> getById(int customerId) {
        return jdbcTemplate.query("select * from person where id = ? for update", userMapper, customerId).stream().findFirst();
    }
}
