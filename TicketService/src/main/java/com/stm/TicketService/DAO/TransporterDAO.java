package com.stm.TicketService.DAO;

import com.stm.TicketService.models.Transporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransporterDAO {
    private final JdbcTemplate jdbcTemplate;
    private final TransporterMapper transporterMapper;

    @Autowired
    public TransporterDAO(JdbcTemplate jdbcTemplate, TransporterMapper transporterMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.transporterMapper = transporterMapper;
    }

    public void add(Transporter transporter) {
        jdbcTemplate.update("insert into transporter(title, phone_number) values(?, ?)",
                transporter.getTitle(),
                transporter.getPhoneNumber());
    }

    public void update(Transporter transporter) {
        jdbcTemplate.update("update transporter set title=?, phone_number=? where id=?",
                transporter.getTitle(),
                transporter.getPhoneNumber(),
                transporter.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("delete from transporter where id=?", id);
    }


    public Optional<Transporter> existByPhoneNumber(String phoneNumber) {
        return jdbcTemplate.query("select * from transporter where phone_number = ? for update",
                transporterMapper,
                phoneNumber).stream().findFirst();
    }

    // explicitly blocking required rows in our transaction to make sure nothing will change/remove our rows
    // between "select" query and "update/delete" command in Service class
    public Optional<Integer> existById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select id from transporter where id = ? for update", Integer.class, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
