package com.stm.TicketService.DAO;

import com.stm.TicketService.DTO.FilterRequestDTO;
import com.stm.TicketService.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TicketDAO {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TicketMapper ticketMapper;

    @Autowired
    public TicketDAO(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, TicketMapper ticketMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.ticketMapper = ticketMapper;
    }

    public void add(Ticket ticket){
        jdbcTemplate.update("insert into ticket(route, date_time, place_number, price) values(?, ?, ?, ?)",
                ticket.getRoute(),
                ticket.getDateTime(),
                ticket.getPlaceNumber(),
                ticket.getPrice());
    }

    public void update(Ticket ticket){
        jdbcTemplate.update("update ticket set route=?, date_time=?, place_number=?, price=? where id=?",
                ticket.getRoute(),
                ticket.getDateTime(),
                ticket.getPlaceNumber(),
                ticket.getPrice(),
                ticket.getId());
    }

    public void delete(int id){
        jdbcTemplate.update("delete from ticket where id=?", id);
    }

    public void reserve(int customerId, int ticketId){
        jdbcTemplate.update("update ticket set reserved_by=? where id=?",
                customerId,
                ticketId);
    }

    public Optional<Ticket> getById(int id) {
            return jdbcTemplate.query("select * from ticket where id = ? for update",
                    ticketMapper,
                    id).stream().findFirst();
    }

    public Optional<Ticket> existByRouteAndPlaceNumber(int routeId, int placeNumber){
        return jdbcTemplate.query("select * from ticket where route=? and place_number=?",
                ticketMapper,
                routeId,
                placeNumber).stream().findFirst();
    }

    public List<Ticket> listByCustomerId(int customerId) {
        return jdbcTemplate.query("select * from ticket where reserved_by=?",
                ticketMapper,
                customerId);
    }

    // Here we could use PageRequest from SpringData,
    // but since all ORMs are prohibited we'll manage with simple limit and offset
    public List<Ticket> available(int page, int itemsPerPage, FilterRequestDTO filterRequestDTO){
        StringBuilder sql = new StringBuilder();

        // values are Objects, since we store String and Date to use in NamedParameterJdbcTemplate
        Map<String, Object> params = new HashMap<>();

        params.put("startDateTime", filterRequestDTO.getStartDateTime());
        params.put("endDateTime", filterRequestDTO.getEndDateTime());
        params.put("transporter", "%" + filterRequestDTO.getTransporter() + "%");
        params.put("limit", itemsPerPage);
        params.put("offset", itemsPerPage * (page - 1));
        params.put("departure_point", "%" + filterRequestDTO.getDeparturePoint() + "%");
        params.put("destination_point", "%" + filterRequestDTO.getDestinationPoint() + "%");

        // Huge and ugly sql query. It could be fixed by using Spring DataJPA with ExampleMatchers
        sql
                .append("select ticket.id, ticket.route, ticket.date_time, ticket.place_number, ticket.price, ticket.reserved_by ")
                .append("from ticket join route on ticket.route=route.id join transporter on route.transporter_id=transporter.id ")
                .append("where ")
                .append("ticket.reserved_by is null ")
                // below filter parameters could be used separately
                // by checking existence in FilterRequestDTO and appending respective conditions to sql query
                .append("and ticket.date_time between :startDateTime and :endDateTime ")
                .append("and transporter.title like :transporter ")
                .append("and route.departure_point like :departure_point and route.destination_point like :destination_point ")
                .append("limit :limit offset :offset ");

        return namedParameterJdbcTemplate.query(sql.toString(),
                params,
                ticketMapper);

    }
}
