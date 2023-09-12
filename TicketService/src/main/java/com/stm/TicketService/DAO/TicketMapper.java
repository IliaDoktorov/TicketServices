package com.stm.TicketService.DAO;


import com.stm.TicketService.models.Ticket;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketMapper implements RowMapper<Ticket> {
    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(rs.getInt("id"));
        ticket.setRoute(rs.getInt("route"));
        ticket.setDateTime(rs.getTimestamp("date_time"));
        ticket.setPlaceNumber(rs.getInt("place_number"));
        ticket.setPrice(rs.getInt("price"));
        ticket.setReservedBy(rs.getInt("reserved_by"));
        return ticket;
    }
}
